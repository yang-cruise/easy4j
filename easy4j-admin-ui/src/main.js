// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import Vuex from 'vuex'
// 时间格式化
import moment from 'moment'
// elementu-i
import ElementUI from 'element-ui'
// import 'element-ui/lib/theme-chalk/index.css'
import '../theme/index.css'

// 框架文件
import router from '@/framework/router/index'
import store from '@/framework/store/index'

// 框架工具函数
import Utils from '@/framework/utils/index'
import getPermission from '@/framework/utils/permission'
import eventBus from '@/framework/utils/eventBus'

// 项目样式
import '../static/scss/index.scss'

Vue.config.productionTip = false

// 全局过滤器  时间过滤
Vue.filter('dateFmt', (input, formatString = 'YYYY-MM-DD') => {
  return moment(input).format(formatString)
})

Vue.prototype.$utils = Utils
Vue.prototype.$getPermission = getPermission
Vue.prototype.$eventBus = eventBus
Vue.prototype.$moment = moment

Vue.use(Vuex)
Vue.use(ElementUI)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})
