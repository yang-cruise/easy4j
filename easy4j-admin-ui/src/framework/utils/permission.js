import store from '@/framework/store/index'

export default (permission) => {
  if (!permission) {
    return true
  }

  // 没权限
  let mySysMenu = JSON.parse(localStorage.getItem('myPermissions'))
  if (!mySysMenu || !mySysMenu.length) {
    store.dispatch('getMyPermissions').then((res) => {
      mySysMenu = res
    })
  }
  if (!mySysMenu || !mySysMenu.length) {
    return false
  }

  return mySysMenu.includes(permission)
}
