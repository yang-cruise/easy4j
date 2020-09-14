<!--
 * @Descripttion:
 * @version:
 * @Author: Huang Lina
 * @Date: 2020-03-10 11:14:30
 * @LastEditors: Huang Lina
 * @LastEditTime: 2020-05-07 14:24:36
 -->
<template>
  <div>
    <el-breadcrumb separator="/">
      <el-breadcrumb-item
        v-for="item in breadcrumbList"
        :key="item.path"
      >
        <router-link :to="{ path: item.path }" v-if="item.path">{{ item.title }}</router-link>
        <template v-else>{{ item.title }}</template>
      </el-breadcrumb-item>
    </el-breadcrumb>
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
      breadcrumbList: [],
    }
  },
  watch: {
    $route: {
      immediate: true,
      deep: true,
      handler () {
        this.setBreadcrumb()
      }
    }
  },
  computed: {

  },
  mounted () {

  },
  methods: {
    setBreadcrumb () {
      let matched = this.$route.matched

      if (!matched || !matched.length) {
        this.breadcrumbList = []
        return
      }

      // 初始化
      this.breadcrumbList = []
      matched.forEach((item, index) => {
        if (item.meta.title) {
          let obj = {
            title: item.meta.title,
          }
          if (index !== matched.length - 1) {
            obj.path = item.path
          } else {
            // 最后一级不做跳转
            obj.path = ''
          }

          this.breadcrumbList.push(obj)
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
