<template>
  <easy4j-basic-dialog
    v-if="dialogVisible"
    :dialogBasicVisible.sync="dialogVisible"
    :basicDialog="deptDialog"
    @submit="onSubmit"
  >
    <div>
      <el-form
        ref="editForm"
        :model="editForm"
        :inline="true"
        :rules="rules"
        label-width="80px"
      >
        <el-form-item label="部门名称" prop="deptName" v-if="deptDialog.type !== 'delete'">
          <el-input
            v-model.trim="editForm.deptName"
            placeholder="10个字以内"
          >
          </el-input>
        </el-form-item>
        <el-form-item label="" prop="" v-else>
          是否确认删除“{{ parentRowInfo.deptName }}”？
        </el-form-item>
        <!-- <el-form-item label="">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitForm('editForm')">确 定</el-button>
        </el-form-item> -->
      </el-form>
    </div>
  </easy4j-basic-dialog>
</template>

<script>
import {
  postSysDept,
  putSysDept,
  deleteSysDept,
} from '@/framework/api/common'

import Easy4jBasicDialog from 'components/easy4j-basic-dialog'

export default {
  components: {
    Easy4jBasicDialog,
  },
  props: {
    deptDialog: {
      type: Object,
      default: () => {},
    },
    dialogDeptVisible: {
      type: Boolean,
      default: false,
    },
    parentRowInfo: {
      type: Object,
      default: () => {},
    },
  },
  data () {
    return {
      dialogVisible: false,
      editForm: {
        deptName: '',
      },
      rules: {
        deptName: [
          { required: true, message: '请输入部门名称', trigger: 'blur' }
        ],
      },
      addFlag: false,
    }
  },
  watch: {
    // 父 => 子
    dialogDeptVisible: {
      immediate: true,
      handler () {
        this.dialogVisible = this.dialogDeptVisible
      },
    },

    // 子 => 父
    dialogVisible: {
      immediate: true,
      handler () {
        this.$emit('update:dialogDeptVisible', this.dialogVisible)
      }
    },

    deptDialog: {
      deep: true,
      immediate: true,
      handler () {
        if (this.deptDialog.type && this.deptDialog.type === 'edit') {
          this.editForm = Object.assign({}, this.parentRowInfo)
        }
      },
    },

    editForm: {
      deep: true,
      immediate: true,
      handler () {
        if (this.editForm.deptName) {
          let deptName = this.editForm.deptName
          if (deptName.length > 10) {
            this.$set(this.editForm, 'deptName', deptName.slice(0, 10))
          }
        }
      },
    },
  },
  computed: {

  },
  mounted () {

  },
  methods: {
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
        if (vm.deptDialog.type === 'add') {
          vm.addTableRow()
        } else if (vm.deptDialog.type === 'edit') {
          vm.editTableRow()
        } else {
          vm.deleteTableRow()
        }
      })
    },

    // 新增
    addTableRow () {
      if (this.addFlag) {
        this.$utils.alert('error', '请勿重复提交！')
        return
      }

      let sort = this.parentRowInfo.subSysDeptList ? this.parentRowInfo.subSysDeptList.length : 0

      let params = {
        parentId: this.parentRowInfo.id,
        deptName: this.editForm.deptName,
        sort,
      }

      const vm = this
      vm.addFlag = true
      postSysDept(params).then(res => {
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
        parentId: this.editForm.parentId,
        deptName: this.editForm.deptName,
        sort: this.editForm.sort,
      }

      const vm = this
      putSysDept(params).then(res => {
        vm.$utils.alert('success', '操作成功！')
        vm.dialogVisible = false
        vm.$emit('reloadTable')
      }).catch(error => {
        console.log(error)
      })
    },

    // 删除
    deleteTableRow () {
      const vm = this
      deleteSysDept({ ids: this.parentRowInfo.id }).then(res => {
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
