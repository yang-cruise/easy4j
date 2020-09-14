<template>
<!-- 菜单数据查询接口未结束，不展示真实的下拉菜单。避免查询时间过长，只展示val的问题。 -->
  <el-select
    v-if="showFlag === 1"
    v-model="selectVal"
    :placeholder="`请选择${ label }`"
    :multiple="multiple"
    :disabled="disabled"
    :clearable="clearable"
    @change="handleChange"
    @clear="handleClear"
  >
    <el-option
      v-for="item in options"
      :key="item.key"
      :label="item.value"
      :value="item.key">
    </el-option>
  </el-select>
  <el-select
    v-else
    v-model="fakeVal"
    :placeholder="disabled ? '' : `请选择${ label }`"
    :disabled="disabled"
    :clearable="clearable"
  >
    <el-option
      v-for="item in fakeOptions"
      :key="item.key"
      :label="item.value"
      :value="item.key">
    </el-option>
  </el-select>
</template>

<script>
import {
  getSysDictItemsByCode
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
    // 列表所属字段标识
    dict: {
      type: String,
      default: '',
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
    // 禁用标识
    disabled: {
      type: Boolean,
      default: false,
    },
    // 清除标识
    clearable: {
      type: Boolean,
      default: true,
    },
  },
  data () {
    return {
      selectVal: `${ this.value }`,
      options: [],
      showFlag: 0, // 菜单数据查询接口结束标识
      fakeVal: '',
      fakeOptions: [],
    }
  },
  computed: {

  },
  mounted () {

  },
  watch: {
    dict: {
      immediate: true,
      handler () {
        // 初始化
        if (this.dict) {
          this.getSysDictItemsByCode(this.dict)
        }
      }
    },

    value: {
      deep: true,
      handler (val) {
        this.selectVal = `${ this.value }`
      },
    },

    selectVal: {
      deep: true,
      handler (val) {
        this.$emit('input', val)
        // this.$emit('change', val)
      },
    }
  },
  methods: {
    getSysDictItemsByCode (code) {
      const vm = this
      vm.showFlag = 0
      getSysDictItemsByCode({ code }).then(res => {
        vm.options = res.data
        vm.showFlag = 1

        vm.$forceUpdate()
      }).catch(error => {
        console.log(error)
      })
    },

    handleChange (val) {
      let arr = this.options.filter(item => item.key === val)
      let label = arr.length ? arr[0].value : ''

      this.$emit('change', val, label)
    },

    handleClear () {
      this.$emit('clear')
    }
  },
}
</script>

<style scoped lang="scss">

</style>
