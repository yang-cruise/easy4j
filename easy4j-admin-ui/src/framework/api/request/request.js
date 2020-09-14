import axios from 'axios'
import Vue from 'vue'
import qs from 'qs'
import router from '../../router/index'

axios.defaults.baseURL = '/api'
axios.defaults.timeout = 1000 * 60 * 2

const request = (obj = {}) => {
  return new Promise((resolve, reject) => {
    let allObject = Object.assign({}, obj)

    // 处理头部
    let headers = Object.assign({}, allObject.headers)
    if (localStorage.getItem('Authorization')) {
      headers['Authorization'] = `Bearer ${localStorage.getItem('Authorization')}`
    }
    allObject.headers = headers

    // 删除不必要的自定义属性
    delete allObject.catch

    axios(allObject).then(function (res) {
      // debugger
      if (allObject.hasOwnProperty('responseType')) {
        resolve(res.data)
      } else if (res.data.code === 200) {
        resolve(res.data)
      } else {
        if ([401, 600, 700, 10005, 10010].includes(res.data.code)) {
          // 需要重新登录
          Vue.prototype.$message.error(res.data.msg)

          // 初始化状态
          Vue.prototype.$utils.init()

          // 跳转登录页
          setTimeout(() => {
            if (router.history.current.path !== '/login') {
              router.push({
                path: '/login',
                query: {
                  /// 登录后需重定向
                  redirect: router.history.current.path,
                }
              })
            }
          }, 1000)
        } else if (obj.catch) {
          // 自定义异常处理函数
          if (typeof obj.catch === 'function') {
            obj.catch()
          } else {
            reject(res.data)
          }

          // 不在调用接口时处理异常就不抛出异常
          // reject(res.data)
        } else {
          Vue.prototype.$message.error(res.data.msg)
        }
      }
    }).catch(function (error) {
      console.log(error)
      Vue.prototype.$message.error('网络异常')
    })
  })
}

const http = {
  get: (url, params, obj) => {
    let ori = {
      url,
      method: 'GET',
      params,
    }
    return request(Object.assign(ori, obj))
  },
  post: (url, params, obj) => {
    let ori = {
      url,
      method: 'POST',
      data: params,
    }
    return request(Object.assign(ori, obj))
  },
  put: (url, params, obj) => {
    let ori = {
      url,
      method: 'PUT',
      data: params,
    }
    return request(Object.assign(ori, obj))
  },
  delete: (url, params, obj) => {
    let ori = {
      url,
      method: 'DELETE',
      params,
    }
    return request(Object.assign(ori, obj))
  },
  upload: (url, params, obj) => {
    let ori = {
      url,
      method: 'POST',
      data: params,
      headers: {
        'Content-Type': 'multipart/form-data'
      },
    }
    return request(Object.assign(ori, obj))
  },

  // 自定义请求
  customize: (url, params, obj) => {
    let ori = {
      url,
      data: params,
    }
    return request(Object.assign(ori, obj))
  }
}

export default http
