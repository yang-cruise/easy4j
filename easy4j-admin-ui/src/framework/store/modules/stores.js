import {
  getSysMenusSelfTree,
  getSysMenusSelfPermissions,
} from '@/framework/api/common'

import router from '@/framework/router/index'
import Utils from '@/framework/utils/index'

const state = {
  openedTab: [],
  loadingLabel: {},
  mySysMenuTree: [],
  myPermissions: [],
  permissionLoading: false,
  loginLoading: false,
}

const getters = {
  openedTab: openedTab => state.openedTab,
  loadingLabel: loadingLabel => state.loadingLabel,
  mySysMenuTree: mySysMenuTree => state.mySysMenuTree,
  myPermissions: myPermissions => state.myPermissions,
  permissionLoading: permissionLoading => state.permissionLoading,
  loginLoading: loginLoading => state.loginLoading,
}

const mutations = {
  setOpenedTab (state, openedTab) {
    state.openedTab = openedTab
  },

  setLoadingLabel (state, obj) {
    // 有新加的属性，要处理一下才能监听到
    let newObj = JSON.parse(JSON.stringify(state.loadingLabel))
    newObj = Object.assign(newObj, obj)
    state.loadingLabel = JSON.parse(JSON.stringify(newObj))
  },

  setMySysMenuTree (state, mySysMenuTree) {
    state.mySysMenuTree = mySysMenuTree
  },

  setMyPermissions (state, myPermissions) {
    state.myPermissions = myPermissions
  },

  setPermissionLoading (state, permissionLoading) {
    state.permissionLoading = permissionLoading
  },

  setLoginLoading (state, loginLoading) {
    state.loginLoading = loginLoading
  },
}

const actions = {
  getMySysMenuTree ({ commit }, goFlag = false) {
    return new Promise((resolve, reject) => {
      getSysMenusSelfTree().then(res => {
        let mySysMenuTree = res.data

        // 保存左菜单数据
        localStorage.setItem('mySysMenuTree', JSON.stringify(mySysMenuTree))
        commit('setMySysMenuTree', mySysMenuTree)

        // 判断是否是失效后重登录
        let route = router.app.$route
        let redirect = route.query.redirect
        if (redirect) {
          top.location.href = window.origin + '/#' + redirect
        } else if (goFlag) {
          // 跳转第一个要激活的菜单
          let firstRoute = Utils.getFirstItemByType(mySysMenuTree)
          top.location.href = window.origin + '/#' + firstRoute.url
        }

        resolve(mySysMenuTree)
      }).catch(error => {
        console.log(error)
        commit('setMySysMenuTree', [])
      })
    })
  },

  getMyPermissions ({ commit }) {
    if (state.permissionLoading) {
      return
    }

    return new Promise((resolve, reject) => {
      commit('setPermissionLoading', true)
      getSysMenusSelfPermissions().then(res => {
        commit('setPermissionLoading', false)
        let permissionList = res.data

        // 保存权限数据
        localStorage.setItem('myPermissions', JSON.stringify(permissionList))
        commit('setMyPermissions', permissionList)

        resolve(permissionList)
      }).catch(error => {
        console.log(error)
        commit('setPermissionLoading', false)
        commit('setMyPermissions', [])
      })
    })
  }
}

export default {
  state,
  getters,
  mutations,
  actions
}
