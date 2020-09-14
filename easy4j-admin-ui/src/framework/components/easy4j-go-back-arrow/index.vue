<template>
  <div
    class="go-back-container"
    @click="$router.back(-1)"
  >
    <i class="el-icon-back"></i>
    返回
  </div>
</template>

<script>
export default {
  components: {

  },
  props: {

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

<style scoped lang='scss'>
  .go-back-container {
    display: inline-flex;
    justify-content: center;
    align-items: center;
    padding-right: 16px;
    border-right: 2px solid #DCDFE6;
    margin-right: 16px;
    cursor: pointer;
    i {
      margin-right: 4px;
    }
  }
</style>
