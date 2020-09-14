<template>
  <easy4j-basic-dialog
    v-if="dialogVisible"
    :dialogBasicVisible.sync="dialogVisible"
    :basicDialog="editDialog"
    @submit="onSubmit"
  >
    <el-form
      :model="editForm"
      :rules="rules"
      ref="editForm"
      label-width="100px"
    >
      <el-form-item label="账号" prop="account">
        <el-input
          v-model.trim="editForm.account"
          v-if="editDialog.type === 'add'"
          placeholder="请输入账号"
        ></el-input>
        <template v-else>
          {{ editForm.account }}
        </template>
      </el-form-item>
      <el-form-item label="初始密码" prop="password" v-if="editDialog.type === 'add'">
        <el-input v-model.trim="editForm.password" placeholder="请输入密码">
          <el-button slot="append" @click="getPassword">随机生成</el-button>
        </el-input>
      </el-form-item>
      <el-form-item label="真实姓名" prop="realname">
        <el-input v-model.trim="editForm.realname" placeholder="请输入真实姓名"></el-input>
      </el-form-item>
      <el-form-item label="性别" prop="sex">
        <el-radio-group v-model="editForm.sex">
          <el-radio label="M">男</el-radio>
          <el-radio label="F">女</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="手机" prop="mobile">
        <el-input v-model.trim="editForm.mobile" placeholder="请输入手机"></el-input>
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model.trim="editForm.email" placeholder="请输入邮箱"></el-input>
      </el-form-item>
      <el-form-item label="角色" prop="roleIds">
        <el-select
          v-model="editForm.roleIds"
          multiple
          placeholder="请选择角色，可多选"
          class="full-width-select"
        >
          <el-option
            v-for="item in roleOptions"
            :key="item.id"
            :label="item.roleName"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="部门" prop="deptId">
        <el-cascader
          v-model="editForm.deptId"
          :show-all-levels="false"
          :options="deptOptions"
          :props="deptProps"
          class="full-width-select"
          clearable></el-cascader>
      </el-form-item>
    </el-form>
  </easy4j-basic-dialog>
</template>

<script>
import {
  postSysUser,
  getSysRoles,
  putSysUser,
  getSysDeptsTree,
  getSysUserDetail
} from '@/framework/api/common'

import Easy4jBasicDialog from 'components/easy4j-basic-dialog'

export default {
  components: {
    Easy4jBasicDialog,
  },
  props: {
    dialogFormVisible: {
      type: Boolean,
      default: false,
    },
    editDialog: {
      type: Object,
      default: () => {
        return {
          type: 'add',
          title: '新增用户',
        }
      },
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
      dialogVisible: false,
      editForm: {
        avatar: '',
        account: '',
        realname: '',
        email: '',
        mobile: '',
        status: '', // 1-正常, 2-冻结
        sex: 'M', // M-男, F-女
        roleIds: [],
        roles: [],
        password: '',
        deptId: '',
      },
      rules: {
        account: [
          { required: true, message: '请输入账号', trigger: 'blur' }
        ],
        password: [
          { validator: validatePass, required: true, trigger: 'blur' }
        ],
        mobile: [
          { validator: validateMobile, trigger: 'blur' }
        ],
        email: [
          { validator: validateMail, trigger: 'blur' }
        ],
        roleIds: [
          { required: true, message: '请选择角色', trigger: 'change' }
        ],
        deptId: [
          { required: true, message: '请选择部门', trigger: 'change' }
        ],
      },
      roleOptions: [],
      deptOptions: [],
      deptProps: {
        checkStrictly: true, // 选择任一级
        emitPath: false, // 只返回该节点的值，不返回路径
        value: 'id',
        label: 'deptName',
        children: 'subSysDeptList',
      },
      addFlag: false,
    }
  },
  watch: {
    // 父 => 子
    dialogFormVisible: {
      immediate: true,
      handler () {
        this.dialogVisible = this.dialogFormVisible
      },
    },

    // 子 => 父
    dialogVisible: {
      immediate: true,
      handler () {
        this.$emit('update:dialogFormVisible', this.dialogVisible)
      }
    },

    parentRowInfo: {
      deep: true,
      immediate: true,
      handler () {
        if (!this.parentRowInfo.id) {
          return
        }

        if (this.editDialog.type && this.editDialog.type === 'edit') {
          this.getRowDetail()
        }
      },
    },
  },
  computed: {

  },
  mounted () {
    this.getSysRoles()
    this.getSysDeptsTree()
  },
  methods: {
    getRowDetail () {
      const vm = this
      getSysUserDetail({ id: this.parentRowInfo.id }).then(res => {
        // 初始化表单的值
        vm.editForm = Object.assign({}, res.data)

        // 强制
        let roleIds = vm.editForm.roles.map(item => item.id)
        vm.$set(vm.editForm, 'roleIds', roleIds)

        vm.$set(vm.editForm, 'deptId', vm.editForm.dept ? vm.editForm.dept.id : '')
      }).catch(error => {
        console.log(error)
      })
    },

    // 获取所有角色列表
    getSysRoles () {
      const vm = this
      getSysRoles().then(res => {
        vm.roleOptions = [...res.data]
      }).catch(error => {
        console.log(error)
      })
    },

    // 获取所有部门列表
    getSysDeptsTree () {
      const vm = this
      getSysDeptsTree().then(res => {
        vm.deptOptions = [...res.data]

        // 极联组件，叶子节点要删除代表子节点的字段
        vm.deptOptions = vm.deptListFmt(vm.deptOptions)
      }).catch(error => {
        console.log(error)
      })
    },

    // 删除无效字段
    deptListFmt (deptOptions) {
      for (let item of deptOptions) {
        if (item.subSysDeptList && item.subSysDeptList.length) {
          this.deptListFmt(item.subSysDeptList)
        }
      }
      return deptOptions
    },

    // 生成随机密码
    getPassword () {
      let password = this.$utils.generateCode(6)
      this.editForm.password = password
    },

    onSubmit (type) {
      if (type === 'cancel') {
        this.dialogVisible = false
        return
      }

      this.submitForm('editForm')
    },

    // 提交表单
    submitForm (formName) {
      const vm = this
      this.$refs[formName].validate((valid) => {
        if (!valid) {
          return false
        }

        // 添加成功后重置表单的值
        if (vm.editDialog.type === 'add') {
          vm.addTableRow()
        } else {
          vm.editTableRow()
        }
      })
    },

    // 新增
    addTableRow () {
      if (this.addFlag) {
        this.$utils.alert('error', '请勿重复提交！')
        return
      }

      let params = {
        account: this.editForm.account,
        email: this.editForm.email,
        mobile: this.editForm.mobile,
        realname: this.editForm.realname,
        roleIds: this.editForm.roleIds,
        sex: this.editForm.sex,
        password: this.editForm.password,
        deptId: this.editForm.deptId,
      }

      const vm = this
      vm.addFlag = true
      postSysUser(params).then(res => {
        vm.addFlag = false
        vm.$utils.alert('success', '操作成功！')
        vm.dialogVisible = false
        vm.$emit('reloadTable')
      }).catch(error => {
        vm.$utils.alert('error', error.msg)
        vm.addFlag = false
      })
    },

    // 修改信息
    editTableRow () {
      let params = {
        id: this.editForm.id,
        email: this.editForm.email,
        mobile: this.editForm.mobile,
        realname: this.editForm.realname,
        roleIds: this.editForm.roleIds,
        sex: this.editForm.sex,
        password: this.editForm.password,
        deptId: this.editForm.deptId,
      }

      const vm = this
      putSysUser(params).then(res => {
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
