
// test
import http from './request/http'
const post = http.post
const get = http.get
const upload = http.upload
const customize = http.customize

// 常规post
export const testPost = (params) => post('/test', params)

// 常规get
export const testGet = (params) => get('/test', params)

// 常规upload
export const testUpload = (params) => upload('/test', params)

// 自定义异常处理
export const testPost1 = (params) => post('/test', params, { catch: '自定义异常处理' })

// 自定义请求
export const testCustomize = (params) => customize('/test', params, obj)