<template>
  <div class="user-profile">
    <div class="form-container">
      <div class="left-content-container">
        <div class="title">
          <!-- <el-avatar :src="userInfo.avatar" class="user-avatar"></el-avatar> -->
          <span>{{ userInfo.account }}</span>
        </div>
        <div class="content-container">
          <div class="content-item">
            <label>姓名：</label>
            <div>{{ userInfo.realname }}</div>
          </div>
          <div class="content-item">
            <label>角色：</label>
            <div>{{ userInfo.roles | strFmt('roleName') }}</div>
          </div>
          <div class="content-item">
            <label>部门：</label>
            <div>{{ userInfo.dept ? userInfo.dept.deptName : '' }}</div>
          </div>
          <div class="content-item">
            <label>手机：</label>
            <div>{{ userInfo.mobile }}</div>
          </div>
          <div class="content-item">
            <label>邮箱：</label>
            <div>{{ userInfo.email }}</div>
          </div>
          <div class="content-item">
            <label>性别：</label>
            <div>
              <easy4j-dict-label
                dict="sys_user.sex"
                :value="userInfo.sex"
              />
            </div>
          </div>
        </div>
      </div>
      <div class="right-content-container">
        <el-tabs v-model="activeName">
          <el-tab-pane label="个人信息" name="first">
            <el-form ref="editForm" :rules="rules" :model="editForm" label-width="80px">
              <el-form-item label="账号">
                <el-input v-model="editForm.account" disabled></el-input>
              </el-form-item>
              <el-form-item label="姓名">
                <el-input v-model="editForm.realname"></el-input>
              </el-form-item>
              <el-form-item label="手机" prop="mobile">
                <el-input v-model="editForm.mobile"></el-input>
              </el-form-item>
              <el-form-item label="邮箱" prop="email">
                <el-input v-model="editForm.email"></el-input>
              </el-form-item>
              <el-form-item label="性别">
                <el-radio-group v-model="editForm.sex">
                  <el-radio label="M">男</el-radio>
                  <el-radio label="F">女</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="">
                <el-button type="primary" @click="updateSelfInfo">更新个人信息</el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
  </div>
</template>

<script>
import {
  getSelfInfo,
  putSelfInfo,
} from '@/framework/api/common'

import Easy4jDictLabel from 'components/easy4j-dict-label'

export default {
  components: {
    Easy4jDictLabel,
  },
  data () {
    // 验证邮箱号
    let validateMail = (rule, value, callback) => {
      if (value === '') {
        callback()
      } else {
        if (!this.$utils.email(this.editForm.email)) {
          callback(new Error('请输入正确的邮箱格式'))
        }

        callback()
      }
    }

    // 验证手机号
    let validateMobile = (rule, value, callback) => {
      if (value === '') {
        callback()
      } else {
        if (!this.$utils.phone(this.editForm.mobile)) {
          callback(new Error('请输入正确的手机号格式'))
        }

        callback()
      }
    }
    return {
      userInfo: {
        avatar: '',
        account: '',
        realname: '',
        roles: '',
        depts: '',
        mobile: '',
        email: '',
        sex: '',
      },
      editForm: {
        account: '',
        realname: '',
        roles: '',
        depts: '',
        mobile: '',
        email: '',
        sex: 'M', // M-男, F-女
      },
      activeName: 'first',
      rules: {
        mobile: [
          { validator: validateMobile, trigger: 'blur' }
        ],
        email: [
          { validator: validateMail, trigger: 'blur' }
        ],
      },
    }
  },
  computed: {

  },
  filters: {
    strFmt (val, prop) {
      if (!val || !val.length) {
        return ''
      }

      return val.map(item => item[prop]).join()
    },
  },
  mounted () {
    this.getSelfInfo()
  },
  methods: {
    getSelfInfo () {
      const vm = this
      getSelfInfo().then(res => {
        // 初始化表单的值
        vm.userInfo = Object.assign({}, res.data)
        localStorage.setItem('userInfo', JSON.stringify(vm.userInfo))

        vm.editForm = Object.assign({}, vm.userInfo)
        vm.$forceUpdate()
      }).catch(error => {
        console.log(error)
      })
    },

    updateSelfInfo () {
      let roles = this.editForm.roles
      let roleIds = roles && roles.length ? roles.map(item => item.id).join() : ''

      let depts = this.editForm.depts
      let deptIds = depts && depts.length ? depts.map(item => item.id).join() : ''

      let params = {
        id: this.editForm.id,
        email: this.editForm.email,
        mobile: this.editForm.mobile,
        realname: this.editForm.realname,
        sex: this.editForm.sex,
        roleIds,
        deptIds,
      }

      const vm = this
      putSelfInfo(params).then(res => {
        vm.$utils.alert('success', '操作成功！')
        vm.getSelfInfo()
      }).catch(error => {
        console.log(error)
      })
    }
  },
}
</script>

<style scoped lang="scss">
  .user-profile {
    .form-container {
      display: flex;
      align-items: flex-start;
      .left-content-container {
        // min-width: 200px;
        width: auto;
        margin-right: 20px;
        .title {
          display: flex;
          flex-direction: column;
          justify-content: center;
          align-items: center;
          margin-bottom: 20px;
          .user-avatar {
            margin-bottom: 10px;
          }
        }
        .content-container {
          .content-item {
            display: flex;
            margin-bottom: 20px;
            word-break: keep-all;
          }
        }
      }
      .right-content-container {
        width: 100%;
        height: 100%;
        border-left: 1px solid #DCDFE6;
        padding-left: 20px;
      }
    }
  }
</style>
