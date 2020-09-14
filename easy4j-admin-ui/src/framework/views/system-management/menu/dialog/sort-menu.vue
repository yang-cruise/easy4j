<!--
 * @Descripttion:
 * @version:
 * @Author: Huang Lina
 * @Date: 2020-05-09 15:14:19
 * @LastEditors: Huang Lina
 * @LastEditTime: 2020-05-09 17:41:47
 -->
<template>
  <easy4j-basic-dialog
    v-if="dialogVisible"
    :dialogBasicVisible.sync="dialogVisible"
    :basicDialog="basicDialog"
    :before-close="handleClose"
    class="full-height-dialog"
    @submit="onSubmit"
  >
    <div class="edit-menu-contanier">
      <div class="menu-tree-container">
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
              v-if="toggleTableFlag"
              :data="treeData"
              node-key="id"
              ref="tree"
              :props="defaultProps"
              draggable
              :default-expand-all="expandAll"
              :allow-drop="allowDrop"
            >
            </el-tree>
          </el-scrollbar>
        </div>
      </div>
    </div>
  </easy4j-basic-dialog>
</template>

<script>
import {
  getSysMenusTree,
  putSysMenusSort,
} from '@/framework/api/common'

import Easy4jBasicDialog from 'components/easy4j-basic-dialog'

export default {
  components: {
    Easy4jBasicDialog,
  },
  props: {
    dialogBasicVisible: {
      type: Boolean,
      default: false,
    },
    editDialog: {
      type: Object,
      default: () => {},
    },
  },
  data () {
    return {
      dialogVisible: false,
      treeData: [],
      defaultProps: {
        children: 'subSysMenuList',
        label: 'name',
      },
      expandAll: true,
      toggleTableFlag: true,
      basicDialog: {
        title: this.editDialog.title,
        btns: [
          {
            text: '展开所有',
            type: 'expand',
          },
          {
            text: '折叠所有',
            type: 'fold',
          },
          {
            text: '取消',
            type: 'cancel',
          },
          {
            btnType: 'primary',
            text: '确定',
            type: 'confirm',
          },
        ]
      }
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
      }
    },
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
      }).catch(error => {
        console.log(error)
      })
    },

    // 放置
    // 规则：只支持同层级拖拽
    allowDrop (draggingNode, dropNode, type) {
      let draggingNodeData = draggingNode.data
      let dropNodeData = dropNode.data

      // 只支持同层级拖拽
      if (type === 'inner') {
        return false
      }

      if (draggingNodeData.parentId !== dropNodeData.parentId) {
        return false
      }

      return true
    },

    // 展开 || 收起
    toggleExpand (flag) {
      this.expandAll = flag
      this.toggleTableFlag = false
      this.$nextTick(() => {
        this.toggleTableFlag = true
      })
    },

    onSubmit (type) {
      switch (type) {
        case 'expand':
          this.toggleExpand(true)
          break
        case 'fold':
          this.toggleExpand(false)
          break
        case 'cancel':
          this.dialogVisible = false
          break
        case 'confirm':
          this.putSysMenusSort()
          break
      }
    },

    getMenuAndId (menu, arr) {
      menu.forEach((el, index) => {
        arr.push({
          menuId: el.id,
          sort: index
        })

        if (el.subSysMenuList && el.subSysMenuList.length) {
          this.getMenuAndId(el.subSysMenuList, arr)
        }
      })
    },

    // 解析排序数据
    getSortMenu () {
      let arr = []
      console.log(this.treeData, 'treeData')
      this.getMenuAndId(this.treeData, arr)
      return arr
    },

    // 拖拽排序
    putSysMenusSort () {
      let params = this.getSortMenu()

      const vm = this
      putSysMenusSort(params).then(res => {
        vm.$utils.alert('success', '操作成功！')
        vm.dialogVisible = false
        vm.$emit('reloadTable')
      })
    },

    // 关闭编辑信息弹窗回调
    handleClose (done) {
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
    justify-content: center;
    height: 100%;
    .menu-tree-container {
      margin-right: 50px;
      .menu-tree {
        padding: 20px;
        min-width: 300px;
        height: 100%;
      }
    }
  }
</style>
