<!--
 * @Descripttion:
 * @version:
 * @Author: Huang Lina
 * @Date: 2020-04-28 17:14:40
 * @LastEditors: Huang Lina
 * @LastEditTime: 2020-04-29 15:28:15
-->
<template>
  <el-cascader
    v-model="selected"
    :options="allList"
    :props="cityProps"
    :clearable="clearable"
    :disabled="disabled"
    :show-all-levels="false"
    :collapse-tags="collapseTags"
    filterable
    @clearCheckedNodes="handleClear"
  ></el-cascader>
</template>

<script>
import cityList from './city'

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
    // 可清空标识
    clearable: {
      type: Boolean,
      default: true,
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
    // 展示的层级，可选0/1/2
    level: {
      type: [String, Number],
      default: 2,
    },
    // 多选模式下是否折叠Tag
    collapseTags: {
      type: Boolean,
      default: false,
    }
  },
  data () {
    return {
      cityList: cityList,
      selected: this.value,
      cityProps: {
        checkStrictly: true, // 选择任一级
        emitPath: false, // 只返回该节点的值，不返回路径
        value: 'value',
        label: 'label',
        children: 'children',
        multiple: this.multiple,
      },
    }
  },
  watch: {
    value: {
      deep: true,
      handler (val) {
        this.selected = this.value
      },
    },

    selected: {
      deep: true,
      handler (val) {
        this.$emit('input', val)
        this.$emit('change', val)
      },
    },
  },
  computed: {
    allList () {
      // 非数值
      if (isNaN(this.level)) {
        return []
      }
      let levelInt = parseInt(this.level)

      // 超出索引
      if (levelInt < 0 || levelInt > 2) {
        return []
      }

      let temp = [...this.cityList]

      this.getLevelData(temp, 0)

      return temp
    }
  },
  mounted () {

  },
  methods: {
    getLevelData (list, count = 0) {
      if (count < parseInt(this.level)) {
        count++
        for (let aItem of list) {
          if (aItem.children && aItem.children.length) {
            this.getLevelData(aItem.children, count)
          }
        }
      } else if (count === parseInt(this.level)) {
        for (let item of list) {
          // 删除下一级数据
          delete item.children
        }
      }
    },

    // 清空选中的节点，不生效
    handleClear () {
      // console.log('wwwwww')
      this.$emit('clear')
    },
  }
}
</script>

<style scoped lang='scss'>

</style>
