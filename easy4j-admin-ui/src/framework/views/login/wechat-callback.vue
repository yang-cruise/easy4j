<template>
  <div v-if="pageLoading" :v-loading="pageLoading" style="height: 100%">
  </div>
  <div v-else>
    <div style="text-align: center; padding-bottom: 10px">
      <el-avatar fit="fill" :size="60" :src="userInfo.headimgurl"></el-avatar>
      <el-row style="padding-bottom: 5px">{{ userInfo.nickname }}</el-row>
      <el-alert v-if="showErrorMsg" :title="errorMsg" type="error" show-icon :closable="false"></el-alert>
    </div>
    <el-form :model="accountForm" :rules="accountRules" ref="accountForm" label-width="0">
      <el-form-item label="" prop="account">
        <el-input
          v-model.trim="accountForm.account"
          @keyup.enter.native="submitForm('accountForm')"
          placeholder="请输入账号"
        >
          <i slot="prefix" class="el-input__icon el-icon-user"></i>
        </el-input>
      </el-form-item>
      <el-form-item label="" prop="password">
        <el-input
          type="password"
          v-model.trim="accountForm.password"
          @keyup.enter.native="submitForm('accountForm')"
          placeholder="请输入密码"
        >
          <i slot="prefix" class="el-input__icon el-icon-lock"></i>
        </el-input>
      </el-form-item>
      <el-form-item label="" prop="captcha" v-if="showCaptcha">
        <el-input
          placeholder="请输入验证码" 
          v-model.trim="accountForm.captcha"
          @keyup.enter.native="submitForm('accountForm')"
        >
          <template solt="prefix">
            <i slot="prefix" class="el-input__icon el-icon-key"></i>
          </template>
          <template slot="append">
            <el-image :src="captchaImageSrc" fit="contain" style="width: 100px;" @click="getCaptcha"></el-image>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          @click="submitForm('accountForm')"
          style="width: 100%"
          :loading="loginLoading"
        >绑定并登录</el-button>
      </el-form-item>
    </el-form>
    <el-row style="text-align: center; font-size: 12px">
      <i class="el-icon-warning-outline"></i> 绑定账号后，下次可使用微信扫码登录
    </el-row>
  </div>
</template>

<script>
import { mapGetters, mapMutations } from 'vuex'
import {
  getTokenByWechat,
  getTokenByAccount,
  getCaptcha,
  getSelfInfo
} from '@/framework/api/common'

export default {
  components: {

  },
  props: {

  },
  data () {
    return {
      pageLoading: true,
      showErrorMsg: false,
      errorMsg: true,
      showCaptcha: false,
      captchaImageSrc: '',
      userInfo: {},
      accountForm: {
        account: '',
        password: '',
        captcha: '',
        uuid: '',
        openId: ''
      },
      accountRules: {
        account: [
          { required: true, message: '请输入账号', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ],
        captcha: [
          { trigger: 'blur', validator: (rule, value, callback) => {
            if (this.showCaptcha && value === '') {
              callback(new Error('请输入验证码'))
            } else {
              callback()
            }
          } }
        ]
      }
    }
  },
  computed: {
    ...mapGetters(['loginLoading'])
  },
  mounted () {
    this.getTokenByWechat()
  },
  watch: {

  },
  methods: {
    ...mapMutations(['setLoginLoading']),

    // 获取验证码
    getCaptcha () {
      const vm = this
      getCaptcha().then(res => {
        const data = res.data
        if (data.show) {
          vm.accountForm.uuid = data.uuid
          vm.captchaImageSrc = data.image
        }
        vm.showCaptcha = data.show
      })
    },

    submitForm (formName) {
      const vm = this
      vm.showErrorMsg = false
      this.$refs[formName].validate((valid) => {
        if (!valid) {
          return false
        }

        vm.login()
      })
    },

    login () {
      if (this.loginLoading) {
        return
      }

      const vm = this
      vm.setLoginLoading(true)

      getTokenByAccount(vm.accountForm).then(res => {
        vm.pageLoading = true
        localStorage.setItem('Authorization', res.data)
        vm.getSelfInfo()
      }).catch(error => {
        vm.errorMsg = error.msg
        vm.showErrorMsg = true
        vm.setLoginLoading(false)
        vm.getCaptcha()
      })
    },

    getSelfInfo () {
      const vm = this
      getSelfInfo().then(res => {
        let userInfo = Object.assign({}, res.data)
        localStorage.setItem('userInfo', JSON.stringify(userInfo))
        vm.$store.dispatch('getMySysMenuTree', true).then(() => {
          vm.setLoginLoading(false)
        })
        vm.$store.dispatch('getMyPermissions')
      }).catch(error => {
        console.log(error)
      })
    },

    getTokenByWechat () {
      const vm = this
      let code = window.location.hash.slice(1,).split('&')[0].split('=')[1]
      if (!code) {
        code = window.location.search.slice(1,).split('&')[0].split('=')[1]
      }
      if (!code) {
        code = window.location.href.slice(1,).split('&')[0].split('=')[1]
      }
      getTokenByWechat({ code }).then(res => {
        vm.pageLoading = true
        localStorage.setItem('Authorization', res.data)
        vm.getSelfInfo()
      }).catch(error => {
        vm.getCaptcha()
        vm.userInfo = error.data
        vm.accountForm.openId = error.data.openid
        vm.pageLoading = false
      })
    }
  },
}
</script>

<style scoped lang="scss">

</style>
