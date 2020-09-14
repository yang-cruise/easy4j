<template>
  <div class="menu-management">
    <!-- 查询表单 -->
    <easy4j-query-form
      v-if="$getPermission('sys:menu:select') && Object.keys(this.formConfig).length"
      :formConfig="formConfig"
      @queryTable="onQueryTable"
    />

    <div class="options-container">
      <easy4j-permission-button
        permission="sys:menu:insert"
        text="新增菜单"
        icon="el-icon-plus"
        @click="openAddDialog"
      />

      <easy4j-permission-button
        permission="sys:menu:update"
        text="菜单排序"
        icon="el-icon-rank"
        @click="openSortDialog"
      />

      <el-button
        size="medium"
        icon="el-icon-plus"
        @click="toggleExpand(true)"
      >展开所有</el-button>
      <el-button
        size="medium"
        icon="el-icon-minus"
        @click="toggleExpand(false)"
      >折叠所有</el-button>
    </div>

    <div
      class="basic-table-container"
      ref="basicTableContainer"
    >
      <el-table
        v-if="toggleTableFlag && reLayout"
        ref="menuTable"
        :data="tableData"
        style="width: 100%"
        fit
        stripe
        row-key="id"
        :max-height="maxHeight"
        :default-expand-all="expandAll"
        :tree-props="{children: 'subSysMenuList', hasChildren: 'hasChildren'}"
      >
        <el-table-column
          prop="name"
          label="菜单名称"
          width="180"
        >
        </el-table-column>
        <el-table-column
          prop="type"
          label="菜单类型"
        >
          <template slot-scope="scope">
            <easy4j-dict-label
              dict="sys_menu.type"
              :value="scope.row.type"
            />
          </template>
        </el-table-column>
        <el-table-column
          prop="perms"
          label="权限标识"
          width="180"
        >
        </el-table-column>
        <el-table-column
          prop="url"
          label="请求地址"
          width="180"
        >
        </el-table-column>
        <el-table-column
          prop="sort"
          label="显示顺序">
        </el-table-column>
        <el-table-column
          prop="icon"
          label="图标">
          <template slot-scope="scope">
            <i :class="scope.row.icon"></i>
          </template>
        </el-table-column>
        <el-table-column
          prop=""
          label="操作"
          width="100"
          fixed="right"
        >
          <template slot-scope="scope">
            <div>
              <el-link
                v-if="$getPermission('sys:menu:update')"
                type="primary"
                @click="openEditDialog(scope.row)"
                class="mar-r10"
                :underline="false"
              >编辑</el-link>
              <el-popconfirm
                title="是否确认删除此条记录？"
                :hideIcon="true"
                cancelButtonType=""
                placement="top"
                @onConfirm="deleteTableRow(scope.row)"
              >
                <el-link
                  v-if="$getPermission('sys:menu:delete')"
                  type="danger"
                  slot="reference"
                  :underline="false"
                >删除</el-link>
              </el-popconfirm>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 新增、编辑弹出框 -->
    <edit-menu
      v-if="dialogFormVisible"
      :dialogBasicVisible.sync="dialogFormVisible"
      :editDialog="editDialog"
      :parentRowInfo="currentRow"
      @reloadTable="getTableList"
    />

    <!-- 新增、编辑弹出框 -->
    <sort-menu
      v-if="dialogSortVisible"
      :dialogBasicVisible.sync="dialogSortVisible"
      :editDialog="sortDialog"
      @reloadTable="getTableList"
    />
  </div>
</template>

<script>
import {
  getSysMenusTree,
  deleteSysMenu
} from '@framework/api/common'

import tableAdaptive from '@mixins/table-adaptive.js'

import Easy4jQueryForm from 'components/easy4j-query-form'
import EditMenu from './dialog/edit-menu'
import SortMenu from './dialog/sort-menu'
import Easy4jDictSelect from 'components/easy4j-dict-select'
import Easy4jDictLabel from 'components/easy4j-dict-label'
import Easy4jPermissionButton from 'components/easy4j-permission-button'

export default {
  components: {
    Easy4jQueryForm,
    EditMenu,
    SortMenu,
    Easy4jDictSelect,
    Easy4jDictLabel,
    Easy4jPermissionButton,
  },
  props: {

  },
  mixins: [tableAdaptive],
  data () {
    return {
      formConfig: {
        formItem: [
          {
            label: '菜单名称',
            prop: 'name',
          },
          {
            type: 'select',
            dict: 'sys_menu.type',
            label: '类型',
            prop: 'type',
          },
        ],
      },
      queryForm: {
        name: '',
        type: '',
      },
      pageConfig: {
        current: 1,
        size: 10,
        total: 0,
      },
      tableData: [],
      typeOptions: [],
      editDialog: {
        type: 'add',
        title: '新增菜单',
      },
      currentRow: {},
      dialogFormVisible: false,
      expandAll: true,
      toggleTableFlag: true,
      dialogSortVisible: false,
      sortDialog: {
        title: '菜单排序',
      }
    }
  },
  watch: {

  },
  computed: {

  },
  filters: {
    typeFmt (val, typeOptions) {
      if (!val || !typeOptions || !typeOptions.length) {
        return ''
      }

      let arr = typeOptions.filter(item => item.key === val)
      return arr.length ? arr[0].value : ''
    },
  },
  mounted () {
    this.getTableList()
  },
  methods: {
    getTableList () {
      let params = {
        name: this.queryForm.name,
        type: this.queryForm.type,
        current: this.pageConfig.current,
        size: this.pageConfig.size,
      }

      const vm = this
      getSysMenusTree(params).then(res => {
        vm.tableData = res.data

        vm.setTableHeight()
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

    // 打开新增弹框
    openAddDialog () {
      this.editDialog.type = 'add'
      this.editDialog.title = '新增菜单'
      this.currentRow = {}
      this.dialogFormVisible = true
    },

    // 打开排序弹框
    openSortDialog () {
      this.dialogSortVisible = true
    },

    // 打开修改弹框
    openEditDialog (row) {
      this.editDialog.type = 'edit'
      this.editDialog.title = '编辑菜单信息'
      this.currentRow = Object.assign({}, row)
      this.dialogFormVisible = true
    },

    toggleExpand (flag) {
      this.expandAll = flag
      this.toggleTableFlag = false
      this.$nextTick(() => {
        this.toggleTableFlag = true
      })
    },

    // 删除
    deleteTableRow (row) {
      const vm = this
      deleteSysMenu({ ids: row.id }).then(res => {
        vm.$utils.alert('success', '操作成功！')
        vm.getTableList()

        // 刷新菜单数据
        vm.$store.dispatch('getMySysMenuTree')
        vm.$store.dispatch('getMyPermissions')
      }).catch(error => {
        console.log(error)
      })
    },
  },

  beforeDestroy () {
    // 销毁时移除缓存，达到页面级缓存
    localStorage.removeItem('sys_menu.type')
  },
}
</script>

<style scoped>

</style>
