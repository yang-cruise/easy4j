/*
 * @Descripttion:
 * @version:
 * @Author: Huang Lina
 * @Date: 2020-03-10 11:14:30
 * @LastEditors: Huang Lina
 * @LastEditTime: 2020-04-26 18:05:33
 */
const routes = [
  {
    path: '/orders/list',
    name: 'Hello',
    component: resolve => require(['@biz/views/index.vue'], resolve),
  },
  // {
  //   path: '/',
  //   name: 'Home',
  //   redirect: '/hello', // 暂时重定向
  // },
]

export default routes
