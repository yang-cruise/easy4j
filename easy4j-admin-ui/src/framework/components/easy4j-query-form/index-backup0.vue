<template>
  <div class="easy4j-query-form">
    <el-drawer
      title="搜索"
      :visible.sync="drawer"
      direction="rtl"
      :with-header="false"
      :before-close="handleClose"
      size="40%"
      custom-class="query-form-drawer"
    >
      <div class="form-container">
        <el-form
          ref="queryForm"
          :inline="true"
          :model="queryForm"
          :label-width="labelWidth"
          @submit.native.prevent
        >
          <el-form-item
            v-for="(item, index) in currentFormConfig"
            :key="index"
            :label="`${ item.label }:`"
            :prop="item.prop"
          >
            <el-select
              v-if="item.type === 'select' && !item.dict"
              v-model.trim="queryForm[item.prop]"
              :placeholder="item.placeholder || `请选择${ item.label }`"
              :clearable="item.clearable"
              filterable
              @clear="handleItemClear(item.prop)"
            >
              <el-option
                v-for="(optionItem, optionIndex) in item.options"
                :key="optionIndex"
                :label="optionItem.label"
                :value="optionItem.value"
              ></el-option>
            </el-select>
            <easy4j-dict-select
              v-if="item.type === 'select' && item.dict"
              :dict="item.dict"
              :label="item.label"
              :clearable="item.clearable"
              v-model="queryForm[item.prop]"
              @change="handleDictSelectChange($event, item.prop)"
            />
            <el-input
              v-if="item.type === 'input'"
              v-model.trim="queryForm[item.prop]"
              :placeholder="item.placeholder || `请输入${ item.label }`"
              clearable
              @clear="handleItemClear(item.prop)"
              @keyup.native.enter="handleBtnClick('query')"
            ></el-input>

            <!-- 框架部门级联-单选 -->
            <easy4j-dept-cascader
              v-if="item.type === 'cascader' && item.prop === 'deptId'"
              :label="item.label"
              v-model="queryForm[item.prop]"
            />

            <!-- 日期选择 -->
            <el-date-picker
              v-if="item.type === 'datePicker' && (item.pickerType !== 'datetimerange')"
              v-model.trim="queryForm[item.prop]"
              type="daterange"
              unlink-panels
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :picker-options="item.pickerOptions"
              :clearable="item.clearable"
            >
            </el-date-picker>

            <!-- 时间日期选择 -->
            <el-date-picker
              v-if="item.type === 'datePicker' && (item.pickerType === 'datetimerange')"
              v-model.trim="queryForm[item.prop]"
              type="datetimerange"
              unlink-panels
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              format="yyyy-MM-dd HH:mm:ss"
              value-format="yyyy-MM-dd HH:mm:ss"
              :default-time="item.defaultTime"
              :picker-options="item.pickerOptions"
              :clearable="item.clearable"
            >
            </el-date-picker>
          </el-form-item>
          <el-form-item label="">
            <label class="btn-label" :style="{ width: `${ parseInt(labelWidth) - 7 }px` }"></label>
            <el-button
              v-for="(item, index) in btnsList"
              :key="index"
              :type="item.btnType"
              @click="handleBtnClick(item.type)"
            >
              {{ item.text }}
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-drawer>
    <el-button
      class="search-btn"
      icon="el-icon-search"
      circle
      @click="drawer = !drawer"
    ></el-button>
  </div>
</template>

<script>
import Easy4jDictSelect from 'components/easy4j-dict-select'
import Easy4jDeptCascader from 'components/easy4j-dept-cascader'

export default {
  components: {
    Easy4jDictSelect,
    Easy4jDeptCascader,
  },
  props: {
    formConfig: {
      type: Object,
      default: () => {},
    },
    labelWidth: {
      type: String,
      default: '100px',
    },
  },
  data () {
    return {
      queryForm: {},
      currentFormConfig: [],
      propOptions: {
        checkStrictly: true, // 选择任一级
        emitPath: false, // 只返回该节点的值，不返回路径
      },
      drawer: false,
    }
  },
  computed: {
    btnsList () {
      let oriBtnList = [
        {
          btnType: 'primary',
          text: '查询',
          type: 'query',
        },
        {
          btnType: '',
          text: '重置',
          type: 'reset',
        },
      ]

      if (!this.formConfig || !this.formConfig.formItem) {
        return []
      }

      if (!this.formConfig.options || !this.formConfig.options.length) {
        return oriBtnList
      }

      return [oriBtnList[0]]
    }
  },
  mounted () {

  },
  watch: {
    // 表单配置项
    formConfig: {
      deep: true,
      immediate: true,
      handler () {
        this.currentFormConfig = [...this.formConfig.formItem]

        let obj = {}
        if (this.currentFormConfig.length) {
          for (let item of this.currentFormConfig) {
            if (!item.hasOwnProperty('type')) {
              item['type'] = 'input'
            }
            obj[item.prop] = item.defaultVal || ''
          }
        }

        // 表单初始化
        this.queryForm = Object.assign({}, obj)
      },
    },
  },
  methods: {
    // 点击事件
    handleBtnClick (type) {
      if (type === 'query') {
        this.onSubmit(type)
      } else {
        this.resetForm(type)
      }
    },

    // 查询
    onSubmit (type) {
      this.$emit('queryTable', this.queryForm, type)
    },

    // 重置表单
    resetForm (type) {
      this.$refs['queryForm'].resetFields()
      this.$emit('queryTable', this.queryForm, type)
    },

    // 清除单个表单数据
    handleItemClear (prop) {
      this.$emit('clear', prop)
    },

    // 字典下拉菜单选择切换
    handleDictSelectChange (val, prop) {
      this.$emit('change', val, prop)
    },

    handleClose (done) {
      done()
    },
  },
}
</script>

<style scoped lang="scss">
  .btn-label {
    display: inline-block;
  }

  .easy4j-query-form {
    display: flex;
    justify-content: flex-end;
    position: relative;
    .form-container {
      padding: 24px;
    }
    .search-btn {
      position: absolute;
    }
  }

  .query-form-drawer {

  }
</style>
