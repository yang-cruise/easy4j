<template>
  <div
    class="basic-table-container"
    ref="basicTableContainer"
  >
    <el-table
      v-if="reLayout"
      ref="easy4jBasicTable"
      :data="tableData"
      style="width: 100%"
      fit
      :max-height="maxHeight"
      :border="tableConfig.border || false"
      :show-summary="showSummary"
      :summary-method="getSummaries"
      @selection-change="handleSelectionChange"
      @cell-dblclick="handleCellDblclick"
      @row-contextmenu="handleRowContextmenu"
      @row-click="handleRowClick"
    >
      <el-table-column
        v-if="tableConfig.multiple"
        type="selection"
        width="55">
      </el-table-column>
      <el-table-column
        v-if="tableConfig.showRowIndex"
        type="index"
        width="55">
      </el-table-column>
      <el-table-column
        v-if="tableConfig.single"
        type=""
        width="55"
      >
        <template slot-scope="scope">
          <el-checkbox
            v-model="singleCheckbox[scope.$index]"
            @change="handleSingleSelection($event, scope.$index)"
          ></el-checkbox>
        </template>
      </el-table-column>
      <el-table-column
        v-for="(item, index) in tableConfig.tableColumn"
        :key="index"
        :prop="item.prop"
        :label="item.label"
        :width="item.width || ''"
        show-overflow-tooltip
        v-bind="item.attr"
        class-name="tag"
      >
        <template slot-scope="scope">
          <!-- 自定义展示列的内容，最新方案，墙裂推荐 -->
          <template v-if="item.slot">
            <slot :name="item.prop" :row="scope.row"></slot>
          </template>

          <!-- 正常展示对应字段内容 -->
          <template v-else>
            <!-- 右键菜单 -->
            <el-popover
              v-if="tableConfig.showRightMenu"
              popper-class="easy4j-table-menu-popover"
              placement="bottom"
              title=""
              width="60px"
              trigger="manual"
              content=""
              :visible-arrow="false"
              v-model="cellPopoverArr[scope.$index][item.prop]">
              <div
                slot="reference"
              >
                {{ scope.row[item.prop] }}
              </div>
              <div class="row-options">
                <el-link
                  v-for="(menuItem, menuIndex) of tableConfig.options"
                  :key="menuIndex"
                  :underline="false"
                  :disabled="disabledMenuItem(menuItem, scope.row)"
                  @click="handleRowMenuClick(scope.row, menuItem.type, disabledMenuItem(menuItem, scope.row))"
                >
                  {{ menuItem.text }}
                </el-link>
              </div>
            </el-popover>

            <template v-else>
              {{ scope.row[item.prop] }}
            </template>
          </template>
        </template>
      </el-table-column>
      <el-table-column
        v-if="tableConfig.options && tableConfig.options.length && showOptions"
        prop=""
        label="操作"
        :width="tableConfig.optionsWidth || 150"
        fixed="right"
      >
        <template slot-scope="scope">
          <template v-for="(optionsItem, index) in tableConfig.options">
            <el-link
              :key="`${ scope.row.id }-${ index }`"
              v-if="optionsItem.type !== 'delete' && showBtn(optionsItem, scope.row)"
              :type="optionsItem.btnType || 'primary'"
              :underline="false"
              :class="{ 'mar-r10': index !== tableConfig.options.length - 1 }"
              @click="handleClick(scope.row, optionsItem.type)"
            >
              {{ optionsItem.text }}
            </el-link>
            <el-popconfirm
              v-if="optionsItem.type === 'delete' && showBtn(optionsItem, scope.row)"
              :key="`${ scope.row.id }-${ index }`"
              title="是否确认删除此条记录？"
              placement="top"
              @onConfirm="handleClick(scope.row, optionsItem.type)"
            >
              <el-link
                :key="`${ scope.row.id }-${ index }`"
                v-if="optionsItem.type === 'delete' && showBtn(optionsItem, scope.row)"
                type="danger"
                :underline="false"
                :class="{ 'mar-r10': index !== tableConfig.options.length - 1 }"
                slot="reference"
              >
                {{ optionsItem.text }}
              </el-link>
            </el-popconfirm>
          </template>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
// import Clipboard from 'clipboard'
import tableAdaptive from '@mixins/table-adaptive.js'

export default {
  components: {

  },
  props: {
    tableConfig: {
      type: Object,
      default: () => {
        return {
          showOptions: true,
          border: false,
          multiple: false,
          showRowIndex: false,
          single: false,
          tableColumn: [],
          showRightMenu: false,
          options: [],
          optionsWidth: 0,
          summary: {
            columns: [],
          },
          tableData: [],
        }
      },
    },
  },
  mixins: [tableAdaptive],
  data () {
    return {
      multipleSelection: [],
      tableData: [],
      singleCheckbox: [],
      cellPopoverArr: [],
      columnArr: [],
      lastRowIndex: undefined,
      lastColumnProp: undefined,
    }
  },
  computed: {
    // 展示表尾合计标识
    showSummary () {
      if (!this.tableConfig.summary || !this.tableConfig.summary.columns) {
        return false
      }
      return Boolean(this.tableConfig.summary.columns.length)
    },

    // 展示行操作标识
    showOptions () {
      if (!this.tableConfig.hasOwnProperty('showOptions')) {
        return true
      }

      return this.tableConfig.showOptions
    }
  },
  mounted () {

  },
  watch: {
    tableConfig: {
      deep: true,
      immediate: true,
      handler () {
        this.tableData = [...this.tableConfig.tableData]

        // 列数组
        this.columnArr = [...this.tableConfig.tableColumn]

        if (this.tableConfig.showRightMenu) {
          // 单元格弹出框标识数组
          this.cellPopoverArr = this.getCellPopover()
        }

        if (this.tableConfig.single) {
          let len = this.tableData.length
          this.singleCheckbox = new Array(len).fill(false)
        }

        this.setTableHeight()
      },
    },
  },
  methods: {
    // 操作按钮按条件展示
    showBtn (optionsItem, row) {
      if (!optionsItem.hasOwnProperty('indeterminate')) {
        return true
      }
      return optionsItem.showFunc(row)
    },

    // 操作按钮点击事件
    handleClick (row, type) {
      this.$emit('editRow', row, type)
    },

    // 多选
    handleSelectionChange (val) {
      this.multipleSelection = val

      if (this.tableConfig.multiple) {
        this.$emit('getMultipleSelection', val)
      }
    },

    // 单选
    handleSingleSelection (val, index) {
      // 选中
      if (val) {
        // 初始化
        this.singleCheckbox = new Array(this.tableData.length).fill(false)
      }

      this.$set(this.singleCheckbox, index, val)

      this.$emit('getSingleSelection', val ? this.tableData[index] : [])
    },

    // 表尾合计行
    getSummaries (param) {
      if (!this.showSummary) {
        return ''
      }

      const { columns, data } = param
      const summaryColumns = this.tableConfig.summary.columns
      const summary = summaryColumns.map(item => item.props)

      const sums = []
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = '当前页合计'
          return
        }

        // 不需要合并
        if (!summary.includes(column.property)) {
          sums[index] = ''
          return
        }

        const values = data.map(item => Number(item[column.property]))
        if (!values.every(value => isNaN(value))) {
          sums[index] = values.reduce((prev, curr) => {
            const value = Number(curr)
            if (!isNaN(value)) {
              return prev + curr
            } else {
              return prev
            }
          }, 0)

          let propIndex = summary.findIndex(item => item === column.property)
          sums[index] += `${ summaryColumns[propIndex].unit }`
        } else {
          sums[index] = ''
        }
      })

      return sums
    },

    handleCellDblclick (row, column, cell, event) {
      // if (!column.property) {
      //   return
      // }

      // let text = row[column.property]
      // let clipboard = new Clipboard('.tag', {
      //   text: () => {
      //     return text
      //   }
      // })
      // clipboard.on('success', e => {
      //   this.$message({message: '复制成功', showClose: true, type: 'success'})
      //   // 释放内存
      //   clipboard.destroy()
      // })
      // clipboard.on('error', e => {
      //   this.$message({message: '复制失败,', showClose: true, type: 'error'})
      //   clipboard.destroy()
      // })
    },

    // 获取单元格弹出框显示标识数组
    getCellPopover () {
      let arr = []
      for (let item of this.tableData) {
        let obj = {}
        for (let prop in item) {
          obj[prop] = false
        }
        arr.push(obj)
      }

      return arr
    },

    // 行右键
    handleRowContextmenu (row, column, event) {
      if (this.tableConfig.showRightMenu) {
        event.preventDefault()

        // 1.取消上次
        if (this.lastColumnProp) {
          this.setCellPopover(this.lastRowIndex, this.lastColumnProp, false)
        }

        // 2.设置当次
        let rowIndex = this.getRowIndex(row, column)
        this.setCellPopover(rowIndex, column.property, true)

        // 3.保存为当次数据
        this.lastRowIndex = rowIndex
        this.lastColumnProp = column.property
      }
    },

    // 设置单个单元格弹窗框显示与否
    setCellPopover (rowIndex, property, flag) {
      let obj = this.cellPopoverArr[rowIndex]
      this.$set(obj, property, flag)
      this.$set(this.cellPopoverArr, rowIndex, obj)
    },

    // 获取当前点击的行索引
    getRowIndex (row, column) {
      let index = this.tableData.findIndex(item => item.id === row.id)
      return index
    },

    // 隐藏弹出框
    handleRowClick () {
      if (this.lastColumnProp) {
        this.setCellPopover(this.lastRowIndex, this.lastColumnProp, false)
      }
    },

    // 禁用菜单选项
    disabledMenuItem (menuItem, row) {
      if (!menuItem.hasOwnProperty('indeterminate')) {
        return false
      }
      return !menuItem.showFunc(row)
    },

    // 菜单选项点击事件
    handleRowMenuClick (row, type, flag) {
      if (!flag) {
        this.$emit('editRow', row, type)
      }
    },
  },
}
</script>

<style>
  .easy4j-table-menu-popover {
    width: auto!important;
    min-width: 0;
    padding: 8px 24px;
  }
</style>

<style scoped lang="scss">
  .row-options {
    display: flex;
    flex-direction: column;
    .el-link {
      padding: 8px 0;
    }
  }
</style>
