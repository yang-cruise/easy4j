/**
 * Created by kenns on 2020/5/5.
 */
const device = {
  namespaced: true,
  state: {
    device: 'desktop',
  },
  mutations: {
    // 获取设备类型
    TOGGLE_DEVICE: (state, device) => {
      state.device = device
    }
  },
  actions: {}
}
export default device
