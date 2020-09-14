<template>
  <el-menu
    :default-active="defaultActive"
    class="all-menu"
    :collapse="isCollapse"
    :collapse-transition="false"
    :router="true"
    background-color="#191a23"
    text-color="#fff"
    @select="handelMenuSelect"
  >
    <template v-for="item in leftMenuList">
      <easy4j-menu-item
        :itemData="item"
        :parentData="{}"
        :key="item.index"
      />
    </template>
  </el-menu>
</template>

<script>
import Easy4jMenuItem from 'components/easy4j-menu/easy4j-menu-item.vue'

export default {
  components: {
    Easy4jMenuItem,
  },
  props: {
    isCollapse: {
      type: Boolean,
      default: false,
    },
  },
  data () {
    return {
      defaultActive: '',
      routeModule: '',
      routePath: '',
      leftMenuList: [],
    }
  },
  watch: {
    '$route': {
      deep: true,
      immediate: true,
      handler () {
        this.setDefaultActive()
      },
    },

    leftMenuList: {
      deep: true,
      immediate: true,
      handler () {
        this.setDefaultActive()
      },
    }
  },
  computed: {

  },
  mounted () {
    let leftMenuList = JSON.parse(localStorage.getItem('mySysMenuTree'))
    if (!leftMenuList || !leftMenuList.length) {
      this.$store.dispatch('getMySysMenuTree').then((res) => {
        this.leftMenuList = res
      })
    } else {
      this.leftMenuList = leftMenuList
    }
  },
  methods: {
    // 设置激活的索引
    setDefaultActive () {
      if (!this.leftMenuList || !this.leftMenuList.length) {
        return
      }

      this.routePath = this.$route.path

      this.$nextTick(() => {
        // console.log(this.$ROUTERUTILS.leftMenuRouters, 'leftMenuRouters')
        this.getDefaultActive(this.leftMenuList, 0)
      })
    },

    // 获取激活的索引
    getDefaultActive (leftMenuList, parentSort) {
      for (let item of leftMenuList) { // 利用foreach循环遍历
        if (item.url === this.routePath) {
          this.defaultActive = parentSort ? `${ parentSort }-${ item.id }` : `${ item.id }`
        } else if (item.subSysMenuList.length) {
          this.getDefaultActive(item.subSysMenuList, item.id) // 递归调用
        }
      }
    },

    // 菜单激活
    handelMenuSelect (index, indexPath) {
      // console.log(index, 'index')
      // console.log(indexPath, 'indexPath')
    },
  }
}
</script>

<style scoped>

</style>
