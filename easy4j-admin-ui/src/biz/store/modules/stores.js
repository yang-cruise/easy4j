/*
 * @Descripttion:
 * @version:
 * @Author: Huang Lina
 * @Date: 2020-03-10 11:14:30
 * @LastEditors: Huang Lina
 * @LastEditTime: 2020-04-26 18:09:39
 */
const state = {
  test: [],
  // 预留字段，配置是否展示首页
  hasHome: false,
}

const getters = {
  test: test => state.test,
  hasHome: hasHome => state.hasHome,
}

const mutations = {
  setTest (state, test) {
    state.test = test
  },
}

const actions = {}

export default {
  state,
  getters,
  mutations,
  actions
}
