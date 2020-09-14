/*
 * @Descripttion:
 * @version:
 * @Author: Huang Lina
 * @Date: 2020-03-23 10:13:32
 * @LastEditors: Huang Lina
 * @LastEditTime: 2020-04-26 15:19:23
 */
import develope from './modules/develope-management'
import systemManagement from './modules/system-management'
import error from './modules/error'
import bizRouter from '@/biz/router'

const routes = [
  // {
  //   path: '/',
  //   name: 'Home',
  //   redirect: '/smsHome', // 暂时重定向
  //   component: resolve => require(['@framework/views/index.vue'], resolve),
  // },
  {
    path: '/login',
    name: 'Login',
    component: resolve => require(['@/framework/views/login/index.vue'], resolve)
  },
  {
    path: '/wechat-callback',
    name: 'WechatCallback',
    component: resolve => require(['@/framework/views/login/wechat-callback.vue'], resolve)
  },
  {
    path: '/test',
    name: 'Test',
    component: resolve => require(['@/framework/views/home/test.vue'], resolve)
  },
  {
    path: '/',
    name: '',
    component: resolve => require(['@/framework/views/home/index.vue'], resolve),
    children: [
      {
        path: '/user-profile',
        name: 'UserProfile',
        component: resolve => require(['@/framework/views/home/user-profile.vue'], resolve),
        meta: {
          module: 'USER-PROFILE',
          title: '个人中心',
          showOnTab: true
        }
      },
      ...systemManagement,
      ...develope,
      ...bizRouter
    ]
  },
  ...error
]

export default routes
