import Vue from 'vue'
import axios from 'axios'
import router from '../../router/index'
import {
  Message
} from 'element-ui'

// create an axios instance
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  timeout: 600000 // request timeout
})

// request interceptor
service.interceptors.request.use(
  config => {
    // do something before request is sent
    config.url = `/tencent/api${ config.url }`

    let auth = localStorage.getItem('Authorization')
    if (auth) {
      config.headers['Authorization'] = `Bearer ${auth}`
    }
    return config
  },
  error => {
    // do something with request error
    return Promise.reject(error)
  }
)

// response interceptor
service.interceptors.response.use(
  response => {
    const res = response.data

    if (res.code === 200) {
      return res
    } else {
      Message({
        message: res.msg,
        type: 'error',
        duration: 3 * 1000
      })

      if (res.code === 401) {
        Vue.prototype.$utils.init()

        setTimeout(() => {
          router.push({
            path: '/login'
          })
        }, 1000)
      } else {
        //
      }
      return Promise.reject(new Error(res.msg || 'Error'))
    }
  },
  error => {
    return Promise.reject(error)
  }
)

export default service
