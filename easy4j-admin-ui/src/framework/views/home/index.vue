<template>
  <div class="all-index">
    <el-container class="all-container">

      <!--移动端-->
      <template v-if="device === 'mobile'">
        <el-drawer
          size="255"
          direction="ltr"
          :visible.sync="isCollapse"
          :with-header="false">
          <div class="mobile-menu">
            <div class="header-logo">
              <img :src="siteConfig.logo" v-if="siteConfig.logo" class="logo-style"/>
              {{ siteConfig.siteName }}
            </div>
            <div class="container">
              <easy4j-menu
                :isCollapse="false"
              />
            </div>
          </div>
        </el-drawer>
      </template>
      <!--其他端-->
      <template v-else>
        <el-aside
          :width="asideWidth"
          class="all-aside"
        >
          <div class="header-logo" v-show="!isCollapse">
            <img :src="siteConfig.logo" v-if="siteConfig.logo" class="logo-style"/>
            {{ siteConfig.siteName }}
          </div>

          <el-scrollbar
            class="flex-1"
            :native="false"
            wrapStyle=""
            wrapClass="x-hidden"
            viewClass="full-height"
            viewStyle=""
            :noresize="false"
            tag="div"
          >
            <easy4j-menu
              :isCollapse="isCollapse"
            />
          </el-scrollbar>
        </el-aside>
      </template>

      <el-container v-loading="mainLoadingFlag">
        <el-header class="all-header">
          <div class="hearder-left">
            <div class="collapse-btn ">
              <div class="header-trigger">
                <i class="el-icon-s-fold" @click="isCollapse = true" v-if="!isCollapse"></i>
                <i class="el-icon-s-unfold" @click="isCollapse = false" v-else></i>
              </div>
            </div>

            <!-- 面包屑 -->
            <easy4j-breadcrumb/>
          </div>
          <div class="header-right">
            <!-- <div class="header-trigger">
              <div class="user">
                <el-avatar :src="userInfo.avatar"></el-avatar>
              </div>
            </div> -->
            <el-dropdown @command="handleCommand">
              <span class="el-dropdown-link">

                您好，{{ userInfo.realname || userInfo.account }}{{ deptName }}
                <i class="el-icon-arrow-down el-icon--right"></i>
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="userProfile">
                  <i class="el-icon-user"></i>
                  个人中心
                </el-dropdown-item>
                <el-dropdown-item command="updatePassword">
                  <i class="el-icon-edit"></i>
                  修改密码
                </el-dropdown-item>
                <el-dropdown-item command="logout">
                  <i class="el-icon-switch-button"></i>
                  退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </el-header>
        <el-main class="all-main">
          <!-- 已打开的页签 -->
          <easy4j-opened-tab/>

          <div class="main-inner" :class="[device]">
            <!-- 有缓存 -->
            <keep-alive :max="10">
              <router-view v-if="$route.meta.keepAlive"/>
            </keep-alive>

            <!-- 无缓存 -->
            <router-view v-if="!$route.meta.keepAlive"/>
          </div>
        </el-main>
      </el-container>
    </el-container>

    <!-- 修改密码 -->
    <update-password
      v-if="dialogUpdateVisible"
      :dialogUpdateVisible.sync="dialogUpdateVisible"
      :allowClose="true"
    />
  </div>
</template>

<script>
import Easy4jMenu from 'components/easy4j-menu/index.vue'
import Easy4jBreadcrumb from 'components/easy4j-breadcrumb/index.vue'
import Easy4jOpenedTab from 'components/easy4j-opened-tab/index.vue'

import UpdatePassword from '../login/dialog/update-password'
import { mixinDevice } from '@/framework/mixins/device.js'
export default {
  mixins: [ mixinDevice ],
  components: {
    Easy4jMenu,
    Easy4jBreadcrumb,
    Easy4jOpenedTab,
    UpdatePassword,
  },
  props: {

  },
  data () {
    return {
      isCollapse: false, // false
      asideWidth: '256px',
      outerWidth: '275px',
      mainLoadingFlag: false,
      userInfo: {
        realname: '',
        account: '',
      },
      siteConfig: {
        logo: '',
        siteName: '',
      },
      dialogUpdateVisible: false,
    }
  },
  watch: {
    isCollapse: {
      immediate: true,
      handler () {
        this.asideWidth = this.isCollapse ? 'auto' : '256px'
      }
    }
  },
  computed: {
    deptName () {
      if (!this.userInfo || !this.userInfo.dept) {
        return ''
      }

      return `(${ this.userInfo.dept.deptName })`
    }
  },
  mounted () {
    if (this.device === 'tablet') {
      this.isCollapse = true
    }
    let userInfo = localStorage.getItem('userInfo')
    if (userInfo) {
      this.userInfo = JSON.parse(userInfo)
    }

    this.loadSiteConfig()

    this.$eventBus.$on('reloadSiteConfig', () => {
      this.loadSiteConfig()
    })
  },
  methods: {
    handleCommand (command) {
      switch (command) {
        case 'userProfile':
          this.$router.push({ name: 'UserProfile' })
          break
        case 'updatePassword':
          this.dialogUpdateVisible = true
          break
        case 'logout':
          this.logout()
          break
      }
    },

    logout () {
      // 初始化
      this.$utils.init()

      this.$router.push({ name: 'Login' })
    },

    loadSiteConfig () {
      let siteConfig = localStorage.getItem('siteConfig')
      if (siteConfig) {
        this.siteConfig = JSON.parse(siteConfig)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
  .all-index {
    height: 100%;
  }

  .mobile-menu {
    width: 255px;
    overflow-x: hidden;
    height: 100vh;
    background: rgb(25, 26, 35);
    display: flex;
    flex-direction: column;
    .container {
      flex: 1;
      height: 90%;
      overflow: auto;
    }
  }
  .mobile {
    margin: 0;
    padding: 0;
  }

  .logo-style {
    max-height: 40px;
    margin-right: 10px;
  }
</style>
