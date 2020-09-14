<template>
  <el-cascader
    v-model="selectDeptId"
    :placeholder="`请选择${ label }`"
    :show-all-levels="false"
    :options="deptOptions"
    :props="deptProps"
    clearable
  ></el-cascader>
</template>

<script>
import {
  getSysDeptsSelf,
} from '@/framework/api/common'

export default {
  components: {

  },
  props: {
    model: {
      prop: 'value',
      evnet: 'change'
    },

    value: {
      type: [String, Number, Array],
    },

    // 列表所属字段名称
    label: {
      type: String,
      default: '',
    },

    // 多选标识
    multiple: {
      type: Boolean,
      default: false,
    },
  },
  data () {
    return {
      selectDeptId: this.value,
      deptOptions: [],
      deptProps: {
        checkStrictly: true, // 选择任一级
        emitPath: false, // 只返回该节点的值，不返回路径
        value: 'id',
        label: 'deptName',
        children: 'subSysDeptList',
        multiple: this.multiple,
      },
    }
  },
  computed: {

  },
  mounted () {
    this.getDeptsList()
  },
  watch: {
    value: {
      deep: true,
      handler (val) {
        this.selectDeptId = this.value
      },
    },

    selectDeptId: {
      deep: true,
      handler (val) {
        this.$emit('input', val)
        this.$emit('change', val)
      },
    },
  },
  methods: {
    // 获取所有部门列表
    getDeptsList () {
      const vm = this
      getSysDeptsSelf().then(res => {
        vm.deptOptions = [...res.data]

        // 极联组件，叶子节点要删除代表子节点的字段
        vm.deptOptions = vm.deptListFmt(vm.deptOptions)
      }).catch(error => {
        console.log(error)
      })
    },

    // 删除无效字段
    deptListFmt (deptOptions) {
      for (let item of deptOptions) {
        if (item.subSysDeptList && item.subSysDeptList.length) {
          this.deptListFmt(item.subSysDeptList)
        }
      }
      return deptOptions
    },
  },
}
</script>

<style scoped lang="scss">

</style>
