const errors = [
  // 断网
  {
    path: '/network-error',
    name: 'NetworkError',
    component: require('@/framework/views/error-page/network-error.vue').default
  },
  // 错误页面
  {
    path: '/error-404',
    name: '404',
    component: resolve => require(['@/framework/views/error-page/404.vue'], resolve)
  },
  // 匹配不到的路径拦截，一定要放在最后
  {
    path: '*',
    redirect: {
      name: '404'
    }
  }
]

export default errors
