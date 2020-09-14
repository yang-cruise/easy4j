<template>
  <el-link
    :type="type"
    class="easy4j-link"
    @click="handleClick"
  >
    <slot></slot>
  </el-link>
</template>

<script>
export default {
  components: {

  },
  props: {
    type: {
      type: String,
      default: 'primary',
    },
  },
  data () {
    return {

    }
  },
  computed: {

  },
  mounted () {

  },
  watch: {

  },
  methods: {
    handleClick () {
      this.$emit('click')
    }
  },
}
</script>

<style scoped lang="scss">
  .easy4j-link {
    // border-bottom: 1px solid!important;
    // padding: 0px!important;
    text-decoration: underline;
    line-height: 1;
    // border-radius: 0px!important;
  }
</style>
