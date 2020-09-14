<template>
  <el-dialog
    title="修改密码"
    :visible.sync="dialogVisible"
    width="30%"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    :before-close="handleClose"
  >
    <div>
      <el-form
        :model="ruleForm"
        :rules="rules"
        ref="ruleForm"
        label-width="100"
        @submit.native.prevent
      >
        <el-form-item label="旧密码" prop="originPassword">
          <el-input
            type="password"
            v-model.trim="ruleForm.originPassword"
            @keyup.enter.native="submitForm('ruleForm')"
          >
          </el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input
            type="password"
            v-model.trim="ruleForm.newPassword"
            @keyup.enter.native="submitForm('ruleForm')"
          >
          </el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
            type="password"
            v-model.trim="ruleForm.confirmPassword"
            @keyup.enter.native="submitForm('ruleForm')"
          >
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            @click="submitForm('ruleForm')"
            class="width-fill"
          >提交</el-button>
          <el-button
            @click="cancelUpdate"
            class="width-fill"
          >取消</el-button>
        </el-form-item>
      </el-form>
    </div>
  </el-dialog>
</template>

<script>
import { mapMutations } from 'vuex'

import {
  putSelfPassword
} from '@/framework/api/common'

export default {
  components: {

  },
  props: {
    dialogUpdateVisible: {
      type: Boolean,
      default: false,
    },
  },
  data () {
    let validateOriginPass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入旧密码'))
      } else {
        if (value.length < 6 || value.length > 20) {
          callback(new Error('请输入6~20个字符'))
        }
        callback()
      }
    }
    let validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入新密码'))
      } else {
        if (value.length < 6 || value.length > 20) {
          callback(new Error('请输入6~20个字符'))
        }

        if (this.ruleForm.confirmPassword !== '') {
          this.$refs.ruleForm.validateField('confirmPassword')
        }
        callback()
      }
    }
    let validateRepeatPass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入新密码'))
      } else if (value !== this.ruleForm.newPassword) {
        callback(new Error('两次输入密码不一致!'))
      } else if (value.length < 6 || value.length > 20) {
        callback(new Error('请输入6~20个字符'))
      } else {
        callback()
      }
    }
    return {
      dialogVisible: false,
      ruleForm: {
        originPassword: '',
        newPassword: '',
        confirmPassword: '',
      },
      rules: {
        originPassword: [
          { validator: validateOriginPass, required: true, trigger: 'blur' },
        ],
        newPassword: [
          { validator: validatePass, required: true, trigger: 'blur' },
        ],
        confirmPassword: [
          { validator: validateRepeatPass, required: true, trigger: 'blur' },
        ],
      }
    }
  },
  watch: {
    // 父 => 子
    dialogUpdateVisible: {
      immediate: true,
      handler () {
        this.dialogVisible = this.dialogUpdateVisible
      },
    },

    // 子 => 父
    dialogVisible: {
      immediate: true,
      handler () {
        this.$emit('update:dialogUpdateVisible', this.dialogVisible)
      }
    }
  },
  computed: {

  },
  mounted () {

  },
  methods: {
    ...mapMutations(['setLoginLoading']),

    submitForm (formName) {
      const vm = this
      this.$refs[formName].validate((valid) => {
        if (!valid) {
          return false
        }

        vm.putSelfPassword()
      })
    },

    putSelfPassword () {
      const vm = this
      putSelfPassword(vm.ruleForm).then(res => {
        let routeName = vm.$route.name
        if (routeName === 'Login') {
          vm.$utils.alert('success', '操作成功！')
          vm.goSms()
        } else {
          vm.$utils.alert('success', '操作成功，请重新登录！')
          vm.$router.push({ name: 'Login' })
        }
      }).catch(error => {
        console.log(error)
      })
    },

    goSms () {
      this.$store.dispatch('getMySysMenuTree', true).then(() => {
        this.setLoginLoading(false)
      })
      this.$store.dispatch('getMyPermissions')
    },

    cancelUpdate () {
      this.dialogVisible = false

      let routeName = this.$route.name
      if (routeName === 'Login') {
        this.goSms()
      }
    },

    handleClose (done) {
      done()

      let routeName = this.$route.name
      if (routeName === 'Login') {
        this.goSms()
      }
    },
  },
}
</script>

<style scoped lang="scss">

</style>
