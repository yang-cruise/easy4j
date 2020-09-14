import { deviceEnquire, DEVICE_TYPE } from '@/framework/utils/device.js'
import { mapState } from 'vuex'

// 设备区分
const mixinDevice = {
  computed: {
    ...mapState({
      device: state => state.device.device
    })
  },
  methods: {
    isMobile () {
      return this.device === DEVICE_TYPE.MOBILE
    },
    isDesktop () {
      return this.device === DEVICE_TYPE.DESKTOP
    },
    isTablet () {
      return this.device === DEVICE_TYPE.TABLET
    }
  }
}

const AppDeviceEnquire = {
  mounted () {
    const { $store } = this
    deviceEnquire(deviceType => {
      switch (deviceType) {
        case DEVICE_TYPE.DESKTOP:
          $store.commit('device/TOGGLE_DEVICE', 'desktop')
          break
        case DEVICE_TYPE.TABLET:
          $store.commit('device/TOGGLE_DEVICE', 'tablet')
          break
        case DEVICE_TYPE.MOBILE:
        default:
          $store.commit('device/TOGGLE_DEVICE', 'mobile')
          break
      }
    })
  }
}

export { AppDeviceEnquire, mixinDevice }
