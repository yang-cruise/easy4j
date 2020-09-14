<template>
  <div class="components-block">
    <div class="components-title">{{ draggableType }}</div>
    <draggable tag="div" :list="componentLists" class="tag-container"
      v-bind="{group:{ name:'people', pull:'clone',put:false},sort:false, ghostClass: 'ghost'}"
    >
      <el-tag
        v-for="(item, index) in componentLists"
        :key="index"
      >
        <i :class="item.icon"></i>
        <span>{{item.name}}</span>
      </el-tag>
    </draggable>
  </div>
</template>

<script>
import draggable from 'vuedraggable'

export default {
  components: {
    draggable,
  },
  props: {
    draggableType: {
      type: String,
      default: '',
    },
    componentLists: {
      type: Array,
      default: () => [],
    },
  },
  data () {
    return {

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
