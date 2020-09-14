<template>
  <div class="easy4j-opened-tab">
    <div class="tab-pre toggle-btn" @click="run(-1)">
      <el-link
        :underline="false"
        icon="el-icon-arrow-left"
      />
    </div>
    <div class="opened-tabs">
      <draggable
        v-model="tabList"
        group="people"
        @end="saveTabs"
        tag="div"
        class="tab-inner-container"
        ref="draggableContainer"
        filter=".undraggable"
      >
        <el-tag
          v-for="(item, index) in tabList"
          :key="index.routeName"
          :type="routeName === item.routeName ? '' : 'info'"
          :effect="routeName === item.routeName ? 'dark' : 'plain'"
          closable
          class="esay4j-tag mar-r5"
          @click="goRoute(item.routeName)"
          @close="handleClose(item, index)"
          :class="[{ undraggable: item.routePath === '/' }, item.routeName]"
        >
          {{ item.title }}
        </el-tag>
      </draggable>
    </div>
    <div class="tab-next toggle-btn" @click="run(1)">
      <el-link
        :underline="false"
        icon="el-icon-arrow-right"
      />
    </div>
    <el-button plain size="small" icon="el-icon-refresh" @click="reloadPage">刷新</el-button>
  </div>
</template>

<script>
import { mapGetters, mapMutations } from 'vuex'

import draggable from 'vuedraggable'

export default {
  inject: ['reload'],
  components: {
    draggable,
  },
  props: {

  },
  data () {
    return {
      routeName: '',
      tabList: [],
    }
  },
  watch: {
    $route: {
      deep: true,
      immediate: true,
      handler () {
        this.routeName = this.$route.name

        this.tabList = [...this.openedTab]
      }
    },

    tabList: {
      deep: true,
      immediate: true,
      handler () {
        this.$nextTick(() => {
          this.checkOverflow()
        })
      },
    },
  },
  computed: {
    ...mapGetters(['openedTab']),
  },
  mounted () {
    this.tabList = [...this.openedTab]
  },
  methods: {
    ...mapMutations(['setOpenedTab']),

    reloadPage () {
      this.reload()
    },

    goRoute (routeName) {
      this.$router.push({ name: routeName })
    },

    handleClose (item, index) {
      // 只有一个页签
      if (this.tabList.length <= 1) {
        return
      }

      let arr = [...this.tabList]

      arr.splice(index, 1)

      // 删除当前
      if (this.routeName === item.routeName) {
        // 激活当前的前一个，删除当前
        this.$router.push({ name: arr[index - 1].routeName })
      } else {
        this.tabList = [...arr]
      }

      // 保存
      this.setOpenedTab(arr)
    },

    // 检判断是否发生溢出
    checkOverflow () {
      // 判断当前激活的页签的位置，激活它
      let index = this.tabList.findIndex(item => item.routeName === this.routeName)
      let len = this.tabList.length

      if (index > len / 2) {
        this.run(1)
      } else {
        this.run(-1)
      }
    },

    // 保存拖拽
    saveTabs (obj) {
      if (this.$store.getters.hasHome) {
        // 有首页
        if (obj.newIndex === 0) {
          // 首页的地位是无法撼动的！！！
          this.tabList = [...this.openedTab]
        } else {
          // 保存
          this.setOpenedTab(this.tabList)
        }
      } else {
        // 无首页
        // 保存
        this.setOpenedTab(this.tabList)
      }
    },

    // 左右切换页签
    run (num) {
      let container = document.querySelector('.tab-inner-container')
      if (!container) {
        return
      }

      switch (num) {
        case -1:
          container.scrollLeft = 0
          break
        case 1:
          container.scrollLeft = container.scrollWidth
          break
      }
    },
  }
}
</script>

<style scoped>
  .tab-inner-container {
    display: flex;
    flex: 1;
    flex-wrap: nowrap;
    overflow: hidden;
  }
</style>
