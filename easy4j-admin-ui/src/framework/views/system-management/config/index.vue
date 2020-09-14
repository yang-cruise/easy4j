<template>
  <div class="role-management">
    <el-tabs v-model="activeName">
      <el-tab-pane label="网站配置" name="site">
        <el-form ref="siteForm" :rules="siteRules" :model="siteForm" label-width="160px">
          <el-form-item label="网站名称" prop="siteName">
            <el-input v-model="siteForm.siteName" placeholder="例：Easy4j"></el-input>
          </el-form-item>
          <el-form-item label="网站图标" prop="logo">
            <el-input v-model="siteForm.logo" placeholder="例：https://axure-file.lanhuhu.com/d758d9c6-5f1c-41a4-9f4a-260a5ac76df9__9984d309f589143b0f6a737b3f1b5986.svg"></el-input>
          </el-form-item>
          <el-form-item label="网站描述" prop="slogan">
            <el-input v-model="siteForm.slogan" placeholder="例：简单，美。"></el-input>
          </el-form-item>
          <el-form-item label="网站备案号" prop="beian">
            <el-input v-model="siteForm.beian" placeholder="例：粤ICP备12345678号-1"></el-input>
          </el-form-item>
          <el-form-item label="网站版权信息" prop="copyright">
            <el-input v-model="siteForm.copyright" placeholder="例：© 版权所有 2006-2020 深圳硕软技术有限公司"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSubmitSiteConfig">保存</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>

      <el-tab-pane label="登录配置" name="login" style="padding-top: 10px">
        <el-form ref="loginForm" :model="loginForm" label-width="100px" :inline="true">
          <div v-for="item in loginForm.config" :key="item.type">
            <el-form-item :label="item.name">
              <el-switch v-model="item.enable"></el-switch>
            </el-form-item>
            <el-form-item label="排序">
              <el-input-number v-model="item.sort" :min="1" :max="3" style="width: 120px" :disabled="!item.enable"></el-input-number>
            </el-form-item>
            <span v-if="item.type === 'account'">
              <el-form-item label="登录失败">
                <el-input-number v-model="item.params.showCaptchaCount" :min="0" :max="10" style="width: 120px" :disabled="!item.enable"></el-input-number> 次出现验证码
              </el-form-item>
              <el-form-item label="密码错误">
                <el-input-number v-model="item.params.lockAccountCount" :min="3" :max="10" style="width: 120px" :disabled="!item.enable"></el-input-number> 次锁定账户
              </el-form-item>
            </span>
            <span v-if="item.type === 'sms'">
              <el-form-item label="appKey" :rules="item.enable ? smsRules.appKey : []">
                <el-input v-model="item.params.appKey" :disabled="!item.enable" placeholder="例：12345678"></el-input>
              </el-form-item>
              <el-form-item label="appSecret" :rules="item.enable ? smsRules.appSecret : []">
                <el-input v-model="item.params.appSecret" :disabled="!item.enable" placeholder="例：abcdefgh9f412345678e8754fa123456"></el-input>
              </el-form-item>
            </span>
            <span v-if="item.type === 'wechat'">
              <el-form-item label="appId" :rules="item.enable ? wechatRules.appId : []">
                <el-input v-model="item.params.appId" :disabled="!item.enable" placeholder="例：wx59b3b53d6a123456"></el-input>
              </el-form-item>
              <el-form-item label="appSecret" :rules="item.enable ? wechatRules.appSecret : []">
                <el-input v-model="item.params.appSecret" :disabled="!item.enable" placeholder="例：abcdefgh9f412345678e8754fa123456"></el-input>
              </el-form-item>
              <el-form-item label="redirectUri" :rules="item.enable ? wechatRules.redirectUri : []">
                <el-input v-model="item.params.redirectUri" :disabled="!item. enable" placeholder="例：http://baidu.com/callback"></el-input>
              </el-form-item>
            </span>
            <el-divider></el-divider>
          </div>

          <el-form-item label=" ">
            <el-button type="primary" @click="onSubmitLoginConfig">保存</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import {
  getSysConfigDetail,
  putSysConfig
} from '@/framework/api/common'

export default {
  components: {
  },
  props: {
  },
  data () {
    return {
      activeName: 'site',
      siteForm: {
        siteName: '',
        logo: '',
        beian: '',
        copyright: '',
        slogan: ''
      },
      siteRules: {
        siteName: [
          { required: true, message: '请输入网站名称', trigger: 'blur' }
        ],
        logo: [
          { required: true, message: '请上传网站图标', trigger: 'blur' }
        ],
        copyright: [
          { required: true, message: '请输入网站版权信息', trigger: 'blur' }
        ]
      },
      loginForm: {
        config: [
          {
            type: "account",
            name: '密码登录',
            sort: 1,
            enable: true,
            params: {
              showCaptchaCount: 3,
              lockAccountCount: 5
            }
          },
          {
            type: "sms",
            name: '短信登录',
            sort: 2,
            enable: false,
            params: {
              appKey: "",
              appSecret: ""
            }
          },
          {
            type: "wechat",
            name: '微信登录',
            sort: 3,
            enable: false,
            params: {
              appId: "",
              appSecret: "",
              redirectUri: ""
            }
          }
        ]
      },
      smsRules: {
        appKey: [
          { required: true, message: '请输入appKey', trigger: 'blur' }
        ],
        appSecret: [
          { required: true, message: '请输入appSecret', trigger: 'blur' }
        ]
      },
      wechatRules: {
        appId: [
          { required: true, message: '请输入appId', trigger: 'blur' }
        ],
        appSecret: [
          { required: true, message: '请输入appSecret', trigger: 'blur' }
        ],
        redirectUri: [
          { required: true, message: '请输入redirectUri', trigger: 'blur' }
        ]
      }
    }
  },
  watch: {

  },
  computed: {

  },
  mounted () {
    this.getSiteConfig()
    this.getLoginConfig()
  },
  methods: {
    getSiteConfig () {
      const vm = this
      getSysConfigDetail({ configKey: 'SITE_CONFIG' }).then(res => {
        vm.siteForm = res.data
      }).catch(error => {
        console.log(error)
      })
    },
    getLoginConfig () {
      const vm = this
      getSysConfigDetail({ configKey: 'LOGIN_CONFIG' }).then(res => {
        vm.loginForm.config = res.data
      }).catch(error => {
        console.log(error)
      })
    },
    onSubmitSiteConfig () {
      const vm = this
      const params = {
        configKey: 'SITE_CONFIG',
        configContent: JSON.stringify(vm.siteForm)
      }
      putSysConfig( params ).then(res => {
        vm.$utils.alert('success', '保存成功！')
        localStorage.setItem('siteConfig', JSON.stringify(vm.siteForm))
        this.$eventBus.$emit('reloadSiteConfig')
      })
    },
    onSubmitLoginConfig () {
      const vm = this
      const params = {
        configKey: 'LOGIN_CONFIG',
        configContent: JSON.stringify(vm.loginForm.config)
      }
      putSysConfig( params ).then(res => {
        vm.$utils.alert('success', '保存成功！')
      })
    }
  }
}
</script>

<style scoped>

</style>
