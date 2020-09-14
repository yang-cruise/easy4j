<template>
  <easy4j-basic-dialog
    v-if="dialogVisible"
    :dialogBasicVisible.sync="dialogVisible"
    :basicDialog="basicDialog"
    class="full-height-dialog"
    @submit="onSubmit"
  >
    <el-row>
      <el-col :span="12">
        字典名称<span class="mar-l20">{{ parentRowInfo.name }}</span>
      </el-col>
      <el-col :span="12">
        字典编码<span class="mar-l20">{{ parentRowInfo.code }}</span>
      </el-col>
    </el-row>

    <el-table
      :data="tableData"
      style="width: 100%"
      :height="tableHeight"
    >
      <el-table-column
        prop="key"
        label="字典值"
      >
        <template slot-scope="scope">
          <template v-if="scope.row.rowFlag">
            <div class="must-cell">
              <span class="must-icon">*</span>
              <el-input
                v-model="scope.row.key"
                placeholder="请输入字典值"
              ></el-input>
            </div>
          </template>
          <template v-else>
            {{ scope.row.key }}
          </template>
        </template>
      </el-table-column>
      <el-table-column
        prop="value"
        label="显示文本"
      >
        <template slot-scope="scope">
          <template v-if="scope.row.rowFlag">
            <div class="must-cell">
              <span class="must-icon">*</span>
              <el-input
                v-model="scope.row.value"
                placeholder="请输入显示文本"
              ></el-input>
            </div>
          </template>
          <template v-else>
            {{ scope.row.value }}
          </template>
        </template>
      </el-table-column>
      <el-table-column
        prop="sort"
        label="排序"
      >
        <template slot-scope="scope">
          <template v-if="scope.row.rowFlag">
            <div class="must-cell">
              <span class="must-icon">*</span>
              <el-input
                v-model.number="scope.row.sort"
                placeholder="请输入排序"
                @blur="formatSort(scope.$index)"
              ></el-input>
            </div>
          </template>
          <template v-else>
            {{ scope.row.sort }}
          </template>
        </template>
      </el-table-column>
      <el-table-column
        prop=""
        label="操作"
        width="180"
      >
        <template slot-scope="scope">
          <div>
            <el-link
              v-if="scope.row.rowFlag"
              type="primary"
              @click="submitRow(scope.row)"
            >保存</el-link>
            <template v-else>
              <el-link type="primary" @click="editCurrentRow(scope.$index)">修改</el-link>
              <el-popconfirm
                title="是否确认删除此条记录？"
                :hideIcon="true"
                cancelButtonType=""
                placement="top"
                @onConfirm="deleteTableRow(scope.row)"
              >
                <el-link type="danger" slot="reference">删除</el-link>
              </el-popconfirm>
            </template>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加列按钮 -->
    <div @click="addNewRow" class="add-btn">
      <i class="el-icon-plus"></i>&nbsp;
      添加
    </div>
  </easy4j-basic-dialog>
</template>

<script>
import {
  getSysDictItemsById,
  postSysDictItem,
  putSysDictItem,
  deleteSysDictItem
} from '@framework/api/common'

import Easy4jBasicDialog from 'components/easy4j-basic-dialog'

export default {
  components: {
    Easy4jBasicDialog,
  },
  props: {
    dialogConfigVisible: {
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
      tableData: [],
      rowForm: {
        key: '',
        value: '',
        sort: 0,
        rowFlag: 'add', // add-新增，modify-修改，''-初始值
      },
      basicDialog: {
        title: '字典管理',
        btns: [
          {
            text: '返回',
            type: 'cancel',
          },
        ],
      }
    }
  },
  watch: {
    // 父 => 子
    dialogConfigVisible: {
      immediate: true,
      handler () {
        this.dialogVisible = this.dialogConfigVisible
      },
    },

    // 子 => 父
    dialogVisible: {
      immediate: true,
      handler () {
        this.$emit('update:dialogConfigVisible', this.dialogVisible)
      }
    }
  },
  computed: {
    tableHeight () {
      return 'calc(100% - 86px)'
    }
  },
  mounted () {
    let id = this.parentRowInfo.id
    if (id) {
      this.getSysDictItemsById(id)
    }
  },
  methods: {
    getSysDictItemsById (id) {
      const vm = this
      getSysDictItemsById({ sysDictId: id }).then(res => {
        vm.tableData = res.data
        for (let item of vm.tableData) {
          item.rowFlag = ''
        }
      }).catch(error => {
        console.log(error)
      })
    },

    // 列表末尾新增一行编辑框
    addNewRow () {
      let addRow = this.tableData.filter(item => item.rowFlag === 'add')
      if (addRow && addRow.length > 0) {
        this.$utils.alert('error', '已有一行可使用！')
        return
      }

      this.tableData.push(this.rowForm)
    },

    // 编辑已有行
    editCurrentRow (index) {
      let obj = Object.assign(this.tableData[index], { rowFlag: 'modify' })

      // 改变当前行的状态，展示编辑框
      this.$set(this.tableData, index, obj)
    },

    // 排序输入框失去焦点事件
    formatSort (index) {
      let sortVal = this.tableData[index].sort

      // 输入非数值则默认为0，输入数值则取绝对值
      this.$set(this.tableData[index], 'sort', isNaN(sortVal) ? 0 : Math.abs(sortVal))
    },

    onSubmit (type) {
      if (type === 'cancel') {
        this.dialogVisible = false
        return
      }

      this.submitRow()
    },

    // 保存当前编辑的行数据
    submitRow (row) {
      if (!row.key) {
        this.$utils.alert('error', '请输入字典值！')
        return
      }
      if (!row.value) {
        this.$utils.alert('error', '请输入显示文本！')
        return
      }

      if (row.rowFlag === 'add') {
        this.addTableRow(row)
      } else if (row.rowFlag === 'modify') {
        this.editTableRow(row)
      }
    },

    // 新增
    addTableRow (row) {
      let params = {
        dictId: this.parentRowInfo.id,
        key: row.key,
        value: row.value,
        sort: row.sort,
      }

      const vm = this
      postSysDictItem(params).then(res => {
        vm.$utils.alert('success', '操作成功！')
        vm.getSysDictItemsById(vm.parentRowInfo.id)
      }).catch(error => {
        console.log(error)
      })
    },

    // 编辑
    editTableRow (row) {
      let params = {
        id: row.id,
        key: row.key,
        value: row.value,
        sort: row.sort,
      }

      const vm = this
      putSysDictItem(params).then(res => {
        vm.$utils.alert('success', '操作成功！')
        vm.getSysDictItemsById(vm.parentRowInfo.id)
      }).catch(error => {
        console.log(error)
      })
    },

    // 删除
    deleteTableRow (row) {
      const vm = this
      deleteSysDictItem({ ids: row.id }).then(res => {
        vm.$utils.alert('success', '操作成功！')
        vm.getSysDictItemsById(vm.parentRowInfo.id)
      }).catch(error => {
        console.log(error)
      })
    },
  },
}
</script>

<style scoped lang="scss">
  .must-cell {
    display: flex;
    align-items: center;
    justify-content: flex-start;
    .must-icon {
      color: red;
      margin-right: 5px;
    }
  }

  .add-btn {
    width: 100%;
    box-sizing: border-box;
    display: flex;
    justify-content: center;
    align-items: center;
    border: 2px dashed;
    border-color: #eee;
    cursor: pointer;
    line-height: 32px;
    border-radius: 4px;
    margin: 20px 0;
    &:hover {
      color: #409eff;
      border-color: #c6e2ff;
      background-color: #ecf5ff;
    }
  }

  .close-btn-contanier {
    width: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
  }
</style>
