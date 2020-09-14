<template>
  <div class="role-management">
    <!-- 查询表单 -->
    <easy4j-query-form
      v-if="$getPermission('sys:role:select') && Object.keys(this.formConfig).length"
      :formConfig="formConfig"
      @queryTable="onQueryTable"
    />

    <!-- 操作按钮 -->
    <div class="options-container">
      <easy4j-permission-button
        permission="sys:role:insert"
        text="新增角色"
        icon="el-icon-plus"
        @click="openAddDialog"
      />
    </div>

    <!-- 表格 -->
    <easy4j-basic-table
      :tableConfig="tableConfig"
      @editRow="onEditRow"
    >
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
        :total="pageConfig.total"
      />
    </div>

    <!-- 新增、编辑弹出框 -->
    <easy4j-basic-dialog
      v-if="dialogFormVisible"
      :dialogBasicVisible.sync="dialogFormVisible"
      :basicDialog="basicDialog"
      @submit="onSubmit"
    >
      <el-form
        :model="editForm"
        :inline="true"
        :rules="rules"
        ref="editForm"
        label-width="80px"
      >
        <el-row>
          <el-col :span="12">
            <el-form-item label="角色名称" prop="roleName">
              <el-input
                v-model="editForm.roleName"
                placeholder="例：管理员"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="角色标识" prop="roleCode">
              <el-input
                v-model="editForm.roleCode"
                placeholder="例：admin"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="description">
              <el-input
                v-model="editForm.description"
                type="textarea"
                :rows="2"
                placeholder="请输入内容"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </easy4j-basic-dialog>

    <!-- 权限配置弹出框 -->
    <easy4j-basic-dialog
      v-if="permissinConfigVisible"
      :dialogBasicVisible.sync="permissinConfigVisible"
      :basicDialog="basicPermissionDialog"
      class="full-height-dialog"
      @submit="onPermissionSubmit"
    >
      <el-scrollbar
        class="tree-scrollbar"
        :native="false"
        wrapStyle=""
        wrapClass="x-hidden mar-b5-im"
        viewClass="scrollbar-height"
        viewStyle=""
        :noresize="false"
        tag="div"
      >
        <el-tree
          :data="permissionData"
          show-checkbox
          default-expand-all
          node-key="id"
          ref="tree"
          highlight-current
          :check-on-click-node="true"
          :expand-on-click-node="false"
          :props="defaultProps"
        >
        </el-tree>
      </el-scrollbar>
    </easy4j-basic-dialog>
  </div>
</template>

<script>
import {
  getSysRolesPage,
  postSysRole,
  putSysRole,
  deleteSysRole,
  getSysMenusTree,
  getSysRolePermissions,
  putSysRolePermissions
} from '@/framework/api/common'

import Easy4jQueryForm from 'components/easy4j-query-form'
import Easy4jBasicTable from 'components/easy4j-basic-table'
import Easy4jPermissionButton from 'components/easy4j-permission-button'
import Easy4jBasicDialog from 'components/easy4j-basic-dialog'

export default {
  components: {
    Easy4jQueryForm,
    Easy4jBasicTable,
    Easy4jPermissionButton,
    Easy4jBasicDialog,
  },
  props: {

  },
  data () {
    return {
      formConfig: {
        formItem: [
          {
            label: '角色名称',
            prop: 'roleName',
          },
          {
            label: '角色标识',
            prop: 'roleCode',
          },
        ],
      },
      queryForm: {
        roleName: '',
        roleCode: '',
      },
      tableConfig: {
        tableData: [],
        tableColumn: [
          {
            prop: 'roleName',
            label: '角色名称',
          },
          {
            prop: 'roleCode',
            label: '角色标识',
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
          {
            text: '权限配置',
            type: 'config',
            indeterminate: true, // 按钮展示不确定性
            showFunc: this.configshowFunc, // 自定义函数
          },
        ],
        optionsWidth: 180,
        multiple: false,
      },
      pageConfig: {
        current: 1,
        size: 10,
        total: 0,
      },
      dialogType: 'add',
      dialogTitle: '新增角色',
      editForm: {
        roleName: '',
        roleCode: '',
        description: '',
      },
      dialogFormVisible: false,
      basicDialog: {
        title: '',
      },
      rules: {
        roleName: [
          { required: true, message: '请输入角色名称', trigger: 'blur' }
        ],
        roleCode: [
          { required: true, message: '请输入角色标识', trigger: 'blur' }
        ],
      },
      permissinConfigVisible: false,
      basicPermissionDialog: {
        title: '权限配置',
      },
      configRow: {},
      isIndeterminate: [],
      checkAll: [],
      checkedSubItem: [],
      permissionData: [],
      defaultProps: {
        children: 'subSysMenuList',
        label: 'name',
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
        roleName: this.queryForm.roleName,
        roleCode: this.queryForm.roleCode,
        current: this.pageConfig.current,
        size: this.pageConfig.size,
      }

      const vm = this
      getSysRolesPage(params).then(res => {
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

    // 不定按钮展示规则
    showFunc (row) {
      // 非管理员，显示
      return `${ row.id }` !== `1`
    },

    // 行按钮带权限-编辑
    editshowFunc (row) {
      return this.showFunc(row) && this.$getPermission('sys:role:update')
    },

    // 行按钮带权限-删除
    deleteshowFunc (row) {
      return this.showFunc(row) && this.$getPermission('sys:role:delete')
    },

    // 行按钮带权限-重置密码
    configshowFunc (row) {
      return this.showFunc(row) && this.$getPermission('sys:role:permission')
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
        case 'config':
          this.openConfig(row)
          break
      }
    },

    // 打开新增弹框
    openAddDialog () {
      this.dialogType = 'add'
      this.dialogTitle = '新增角色'
      this.basicDialog.title = '新增角色'

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
      this.dialogTitle = '修改角色信息'
      this.basicDialog.title = '编辑角色信息'

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
        roleName: this.editForm.roleName,
        roleCode: this.editForm.roleCode,
        description: this.editForm.description,
      }

      const vm = this
      postSysRole(params).then(res => {
        vm.$utils.alert('success', '操作成功！')
        vm.dialogFormVisible = false
        vm.getTableList()
      })
    },

    // 修改信息
    editTableRow () {
      let params = {
        id: this.editForm.id,
        roleName: this.editForm.roleName,
        roleCode: this.editForm.roleCode,
        description: this.editForm.description,
      }

      const vm = this
      putSysRole(params).then(res => {
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
      deleteSysRole({ ids: row.id }).then(res => {
        vm.$utils.alert('success', '操作成功！')
        vm.getTableList()
      }).catch(error => {
        console.log(error)
      })
    },

    // 打开配置弹出框
    openConfig (row) {
      this.permissinConfigVisible = true
      this.configRow = Object.assign({}, row)
      this.getSysMenusTree(row.id)
    },

    // 查询所有菜单
    getSysMenusTree (id) {
      const vm = this
      getSysMenusTree().then(res => {
        vm.permissionData = res.data
        vm.getSysRolePermissions(id)
      }).catch(error => {
        console.log(error)
      })
    },

    // 查询当前角色拥有的菜单权限
    getSysRolePermissions (roleId) {
      const vm = this
      getSysRolePermissions({ roleId }).then(res => {
        let currentRoleMenu = [...res.data]

        // 只设置叶子节点
        vm.setCheckedNode(currentRoleMenu)
      }).catch(error => {
        console.log(error)
      })
    },

    // 默认选中当前节点
    setCheckedNode (currentRoleMenu) {
      let arr = []
      for (let item of currentRoleMenu) {
        // 根据id获取节点
        let currentNode = this.$refs.tree.getNode(item)
        if (!currentNode) {
          continue
        }

        // 根据节点获取节点数据
        let currentNodeData = currentNode.data

        if (!currentNodeData.subSysMenuList || !currentNodeData.subSysMenuList.length) {
          // 是叶子节点，保存
          arr.push(item)
        }
      }

      this.$refs.tree.setCheckedKeys(arr)
    },

    // 获取选中的权限
    getRoleMenu () {
      // 获取所有选中的节点id
      let checkedKeys = [...this.$refs.tree.getCheckedKeys()]
      for (let item of checkedKeys) {
        // 根据id获取节点
        let currentNode = this.$refs.tree.getNode(item)
        if (!currentNode) {
          continue
        }

        // 根据节点数据获取parentId
        let parentId = currentNode.data.parentId

        // 保存parentId，此时会遍历到新加的父节点，会继续往上查找直到根节点
        // parentId为0则不保存
        if (!checkedKeys.includes(parentId) && parentId) {
          checkedKeys.push(parentId)
        }
      }

      return checkedKeys
    },

    onPermissionSubmit (type) {
      if (type === 'cancel') {
        this.permissinConfigVisible = false
        return
      }

      this.putSysRolePermissions()
    },

    // 权限分配
    putSysRolePermissions () {
      let menuIds = this.getRoleMenu()
      let params = {
        roleId: this.configRow.id,
        menuIds: menuIds.join(),
      }

      const vm = this
      putSysRolePermissions(params).then(res => {
        vm.$utils.alert('success', '操作成功！')
        vm.permissinConfigVisible = false
        vm.getTableList()
      }).catch(error => {
        console.log(error)
      })
    },
  }
}
</script>

<style scoped>
  .tree-scrollbar {
    height: 100%;
  }

  .scrollbar-height {
    height: 100%;
  }
</style>
