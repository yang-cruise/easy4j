/*
 * @Descripttion:
 * @version:
 * @Author: Huang Lina
 * @Date: 2020-03-10 11:14:30
 * @LastEditors: Huang Lina
 * @LastEditTime: 2020-04-27 10:40:41
 */
import Vue from 'vue'
import Router from 'vue-router'
import store from '../store/index'
import routes from './allRouters'
import Utils from '@/framework/utils/index'
import { setKeepAlive } from './routeUtils'

Vue.use(Router)

// 解决ElementUI导航栏中的vue-router在3.0版本以上重复点菜单报错问题
const originalPush = Router.prototype.push
Router.prototype.push = function push (location) {
  return originalPush.call(this, location).catch(err => err)
}

const router = new Router({
  routes: setKeepAlive(routes)
})

// 生成页签数据
const getOpenedTab = (to, from) => {
  // 获取旧的数据
  let storeOld = [...store.getters.openedTab]

  let routeName = to.name

  if (!to.meta.showOnTab) {
    return
  }

  if (store.getters.hasHome && !storeOld.length) {
    storeOld = [{
      title: '首页',
      routeName: 'Home',
      routePath: '/',
    }].concat(storeOld)
  }

  let hasTabArr = storeOld.filter(item => item.routeName === routeName)
  // 判断页签是否已存在
  // 不存在则添加页签
  if (!hasTabArr.length) {
    storeOld.push({
      title: to.meta.title,
      routeName: to.name,
      routePath: to.path,
    })
  }

  // 保存新的数据
  store.commit('setOpenedTab', storeOld)
}

router.beforeEach((to, from, next) => {
  if (!localStorage.getItem('Authorization')) {
    if (to.path === '/login') {
      next()
    } else if (to.path === '/wechat-callback') {
      next()
    } else {
      next({
        path: '/login',
        query: {
          // 登录后需重定向
          redirect: to.fullPath,
        }
      })
    }
  } else {
    if (to.path === '/login') {
      // 初始化
      Vue.prototype.$utils.init()

      next({
        path: '/login'
      })
    } else if (to.path === '/') {
      // 跳转首页
      if (store.getters.hasHome) {
        // 有首页
        next()
      } else {
        // 无首页
        // 跳转第一个要激活的菜单
        let leftMenuList = JSON.parse(localStorage.getItem('mySysMenuTree'))
        let firstRoute = Utils.getFirstItemByType(leftMenuList)
        next({ path: firstRoute.url })
      }
    } else {
      next()
    }
  }
  // next()
})

router.afterEach((to, from) => {
  // 生成面页签数据
  getOpenedTab(to, from)
})

export default router
