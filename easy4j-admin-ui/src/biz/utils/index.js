import Vue from 'vue'

export default {
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
    return (/^([a-zA-Z]|[0-9])(\w|\\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/.test(val))
  },

  // 生成随机字符串
  generateCode (len) {
    let arr = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9']
    let str = ''
    for (let i = 0; i < len; i++) {
      str += arr[Math.floor(Math.random() * arr.length)]
    }
    return str
  }
}
