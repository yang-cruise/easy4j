<template>
  <div class="role-management">
    <!-- 查询表单 -->
    <easy4j-query-form
      v-if="$getPermission('sys:dict:select') && Object.keys(this.formConfig).length"
      :formConfig="formConfig"
      @queryTable="onQueryTable"
    />

    <!-- 操作按钮 -->
    <div class="options-container">
      <easy4j-permission-button
        permission="sys:dict:insert"
        text="新增字典"
        icon="el-icon-plus"
        @click="openAddDialog"
      />
    </div>

    <!-- 表格 -->
    <easy4j-basic-table
      :tableConfig="tableConfig"
      @editRow="onEditRow"
    >
      <!-- 字典名称 -->
      <template slot='name' slot-scope="scope">
        <el-button type="text" @click="showDetail(scope.row)">
          {{ scope.row.name }}
        </el-button>
      </template>
    </easy4j-basic-table>

    <!-- 分页 -->
    <div class="page-container">
      <!-- 占位符，使得分页在右边，不要删 -->
      <div></div>

      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageConfig.current"
        :page-sizes="[1, 2, 5, 10, 50, 100]"
        :page-size="pageConfig.size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pageConfig.total">
      </el-pagination>
    </div>

    <!-- 新增、编辑弹出框 -->
    <easy4j-basic-dialog
      v-if="dialogFormVisible"
      :dialogBasicVisible.sync="dialogFormVisible"
      :basicDialog="basicDialog"
      :title="dialogTitle"
      :before-close="handleClose"
      @submit="onSubmit"
    >
      <el-form
        :model="editForm"
        :rules="rules"
        ref="editForm"
        label-width="80px"
      >
            <el-form-item label="字典名称" prop="name">
              <el-input
                v-model="editForm.name"
                placeholder="请输入字典名称"
              ></el-input>
            </el-form-item>
            <el-form-item label="字典编码" prop="code">
              <el-input
                v-model="editForm.code"
                placeholder="例：sys_user.status"
              ></el-input>
            </el-form-item>
            <el-form-item label="显示顺序" prop="sort">
              <el-input
                v-model.number="editForm.sort"
                placeholder="请输入显示顺序"
              ></el-input>
            </el-form-item>
            <el-form-item label="备注" prop="description">
              <el-input
                v-model="editForm.description"
                type="textarea"
                :rows="2"
                placeholder="请输入内容"
              ></el-input>
            </el-form-item>
      </el-form>
    </easy4j-basic-dialog>

    <!-- 字典配置 -->
    <dict-detail
      v-if="dialogConfigVisible"
      :dialogConfigVisible.sync="dialogConfigVisible"
      :parentRowInfo="currentRow"
    />
  </div>
</template>

<script>
import {
  getSysDictsPage,
  postSysDict,
  putSysDict,
  deleteSysDict
} from '@framework/api/common'

import Easy4jQueryForm from 'components/easy4j-query-form'
import Easy4jBasicTable from 'components/easy4j-basic-table'
import Easy4jPermissionButton from 'components/easy4j-permission-button'
import DictDetail from './dialog/dict-detail'
import Easy4jBasicDialog from 'components/easy4j-basic-dialog'

export default {
  components: {
    Easy4jQueryForm,
    Easy4jBasicTable,
    Easy4jPermissionButton,
    DictDetail,
    Easy4jBasicDialog,
  },
  props: {

  },
  data () {
    return {
      formConfig: {
        formItem: [
          {
            label: '字典名称',
            prop: 'name',
          },
          {
            label: '字典编码',
            prop: 'code',
          },
        ],
      },
      queryForm: {
        name: '',
        code: '',
      },
      tableConfig: {
        tableData: [],
        tableColumn: [
          {
            prop: 'name',
            label: '字典名称',
            slot: true,
          },
          {
            prop: 'code',
            label: '字典编码',
          },
          {
            prop: 'sort',
            label: '显示顺序',
          },
          {
            prop: 'description',
            label: '备注',
          },
        ],
        options: [
          {
            text: '编辑',
            type: 'edit',
            indeterminate: true, // 按钮展示不确定性
            showFunc: this.editshowFunc, // 自定义函数
          },
          {
            text: '删除',
            type: 'delete',
            indeterminate: true, // 按钮展示不确定性
            showFunc: this.deleteshowFunc, // 自定义函数
          },
        ],
        multiple: false,
        showRowIndex: true,
      },
      pageConfig: {
        current: 1,
        size: 10,
        total: 0,
      },
      dialogType: 'add',
      dialogTitle: '新增字典',
      editForm: {
        name: '',
        code: '',
        sort: '',
        description: '',
      },
      dialogFormVisible: false,
      rules: {
        name: [
          { required: true, message: '请输入字典名称', trigger: 'blur' }
        ],
        code: [
          { required: true, message: '请输入字典编码', trigger: 'blur' }
        ],
        sort: [
          { required: true, message: '请输入显示顺序', trigger: 'blur' },
          { type: 'number', message: '显示顺序必须为数字值' }
        ],
      },
      dialogConfigVisible: false,
      currentRow: {},
      basicDialog: {
        title: '',
      },
    }
  },
  watch: {

  },
  computed: {

  },
  mounted () {
    this.getTableList()
  },
  methods: {
    getTableList () {
      let params = {
        name: this.queryForm.name,
        code: this.queryForm.code,
        current: this.pageConfig.current,
        size: this.pageConfig.size,
      }

      const vm = this
      getSysDictsPage(params).then(res => {
        vm.tableConfig.tableData = res.data.records
        vm.pageConfig.total = res.data.total
      }).catch(error => {
        console.log(error)
      })
    },

    // 按条件查询
    onQueryTable (queryForm) {
      this.queryForm = Object.assign(this.queryForm, queryForm)

      // 初始化当前页数
      this.pageConfig.current = 1
      this.getTableList()
    },

    // 每页条数
    handleSizeChange (val) {
      this.pageConfig.current = 1
      this.pageConfig.size = val
      this.getTableList()
    },

    // 分页
    handleCurrentChange (val) {
      this.pageConfig.current = val
      this.getTableList()
    },

    // 行按钮带权限-编辑
    editshowFunc () {
      return this.$getPermission('sys:dict:update')
    },

    // 行按钮带权限-删除
    deleteshowFunc () {
      return this.$getPermission('sys:dict:delete')
    },

    // 操作行
    onEditRow (row, type) {
      switch (type) {
        case 'edit':
          this.openEditDialog(row)
          break
        case 'delete':
          this.deleteTableRow(row)
          break
      }
    },

    // 打开新增弹框
    openAddDialog () {
      this.dialogType = 'add'
      this.dialogTitle = '新增字典'
      this.basicDialog.title = '新增字典'

      // 初始化表单的值
      this.editForm = {
        roleName: '',
        roleCode: '',
        description: '',
      }

      this.dialogFormVisible = true
    },

    // 打开修改弹框
    openEditDialog (row) {
      this.dialogType = 'edit'
      this.dialogTitle = '编辑字典信息'
      this.basicDialog.title = '编辑字典信息'

      // 初始化表单的值
      this.editForm = Object.assign({}, row)

      this.dialogFormVisible = true
    },

    onSubmit (type) {
      if (type === 'cancel') {
        this.dialogFormVisible = false
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
        // todo
        if (vm.dialogType === 'add') {
          vm.addTableRow()
        } else {
          vm.editTableRow()
        }
      })
    },

    // 新增
    addTableRow () {
      let params = {
        name: this.editForm.name,
        code: this.editForm.code,
        sort: this.editForm.sort,
        description: this.editForm.description,
      }

      const vm = this
      postSysDict(params).then(res => {
        vm.$utils.alert('success', '操作成功！')
        vm.dialogFormVisible = false
        vm.getTableList()
      }).catch(error => {
        console.log(error)
      })
    },

    // 修改信息
    editTableRow () {
      let params = {
        id: this.editForm.id,
        name: this.editForm.name,
        code: this.editForm.code,
        sort: this.editForm.sort,
        description: this.editForm.description,
      }

      const vm = this
      putSysDict(params).then(res => {
        vm.$utils.alert('success', '操作成功！')
        vm.dialogFormVisible = false
        vm.getTableList()
      }).catch(error => {
        console.log(error)
      })
    },

    // 关闭编辑信息弹窗回调
    handleClose (done) {
      this.$refs['editForm'].resetFields()
      done()
    },

    // 删除
    deleteTableRow (row) {
      const vm = this
      deleteSysDict({ ids: row.id }).then(res => {
        vm.$utils.alert('success', '操作成功！')
        vm.getTableList()
      }).catch(error => {
        console.log(error)
      })
    },

    // 打开详情弹出框
    showDetail (row) {
      this.dialogConfigVisible = true
      this.currentRow = Object.assign({}, row)
    },
  }
}
</script>

<style scoped>

</style>
