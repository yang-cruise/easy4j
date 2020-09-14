<template>
  <easy4j-basic-dialog
    v-if="dialogVisible"
    :dialogBasicVisible.sync="dialogVisible"
    :basicDialog="editDialog"
    :title="editDialog.title"
    :before-close="handleClose"
    dialogWidth="80%"
    class="full-height-dialog"
    @submit="onSubmit"
  >
    <div class="edit-menu-contanier">
      <div class="menu-tree-container">
        上级菜单
        <div class="menu-tree">
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
              :data="treeData"
              show-checkbox
              default-expand-all
              node-key="id"
              ref="tree"
              highlight-current
              :check-on-click-node="true"
              :expand-on-click-node="false"
              :check-strictly="true"
              :props="defaultProps"
              @check-change="handleCheckChange"
            >
            </el-tree>
          </el-scrollbar>
        </div>
      </div>
      <div class="menu-form-container">
        <el-form
          :model="editForm"
          :rules="rules"
          ref="editForm"
          label-width="80px"
        >
          <el-form-item label="菜单类型" prop="type">
            <easy4j-dict-select
              class="full-width-select"
              dict="sys_menu.type"
              label="类型"
              v-model="editForm.type"
              @change="handleMenuChange"
            />
          </el-form-item>
          <el-form-item label="菜单名称" prop="name">
            <el-input
              v-model.trim="editForm.name"
              placeholder="请输入菜单名称"
            ></el-input>
          </el-form-item>
          <el-form-item
            v-if="!editForm.type || !['B'].includes(editForm.type)"
            label="显示顺序"
            prop="sort"
            :rules="rules.sort"
          >
            <el-input
              v-model.number="editForm.sort"
              placeholder="请输入显示顺序"
            ></el-input>
          </el-form-item>
          <el-form-item
            v-else
            label="显示顺序"
            prop="sort"
            :rules="[]"
          >
            <el-input
              v-model.number="editForm.sort"
              placeholder="请输入显示顺序"
              disabled
            ></el-input>
          </el-form-item>
          <el-form-item label="权限标识" prop="perms">
            <el-input
              v-model="editForm.perms"
              placeholder="例：sys:user:list"
            ></el-input>
          </el-form-item>
          <el-form-item
            label="请求地址"
            prop="url"
            v-if="!editForm.type || ['D', 'B'].includes(editForm.type)"
          >
            <el-input
              v-model="editForm.url"
              placeholder="例：/sysUser/list"
              :disabled="disabledUrl"
            ></el-input>
          </el-form-item>
          <el-form-item label="请求地址" prop="mUrl" v-else>
            <el-input
              v-model="editForm.mUrl"
              placeholder="例：/sysUser/list"
            ></el-input>
          </el-form-item>
          <el-form-item label="菜单图标" prop="icon">
            <el-input
              v-model="editForm.icon"
              placeholder="例：el-icon-setting"
            ></el-input>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </easy4j-basic-dialog>
</template>

<script>
import {
  getSysMenusTree,
  postSysMenu,
  putSysMenu,
} from '@/framework/api/common'

import Easy4jBasicDialog from 'components/easy4j-basic-dialog'
import Easy4jDictSelect from 'components/easy4j-dict-select'

export default {
  components: {
    Easy4jBasicDialog,
    Easy4jDictSelect,
  },
  props: {
    dialogBasicVisible: {
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
    // 验证显示顺序
    let validateSort = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入正整数，从1开始'))
      } else {
        if (value < 1) {
          callback(new Error('请输入正整数，从1开始'))
        }

        callback()
      }
    }

    return {
      dialogVisible: false,
      treeData: [],
      defaultProps: {
        children: 'subSysMenuList',
        label: 'name',
      },
      editForm: {
        type: '',
        name: '',
        sort: 1,
        perms: '',
        url: '',
        mUrl: '',
        icon: '',
      },
      rules: {
        type: [
          { required: true, message: '请选择类型', trigger: 'change' }
        ],
        name: [
          { required: true, message: '请输入菜单名称', trigger: 'blur' }
        ],
        sort: [
          { required: true, message: '请输入显示顺序', trigger: 'blur' },
          { type: 'number', message: '请输入正整数，从1开始', trigger: 'blur' },
          { validator: validateSort, trigger: 'blur' }
        ],
        mUrl: [
          { required: true, message: '请输入请求地址', trigger: 'blur' }
        ],
      },
      currentChecked: {},
      disabledUrl: false,
    }
  },
  watch: {
    // 父 => 子
    dialogBasicVisible: {
      immediate: true,
      handler () {
        this.dialogVisible = this.dialogBasicVisible
      },
    },

    // 子 => 父
    dialogVisible: {
      immediate: true,
      handler () {
        this.$emit('update:dialogBasicVisible', this.dialogVisible)

        if (!this.dialogVisible) {
          if (this.$refs.editForm) {
            this.$refs.editForm.resetFields()
          }
        }
      }
    },

    editDialog: {
      deep: true,
      immediate: true,
      handler () {
        if (this.editDialog.type && this.editDialog.type === 'edit') {
          this.editForm = Object.assign({}, this.parentRowInfo)
          this.$set(this.editForm, 'mUrl', this.editForm.url)
          this.currentChecked.id = this.editForm.parentId

          // 菜单才有url
          this.disabledUrl = this.editForm.type !== 'M'
        }
      },
    }
  },
  computed: {

  },
  mounted () {
    this.getTableList()
  },
  methods: {
    getTableList () {
      const vm = this
      getSysMenusTree(this.queryform).then(res => {
        vm.treeData = res.data

        // 设置禁用节点
        vm.setDisabled(vm.treeData)

        vm.$nextTick(() => {
          if (vm.$refs.tree) {
            vm.$refs.tree.setCheckedKeys([vm.currentChecked.id])
          }
        })
      }).catch(error => {
        console.log(error)
      })
    },

    // 设置禁用节点
    setDisabled (treeData) {
      for (let item of treeData) {
        item.disabled = item.type === 'B'
        if (item.subSysMenuList && item.subSysMenuList.length) {
          this.setDisabled(item.subSysMenuList)
        }
      }
    },

    // 点击勾选框
    handleCheckChange (data, checked, indeterminate) {
      // if (data.type === 'B') {
      //   this.$utils.alert('warning', '按钮不能作为上级菜单！')
      //   return
      // }

      if (checked) {
        // 选中
        this.currentChecked = data
        this.$refs.tree.setCheckedKeys([data.id])
      } else {
        // 取消选中
        this.currentChecked = {}
      }
    },

    // 选择菜单类型
    handleMenuChange (val) {
      if (!val) {
        this.disabledUrl = false
        return
      }

      // 只有菜单有url
      this.disabledUrl = val !== 'M'
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
      let params = {
        // parentId: parentId, // 父菜单ID
        type: this.editForm.type,
        name: this.editForm.name,
        sort: this.editForm.sort,
        perms: this.editForm.perms,
        // url: this.editForm.url,
        icon: this.editForm.icon,
      }

      // 选中按钮菜单
      if (this.editForm.type === 'B') {
        if (!this.currentChecked.id) {
          // 未勾选上级菜单
          this.$utils.alert('warning', '请选择一个上级菜单！')
          return
        }

        params.parentId = this.currentChecked.id
      }

      // 菜单才有url
      if (this.editForm.type === 'M') {
        params.url = this.editForm.mUrl
        params.parentId = this.currentChecked.id || 0
      }

      const vm = this
      postSysMenu(params).then(res => {
        vm.$utils.alert('success', '操作成功！')
        vm.dialogVisible = false
        vm.$emit('reloadTable')

        // 刷新菜单数据
        vm.$store.dispatch('getMySysMenuTree')
        vm.$store.dispatch('getMyPermissions')
      }).catch(error => {
        console.log(error)
      })
    },

    // 修改信息
    editTableRow () {
      let params = {
        parentId: this.currentChecked.id || 0, // 父菜单ID
        id: this.editForm.id,
        type: this.editForm.type,
        name: this.editForm.name,
        sort: this.editForm.sort,
        perms: this.editForm.perms,
        // url: this.editForm.url,
        icon: this.editForm.icon,
      }

      // 选中按钮菜单
      if (this.editForm.type === 'B') {
        if (!this.currentChecked.id) {
          // 未勾选上级菜单
          this.$utils.alert('warning', '请选择一个上级菜单！')
          return
        }
      }

      // 菜单才有url
      if (this.editForm.type === 'M') {
        params.url = this.editForm.mUrl
      }

      const vm = this
      putSysMenu(params).then(res => {
        vm.$utils.alert('success', '操作成功！')
        vm.dialogVisible = false
        vm.$emit('reloadTable')

        // 刷新菜单数据
        vm.$store.dispatch('getMySysMenuTree')
        vm.$store.dispatch('getMyPermissions')
      }).catch(error => {
        console.log(error)
      })
    },

    // 关闭编辑信息弹窗回调
    handleClose (done) {
      this.$refs['editForm'].resetFields()
      this.dialogVisible = false
      done()
    },
  },
}
</script>

<style scoped lang="scss">
  .tree-scrollbar {
    height: 100%;
  }

  .edit-menu-contanier {
    display: flex;
    justify-content: flex-start;
    height: 100%;
    .menu-tree-container {
      margin-right: 50px;
      .menu-tree {
        border: 1px solid #ccc;
        padding: 20px;
        min-width: 300px;
        height: 100%;
      }
    }
    .menu-form-container {
      flex: 1;
      max-width: 600px;
    }
  }
</style>
