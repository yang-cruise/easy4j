<template>
  <div class="widget-form">
    <draggable
      class="widget-form-list-container"
      v-model="widgetForm.list"
      v-bind="{group:'people', ghostClass: 'ghost',animation: 200, handle: '.drag-widget'}"
      @end="handleMoveEnd"
      @add="handleWidgetAdd"
    >
      <div class="widget-form-list">
        <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
          <widget-form-item
            v-for="(item, index) in widgetForm.list"
            :key="index"
            :itemData="item"
            :select.sync="selectWidget"
          />
        </el-form>
      </div>
    </draggable>
  </div>
</template>

<script>
import draggable from 'vuedraggable'

import WidgetFormItem from './widget-form-item'

export default {
  components: {
    draggable,
    WidgetFormItem,
  },
  props: {
    widgetForm: {
      type: Object,
      default: () => {
        return {
          list: [],
        }
      },
    },
    select: {
      type: Object,
      default: () => {},
    },
  },
  data () {
    return {
      selectWidget: this.select,
      rules: {},
      ruleForm: {},
    }
  },
  watch: {
    select (val) {
      this.selectWidget = val
    },

    selectWidget: {
      handler (val) {
        this.$emit('update:select', val)
      },
      deep: true
    },
  },
  computed: {

  },
  mounted () {

  },
  methods: {
    handleMoveEnd () {

    },

    handleWidgetAdd (event) {
      console.log(event, 'event')
      const newIndex = event.newIndex

      // 为拖拽到容器的元素添加唯一 key
      const key = Date.parse(new Date()) + '_' + Math.ceil(Math.random() * 99999)
      this.$set(this.widgetForm.list, newIndex, {
        ...this.widgetForm.list[newIndex],
        options: {
          ...this.widgetForm.list[newIndex].options,
          remoteFunc: 'func_' + key,
        },
        key,
        // 绑定键值
        model: this.widgetForm.list[newIndex].type + '_' + key,
        rules: []
      })
      console.log(this.widgetForm.list, '333333')
      console.log(this.widgetForm.list[newIndex], '2222222')
      console.log(this.selectWidget, 'selectWidget')

      this.selectWidget = this.widgetForm.list[newIndex]
    },
  }
}
</script>

<style scoped>

</style>
