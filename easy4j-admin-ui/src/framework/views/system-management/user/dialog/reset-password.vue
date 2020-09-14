<template>
  <easy4j-basic-dialog
    v-if="dialogVisible"
    :dialogBasicVisible.sync="dialogVisible"
    :basicDialog="basicDialog"
    @submit="onSubmit"
  >
    <div>
      <el-form
        ref="resetPassform"
        :model="resetPassform"
        :inline="true"
        :rules="rules"
        label-width="80px"
        @submit.native.prevent
      >
        <el-form-item label="新密码" prop="newPassword">
          <el-input
            v-model.trim="resetPassform.newPassword"
            placeholder="请输入新密码"
          >
            <el-button slot="append" @click="getPassword">随机生成</el-button>
          </el-input>
        </el-form-item>
      </el-form>
    </div>
  </easy4j-basic-dialog>
</template>

<script>
import {
  putSysUserPassword,
} from '@framework/api/common'

import Easy4jBasicDialog from 'components/easy4j-basic-dialog'

export default {
  components: {
    Easy4jBasicDialog,
  },
  props: {
    dialogResetVisible: {
      type: Boolean,
      default: false,
    },
    parentRowInfo: {
      type: Object,
      default: () => {},
    },
  },
  data () {
    // 验证密码
    let validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else {
        if (value.length < 6 || value.length > 20) {
          callback(new Error('请输入6~20个字符'))
        }

        callback()
      }
    }
    return {
      dialogVisible: false,
      resetPassform: {
        newPassword: '',
      },
      rules: {
        newPassword: [
          { validator: validatePass, required: true, trigger: 'blur' }
        ],
      },
      basicDialog: {
        title: '重置密码',
      }
    }
  },
  watch: {
    // 父 => 子
    dialogResetVisible: {
      immediate: true,
      handler () {
        this.dialogVisible = this.dialogResetVisible
      },
    },

    // 子 => 父
    dialogVisible: {
      immediate: true,
      handler () {
        this.$emit('update:dialogResetVisible', this.dialogVisible)

        // 清空重置密码表单
        if (!this.dialogVisible && this.$refs.resetPassform) {
          this.$refs.resetPassform.resetFields()
        }
      }
    },
  },
  computed: {

  },
  mounted () {

  },
  methods: {
    // 生成随机密码
    getPassword () {
      let password = this.$utils.generateCode(6)
      this.resetPassform.newPassword = password
    },

    onSubmit (type) {
      if (type === 'cancel') {
        this.dialogVisible = false
        return
      }

      this.submitResetPass()
    },

    // 提交重置密码
    submitResetPass () {
      const vm = this
      this.$refs['resetPassform'].validate((valid) => {
        if (!valid) {
          return false
        }

        vm.resetPassword()
      })
    },

    // 重置密码
    resetPassword () {
      let params = {
        userId: this.parentRowInfo.id,
        newPassword: this.resetPassform.newPassword,
      }
      const vm = this
      putSysUserPassword(params).then(res => {
        vm.$utils.alert('success', '操作成功！')
        vm.dialogVisible = false
        vm.$emit('reloadTable')
      }).catch(error => {
        console.log(error)
      })
    },
  },
}
</script>

<style scoped lang="scss">

</style>
