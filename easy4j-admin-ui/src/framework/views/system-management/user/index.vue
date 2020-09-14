<template>
  <div class="user-management">
    <div class="left-content">
      <el-tree
        :data="treeData"
        default-expand-all
        node-key="id"
        ref="tree"
        highlight-current
        :check-on-click-node="true"
        :expand-on-click-node="false"
        :props="defaultProps"
        :current-node-key="currentNodeKey"
        @node-click="handleNodeClick"
        @node-drop="handleDrop"
        draggable
      >
        <span class="custom-tree-node" slot-scope="{ node, data }">
        <span class="custom-tree-node-label">{{ node.label }}</span>
        <span>
          <el-dropdown
            placement="bottom-start"
            @command="handleCommand($event, data)"
          >
            <span class="el-dropdown-link">
              <el-button type="text" icon="el-icon-more"></el-button>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="addChild" v-if="$getPermission('sys:dept:insert')">
                添加子部门
              </el-dropdown-item>
              <el-dropdown-item command="update" v-if="$getPermission('sys:dept:update')">
                编辑名称
              </el-dropdown-item>
              <el-dropdown-item command="delete" v-if="$getPermission('sys:dept:delete')">
                删除
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </span>
      </span>
      </el-tree>
    </div>
    <div class="ver-devide"></div>
    <div class="right-content">

      <!-- 查询表单 -->
      <easy4j-query-form
        v-if="$getPermission('sys:user:select') && Object.keys(this.formConfig).length"
        :formConfig="formConfig"
        @queryTable="onQueryTable"
      />

      <!-- 操作按钮 -->
      <div class="options-container">
        <easy4j-permission-button
          permission="sys:user:insert"
          text="新增用户"
          icon="el-icon-plus"
          @click="openAddDialog"
        />
      </div>

      <!-- 表格 -->
      <easy4j-basic-table
        :tableConfig="tableConfig"
        @editRow="onEditRow"
      >
        <!-- 部门 -->
        <template slot='dept' slot-scope="scope">
          {{ scope.row.dept ? scope.row.dept.deptName : '' }}
        </template>

        <!-- 角色 -->
        <template slot='roles' slot-scope="scope">
          {{ scope.row.roles | strFmt('roleName') }}
        </template>

        <!-- 状态 -->
        <template slot='status' slot-scope="scope">
          <div @click.stop="showStatusChange(scope.row)">
            <el-switch
              class="disable-switch"
              v-model="scope.row.status"
              active-color="#13ce66"
              inactive-color="#C0CCDA"
              disabled
              :active-value="1"
              :inactive-value="2"
            >
            </el-switch>
          </div>
        </template>
      </easy4j-basic-table>

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
    </div>

    <!-- 新增、编辑用户弹出框 -->
    <edit-user
      v-if="dialogFormVisible"
      :dialogFormVisible.sync="dialogFormVisible"
      :editDialog="editDialog"
      :parentRowInfo="confirmDialog.row"
      @reloadTable="getTableList"
    />

    <!-- 确认弹出框：状态切换确认 -->
    <el-dialog
      v-if="confirmDialog.visible"
      title="提示"
      :visible.sync="confirmDialog.visible"
      width="30%"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
    >
      <span>{{ confirmDialog.tip }}</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="confirmDialog.visible = false">取 消</el-button>
        <el-button type="primary" @click="handleConfirm">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 重置密码 -->
    <reset-password
      v-if="dialogResetVisible"
      :dialogResetVisible.sync="dialogResetVisible"
      :parentRowInfo="confirmDialog.row"
      @reloadTable="getTableList"
    />

    <!-- 新增、编辑部门弹出框 -->
    <edit-dept
      v-if="dialogDeptVisible"
      :dialogDeptVisible.sync="dialogDeptVisible"
      :deptDialog="deptDialog"
      :parentRowInfo="deptDialog.row"
      @reloadTable="getSysDeptsTree"
    />
  </div>
</template>

<script>
import {
  getSysDeptsTree,
  getSysUsersPage,
  putSysDept,
  putSysUserStatus,
  deleteSysUser,
} from '@framework/api/common'

import EditUser from './dialog/edit-user'
import ResetPassword from './dialog/reset-password'
import EditDept from './dialog/edit-dept'

import Easy4jQueryForm from 'components/easy4j-query-form'
import Easy4jBasicTable from 'components/easy4j-basic-table'
import Easy4jPermissionButton from 'components/easy4j-permission-button'

export default {
  components: {
    Easy4jQueryForm,
    Easy4jBasicTable,
    Easy4jPermissionButton,
    EditUser,
    ResetPassword,
    EditDept,
  },
  props: {

  },
  data () {
    return {
      formConfig: {
        formItem: [
          {
            label: '账号',
            prop: 'account',
          },
          {
            label: '真实姓名',
            prop: 'realname',
          },
          {
            label: '手机',
            prop: 'mobile',
          },
          {
            label: '邮箱',
            prop: 'email',
          },
          {
            type: 'select',
            dict: 'sys_user.status',
            label: '状态',
            prop: 'status',
          },
        ],
      },
      queryForm: {
        deptId: '',
        account: '',
        realname: '',
        mobile: '',
        email: '',
        status: '',
      },
      tableConfig: {
        tableData: [],
        tableColumn: [
          {
            prop: 'account',
            label: '账号',
          },
          {
            prop: 'realname',
            label: '真实姓名',
          },
          {
            prop: 'dept',
            label: '部门',
            slot: true,
          },
          {
            prop: 'roles',
            label: '角色',
            slot: true,
          },
          {
            prop: 'email',
            label: '邮箱',
          },
          {
            prop: 'mobile',
            label: '手机',
          },
          {
            prop: 'gmtCreate',
            label: '创建时间',
          },
          {
            prop: 'status',
            label: '状态',
            slot: true,
          },
        ],
        options: [
          {
            text: '编辑',
            type: 'edit',
            indeterminate: true,
            showFunc: this.editshowFunc,
          },
          {
            text: '删除',
            type: 'delete',
            indeterminate: true,
            showFunc: this.deleteshowFunc,
          },
          {
            text: '重置密码',
            type: 'config',
            indeterminate: true,
            showFunc: this.configshowFunc,
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
      editDialog: {
        type: 'add', // add-新增, edit-编辑
        title: '新增用户', // 新增用户 || 编辑用户信息
      },
      dialogFormVisible: false,
      confirmDialog: {
        visible: false,
        type: 'switch', // switch-切换, reset-重置
        tip: '',
        row: {},
      },
      dialogResetVisible: false,
      treeData: [],
      currentNodeKey: '',
      defaultProps: {
        children: 'subSysDeptList',
        label: 'deptName',
      },
      dialogDeptVisible: false,
      deptDialog: {
        visible: false,
        type: 'add', // add-新增, edit-编辑, delete-删除
        row: {},
      },
    }
  },
  watch: {

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
    this.getSysDeptsTree()
  },
  methods: {
    // 获取所有部门列表
    getSysDeptsTree () {
      const vm = this
      getSysDeptsTree().then(res => {
        vm.treeData = [...res.data]

        // 选中第一个部门
        vm.currentNodeKey = vm.treeData && vm.treeData.length ? vm.treeData[0].id : -1
        vm.queryForm.deptId = vm.currentNodeKey

        vm.$nextTick(() => {
          vm.$refs.tree.setCurrentKey(vm.currentNodeKey)
        })

        vm.getTableList()
      }).catch(error => {
        console.log(error)
      })
    },

    // 部门选项
    handleCommand (val, data) {
      switch (val) {
        case 'addChild':
          this.deptDialog.type = 'add'
          this.deptDialog.title = '新增部门'
          break
        case 'update':
          this.deptDialog.type = 'edit'
          this.deptDialog.title = '编辑部门信息'
          break
        case 'delete':
          this.deptDialog.type = 'delete'
          this.deptDialog.title = '提示'
          break
      }

      this.deptDialog.row = Object.assign({}, data)
      this.dialogDeptVisible = true
    },

    // 节点被点击
    handleNodeClick (data, node, el) {
      this.queryForm.deptId = data.id
      this.getTableList()
    },

    // 节点拖拽
    handleDrop (draggingNode, dropNode, dropType, ev) {
      // draggingNode: 被拖拽节点对应的 Node
      // dropNode: 结束拖拽时最后进入的节点（当前层级最后一个节点，可根据节点上的信息获取父节信息）
      let currentData = draggingNode.data
      let currentDataId = currentData.id

      // 得到父节点id
      // 根据id获取父节点数据
      let parentId = dropNode.data.parentId
      let parentNode = this.$refs.tree.getNode(parentId)
      let parentData = parentNode.data

      // 获取拖拽后的顺序
      let index = parentData.subSysDeptList.findIndex(item => item.id === currentDataId)

      let params = {
        deptName: currentData.deptName,
        id: currentData.id,
        parentId: dropNode.data.parentId,
        sort: index + 1, // 顺序从1开始，索引要 +1
      }

      this.putSysDept(params)
    },

    // 保存部门拖拽
    putSysDept (params) {
      const vm = this
      putSysDept(params).then(res => {
        vm.$utils.alert('success', '操作成功！')
        vm.getSysDeptsTree()
      }).catch(error => {
        console.log(error)
      })
    },

    getTableList () {
      let params = {
        deptId: this.queryForm.deptId,
        account: this.queryForm.account,
        realname: this.queryForm.realname,
        mobile: this.queryForm.mobile,
        email: this.queryForm.email,
        status: this.queryForm.status,
        current: this.pageConfig.current,
        size: this.pageConfig.size,
      }

      const vm = this
      getSysUsersPage(params).then(res => {
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
    editshowFunc (row) {
      return this.$getPermission('sys:user:update') && row.account !== 'admin'
    },

    // 行按钮带权限-删除
    deleteshowFunc (row) {
      return this.$getPermission('sys:user:delete') && row.account !== 'admin'
    },

    // 行按钮带权限-重置密码
    configshowFunc (row) {
      return this.$getPermission('sys:user:update') && row.account !== 'admin'
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
          this.showResetPassword(row)
          break
      }
    },

    // 打开新增弹框
    openAddDialog () {
      this.editDialog.type = 'add'
      this.editDialog.title = '新增用户'

      this.confirmDialog.row = {}
      this.dialogFormVisible = true
    },

    // 打开编辑弹框
    openEditDialog (row) {
      this.editDialog.type = 'edit'
      this.editDialog.title = '编辑用户信息'

      this.confirmDialog.row = Object.assign({}, row)
      this.dialogFormVisible = true
    },

    // 打开状态切换确认框
    showStatusChange (row) {
      this.confirmDialog.type = 'switch'
      this.confirmDialog.tip = `是否确认${ row.status === 1 ? '禁用' : '启用' }账号“${ row.account }”`
      this.confirmDialog.row = row
      this.confirmDialog.visible = true
    },

    // 状态切换
    putSysUserStatus () {
      let params = {
        status: this.confirmDialog.row.status === 1 ? 2 : 1,
        userId: this.confirmDialog.row.id
      }

      const vm = this
      putSysUserStatus(params).then(res => {
        vm.$utils.alert('success', '操作成功！')
        vm.confirmDialog.visible = false
        vm.getTableList()
      }).catch(error => {
        console.log(error)
      })
    },

    // 删除
    deleteTableRow (row) {
      const vm = this
      deleteSysUser({ ids: row.id }).then(res => {
        vm.$utils.alert('success', '操作成功！')
        vm.getTableList()
      }).catch(error => {
        console.log(error)
      })
    },

    // 打开重置密码确认框
    showResetPassword (row) {
      this.confirmDialog.row = Object.assign({}, row)
      this.dialogResetVisible = true
    },

    // 打开确认框
    handleConfirm () {
      this.putSysUserStatus()
    },
  }
}
</script>

<style>
  .disable-switch.el-switch.is-disabled .el-switch__core {
    cursor: pointer!important;
  }
</style>

<style scoped lang="scss">
  .user-management {
    display: flex;
    justify-content: space-between;
    flex-direction: row!important;
    .left-content {
      .custom-tree-node {
        flex: 1;
        display: flex;
        align-items: center;
        justify-content: space-between;
        font-size: 14px;
        // min-width: 100px;
        width: 150px;
        overflow: hidden;
        .custom-tree-node-label {
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
      }
    }
    .ver-devide {
      border-right: 1px solid #DCDFE6;
      margin: 0 20px;
    }
    .right-content {
      width: 100%;
      overflow: hidden;
      display: flex;
      flex-direction: column;
    }
  }
</style>
