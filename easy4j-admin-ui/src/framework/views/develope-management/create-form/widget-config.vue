<template>
  <div v-if="show">
    <div>
      请配置字段属性
    </div>
    <el-form label-position="top">
      <el-form-item label="字段标识">
        <el-input v-model="widgetFormSelect.field"></el-input>
      </el-form-item>
      <el-form-item label="标签名">
        <el-input v-model="widgetFormSelect.name"></el-input>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  components: {

  },
  props: {
    widgetFormSelect: {
      type: Object,
      default: () => {},
    },
  },
  data () {
    return {

    }
  },
  watch: {

  },
  computed: {
    show () {
      return this.widgetFormSelect && Object.keys(this.widgetFormSelect).length
    }
  },
  mounted () {

  },
  methods: {

  }
}
</script>

<style scoped>

</style>
