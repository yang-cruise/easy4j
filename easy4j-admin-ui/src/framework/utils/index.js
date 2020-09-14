import Vue from 'vue'
import store from '../store/index'

const getMeu = (meuTree, type) => {
  if (!meuTree || !meuTree.length) {
    return ''
  }

  for (let item of meuTree) {
    if (item.type === type) {
      return item
    } else {
      if (item.subSysMenuList && item.subSysMenuList.length) {
        return getMeu(item.subSysMenuList, type)
      }
    }
  }

  return ''
}

export default {
  // 提示
  alert (types, msg) {
    Vue.prototype.$message({
      type: types || 'success',
      message: msg,
      duration: 1500,
      offset: 220
    })
  },

  // 验证手机号
  phone (val) {
    return (/^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9])|(16[6]))\d{8}$/.test(val))
  },

  // 验证邮箱
  email (val) {
    return (/^([a-zA-Z0-9]+[-|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[-|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/.test(val))
  },

  // 生成随机字符串
  generateCode (len) {
    let arr = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9']
    let str = ''
    for (let i = 0; i < len; i++) {
      str += arr[Math.floor(Math.random() * arr.length)]
    }
    return str
  },

  // 初始化全局
  init () {
    localStorage.clear()
    store.commit('setOpenedTab', [])
  },

  // 下划线或者点号转驼峰
  toCamel (str) {
    // return str.replace(/([^_])(?:_+([^_]))/g, ($0, $1, $2) => {
    return str.replace(/([^_|.])(?:[_|.]+([^_|.]))/g, ($0, $1, $2) => {
      return $1 + $2.toUpperCase()
    })
  },

  // 根据菜单列表获取第一个可用的菜单
  getFirstItemByType (meuTree, type = 'M') {
    let firstMenu = getMeu(meuTree, type)
    return firstMenu
  },

  // 验证数字串
  isNumberStr (val) {
    return (/^[0-9]*$/).test(val)
  },

  // 验证金额，带小数点
  isPrice (val) {
    return (/((^[1-9]\d*)|^0)(\.\d){0,1}$/).test(val)
  }
}
