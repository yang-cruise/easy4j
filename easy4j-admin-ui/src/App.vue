<template>
  <div id="app">
    <router-view v-if="isRouterAlive"/>
  </div>
</template>

<script>
import { AppDeviceEnquire } from '@/framework/mixins/device.js'
export default {
  name: 'App',
  mixins: [ AppDeviceEnquire ],
  provide () {
    return {
      reload: this.reload
    }
  },
  data () {
    return {
      isRouterAlive: true,
    }
  },
  mounted () {
    window.addEventListener('offline', () => {
      // 网络由正常常到异常时触发
      sessionStorage.locationUrl = window.location.href
      this.$router.push({ name: 'NetworkError' })
    })
    window.addEventListener('online', () => {
      window.location.href = sessionStorage.locationUrl
    })
  },
  methods: {
    reload () {
      this.isRouterAlive = false
      this.$nextTick(() => {
        this.isRouterAlive = true
      })
    }
  }
}
</script>

<style>
  body, html {
    margin: 0;
    padding: 0;
    height: 100%;
    font-family: Helvetica Neue,Helvetica,PingFang SC,Hiragino Sans GB,Microsoft YaHei,SimSun,sans-serif;
    font-weight: 400;
    -webkit-font-smoothing: antialiased;
    -webkit-tap-highlight-color: transparent;
  }
</style>
