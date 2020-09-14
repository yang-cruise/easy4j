import Vue from 'vue'
import Vuex from 'vuex'
import device from './modules/device.js'
import frameworkStores from './modules/stores'
import bizStores from '@biz/store/index'

Vue.use(Vuex)

const state = {}

const getters = {}

const mutations = {}

const actions = {}

const store = new Vuex.Store({
  state,
  getters,
  mutations,
  actions,
  modules: {
    device,
    frameworkStores,
    bizStores,
  },
  strict: true
})

export default store
