<template>
  <div class="draggable-components">
    <el-collapse v-model="activeNames" class="draggable-collapse">
      <!-- 可拖拽元素按类型展示 -->
      <!-- 表单字段 -->
      <el-collapse-item title="表单字段" name="1">
        <draggable-block
          draggableType="基础字段"
          :componentLists="basicComponents"
        />
        <draggable-block
          draggableType="高级字段"
          :componentLists="advanceComponents"
        />
      </el-collapse-item>

      <!-- 非表单字段 -->
      <el-collapse-item title="非表单字段" name="2">
        <draggable-block
          draggableType="布局字段"
          :componentLists="layoutComponents"
        />
        <draggable-block
          draggableType="非表单字段"
          :componentLists="unFormComponents"
        />
      </el-collapse-item>
    </el-collapse>
  </div>
</template>

<script>
import DraggableBlock from './draggable-block'

import {
  basicComponents,
  advanceComponents,
  layoutComponents,
  unFormComponents,
} from '@/framework/api/componentsConfig'

export default {
  components: {
    DraggableBlock,
  },
  props: {

  },
  data () {
    return {
      basicComponents,
      advanceComponents,
      layoutComponents,
      unFormComponents,
      activeNames: ['1', '2'],
    }
  },
  watch: {

  },
  computed: {

  },
  mounted () {

  },
  methods: {

  }
}
</script>

<style scoped>

</style>
