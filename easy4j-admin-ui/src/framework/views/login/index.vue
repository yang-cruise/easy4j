<template>
  <div class="easy4j-login-container">
    <div class="easy4j-login">
      <div class="easy4j-login-title">
        <p class="easy4j-login-logo">
          <img :src="siteConfig.logo" v-if="siteConfig.logo"/>
          <i :class="defaultSiteConfig.logo" v-else></i>
          {{ siteConfig.siteName}}
        </p>
        <p class="easy4j-login-des">
          {{ siteConfig.slogan}}
        </p>
      </div>
      <el-card>
        <el-tabs v-model="activeName" @tab-click="onTabClick">
            <el-tab-pane :label="item.name" :name="item.type" v-for="item in loginConfig" :key="item.type" v-if="item.enable === true">
              <div v-if="item.type === 'account'">
                <div style="padding-bottom: 10px">
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
                      class="width-fill"
                      :loading="loginLoading"
                    >登录</el-button>
                  </el-form-item>
                  <!-- <div style="text-align: right;">
                    <el-link type="info">忘记密码</el-link>
                  </div> -->
                </el-form>
              </div>
              <div v-if="item.type === 'sms'">
                <el-form :model="smsForm" :rules="smsRules" ref="smsForm" label-width="0">
                  <el-form-item label="" prop="mobile">
                    <el-input
                      v-model.trim="smsForm.mobile"
                      @keyup.enter.native="submitForm('smsForm')"
                      placeholder="请输入手机号"
                    >
                      <i slot="prefix" class="el-input__icon el-icon-mobile-phone"></i>
                    </el-input>
                  </el-form-item>
                  <el-form-item label="" prop="captcha">
                    <el-input
                      v-model.trim="smsForm.captcha"
                      @keyup.enter.native="submitForm('smsForm')"
                      placeholder="请输入验证码"
                    >
                      <i slot="prefix" class="el-input__icon el-icon-key"></i>
                      <el-button slot="append">获取验证码</el-button>
                    </el-input>
                  </el-form-item>
                  <el-form-item>
                    <el-button
                      type="primary"
                      @click="submitForm('smsForm')"
                      class="width-fill"
                      :loading="loginLoading"
                    >登录</el-button>
                  </el-form-item>
                </el-form>
              </div>
              <div v-if="item.type === 'wechat'">
                <div id="wechat-container" style="text-align: center"></div>
              </div>
            </el-tab-pane>
        </el-tabs>
      </el-card>
    </div>
    <div class="easy4j-login-footer">
      <p>{{ siteConfig.copyright }}</p>
      <el-link type="info" href="http://www.beian.miit.gov.cn/" target="_blank">{{ siteConfig.beian }}</el-link>
    </div>

    <!-- 修改密码 -->
    <update-password
      v-if="dialogUpdateVisible"
      :dialogUpdateVisible.sync="dialogUpdateVisible"
    />
  </div>
</template>

<script>
import { mapGetters, mapMutations } from 'vuex'

import {
  getTokenByAccount,
  getCaptcha,
  getSelfInfo,
  getSiteConfig,
  getLoginConfig,
  getWechatConfig
} from '@/framework/api/common'

import UpdatePassword from './dialog/update-password'

export default {
  components: {
    UpdatePassword
  },
  data () {
    return {
      showErrorMsg: false,
      errorMsg: true,
      showCaptcha: false,
      captchaImageSrc: '',
      defaultSiteConfig: {
        logo: 'el-icon-s-platform',
        siteName: 'Easy4j',
        slogan: '简单，美。',
        beian: '粤ICP备12345678号-1',
        copyright: '© 版权所有 2006-2020 深圳硕软技术有限公司',
      },
      defaultLoginConfig: [{
        name: "密码登录",
        sort: 1,
        type: "account",
        enable: true,
        params: {lockAccountCount: 5, showCaptchaCount: 3}
      }],
      siteConfig: {
        logo: '',
        siteName: '',
        slogan: '',
        beian: '',
        copyright: '',
      },
      loginConfig: [],
      accountForm: {
        account: '',
        password: '',
        captcha: '',
        uuid: ''
      },
      accountRules: {
        account: [
          { required: true, message: '请输入账号', trigger: 'blur' },
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
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
      },
      smsForm: {
        mobile: '',
        captcha: ''
      },
      smsRules: {
        mobile: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
        ],
        captcha: [
          { required: true, message: '请输入验证码', trigger: 'blur' },
        ]
      },
      activeName: '',
      dialogUpdateVisible: false,
    }
  },
  computed: {
    ...mapGetters(['loginLoading'])
  },
  mounted () {
    this.siteConfig = Object.assign({}, this.defaultSiteConfig)
    this.getSiteConfig()
    this.getLoginConfig()

    // 获取登录状态
    if (localStorage.getItem('rememberPsw') === 'true') {
      this.getCookie()
    }
  },
  methods: {
    ...mapMutations(['setLoginLoading']),

    onTabClick (e) {
      if (e.name === 'account') {
        this.getCaptcha()
      }
      if (e.name === 'wechat') {
        this.buildWechatQR()
      }
    },

    // 获取项目信息
    getSiteConfig () {
      const vm = this
      getSiteConfig().then(res => {
        vm.siteConfig = res.data ? res.data : this.defaultSiteConfig
        localStorage.setItem('siteConfig', JSON.stringify(vm.siteConfig))

        // 初始化浏览器展示信息
        document.title = vm.siteConfig.siteName
        document.getElementById('indexIcon').href = vm.siteConfig.logo
      }).catch(error => {
        console.log(error)
      })
    },

    // 获取登录配置
    getLoginConfig () {
      const vm = this
      getLoginConfig().then(res => {
        vm.loginConfig = res.data ? res.data : this.defaultLoginConfig
        for (let i=0; i<vm.loginConfig.length; i++) {
          const config = vm.loginConfig[i]
          if (config.enable) {
            vm.activeName = config.type
            if (config.type === 'account') {
              vm.getCaptcha()
            }
            if (config.type === 'wechat') {
              vm.buildWechatQR()
            }
            break
          }
        }
      })
    },

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
      this.$refs[formName][0].validate((valid) => {
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

      getTokenByAccount(this.accountForm).then(res => {
        localStorage.setItem('Authorization', res.data)
        vm.getSelfInfo()
      }).catch(error => {
        vm.errorMsg = error.msg
        vm.showErrorMsg = true
        vm.setLoginLoading(false)
        vm.getCaptcha()
      })
    },

    // 获取用户信息
    getSelfInfo () {
      const vm = this
      getSelfInfo().then(res => {
        let userInfo = Object.assign({}, res.data)
        localStorage.setItem('userInfo', JSON.stringify(userInfo))
        if (userInfo.modifyPassword === 0) {
          vm.$confirm('您已经使用初始密码登录成功，为保证您的账户安全，建议您立即修改密码。', '提示', {
            confirmButtonText: '立即修改',
            cancelButtonText: '暂不修改',
            closeOnClickModal: false,
            closeOnPressEscape: false,
          }).then(() => {
            // 初始密码，未修改
            vm.dialogUpdateVisible = true
          }).catch(() => {
            vm.$store.dispatch('getMySysMenuTree', true).then(() => {
              vm.setLoginLoading(false)
            })
            vm.$store.dispatch('getMyPermissions')
          })
        } else {
          vm.$store.dispatch('getMySysMenuTree', true).then(() => {
            vm.setLoginLoading(false)
          })
          vm.$store.dispatch('getMyPermissions')
        }
      }).catch(error => {
        console.log(error)
      })
    },

    buildWechatQR () {

      getWechatConfig().then(res => {
        new WxLogin({
          self_redirect: true,
          id: 'wechat-container',
          appid: res.data.appId,
          scope: 'snsapi_login',
          redirect_uri: encodeURIComponent(res.data.redirectUri),
          state: 'null',
          href: encodeURIComponent(window.origin + '/css/wechat-style.css')
        })
      })
    } 
  },
}
</script>

<style scoped lang="scss">
  @media (min-width: 768px) {
    .easy4j-login-container {
      background-image:url(/img/body.svg);
      background-repeat: no-repeat;
      background-position: 50%;
      background-size: 100%
    }
  }
  .easy4j-login-container {
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: space-between;
    flex-direction: column;
    align-items: center;
    padding: 50px 0 10px;
    box-sizing: border-box;
  }

  .easy4j-login {
    width: 384px;
    max-width: 95%;
  }

  .easy4j-login-title {
    display: flex;
    flex-direction: column;
    align-items: center;
  }

  .easy4j-login-logo {
    display: flex;
    align-items: center;
    font-weight: 600;
    font-size: 30px;
    img {
      min-width: 40px;
      max-width: 60px;
      min-height: 40px;
      max-height: 60px;
      margin-right: 10px;
    }
    i {
      margin-right: 10px;
    }
  }

  .easy4j-login-des {
    font-size: 14px;
    margin: 20px 0;
    color: #808695;
  }

  .width-fill {
    width: 100%;
  }

  .easy4j-login-footer {
    padding: 10px;
    font-size: 14px;
    text-align: center;
  }

</style>
