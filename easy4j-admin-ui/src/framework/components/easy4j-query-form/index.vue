<!--
 * @Descripttion: 基于ElementUI中el-form组件的二次封装，用于表格条件查询。
 * 思路：
 * 1.当表单字段 <= 3 时，直接展示在表格上方；
 * 2.当表单字段 > 3 时，只展示前三个在表格上方，其余点击 更多 打开抽屉展示在抽屉内；
 *   抽屉内的表单按两列放置，抽屉设置点击键盘 esc 键或者点击界面遮罩处都能收起抽屉。
 * @version:
 * @Author: Huang Lina
 * @Date: 2020-03-23 10:13:32
 * @LastEditors: Huang Lina
 * @LastEditTime: 2020-05-08 16:07:08
-->
<template>
  <div
    class="easy4j-query-form"
  >
    <template>
      <el-form
        ref="queryForm"
        :inline="true"
        :model="queryForm"
        :label-width="labelWidth"
        @submit.native.prevent
        :class="[device, 'tableform']"
      >
        <el-form-item
          v-for="(item, index) in frontFormConfig"
          :key="index"
          :label="`${ item.label }:`"
          :prop="item.prop"
        >
          <el-select
            v-if="item.type === 'select' && !item.dict"
            v-model.trim="queryForm[item.prop]"
            :placeholder="item.placeholder || `请选择${ item.label }`"
            :clearable="item.clearable || true"
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
            :clearable="item.clearable || true"
            v-model="queryForm[item.prop]"
            @change="handleDictSelectChange($event, item.prop)"
          />
          <el-input
            v-if="item.type === 'input'"
            v-model.trim="queryForm[item.prop]"
            :placeholder="item.placeholder || `请输入${ item.label }`"
            :clearable="item.clearable || true"
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
          <!--<el-date-picker
            v-if="item.type === 'datePicker'"
            v-model.trim="queryForm[item.prop]"
            :type="item.pickerType"
            unlink-panels
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :format="item.format"
            :value-format="item.valueFormat"
            :default-time="item.defaultTime"
            :picker-options="item.pickerOptions"
            :clearable="item.clearable || true"
            :editable="device !== 'mobile'"
            style="width: 100%;"
          >
          </el-date-picker>-->
          <easy4j-time
            v-if="item.type === 'datePicker'"
            v-model.trim="queryForm[item.prop]"
            :type="item.pickerType"
            :format="item.format"
            :value-format="item.valueFormat"
            :default-time="item.defaultTime"
            :picker-options="item.pickerOptions"
            :clearable="item.clearable || true"
            style="width: 100%;">
          </easy4j-time>
        </el-form-item>
        <el-form-item label="">
          <label v-if="device !== 'mobile'" class="btn-label" :style="{ width: `${ parseInt(labelWidth) - 7 }px` }"></label>
          <el-button
            v-for="(item, index) in btnsList"
            :key="index"
            :type="item.btnType"
            @click="handleBtnClick(item.type)"
          >
            {{ item.text }}
          </el-button>
          <template v-if="showMore">
            <el-link
              v-if="currentFormConfig.length > frontFormConfig.length"
              type="primary"
              class="mar-l16"
              :underline="false"
              @click="showHandle"
            >
              更多条件&nbsp;
              <i :style="device === 'mobile' ? 'transform: rotate(90deg)': ''" class="el-icon-d-arrow-right"></i>
            </el-link>
            <el-link
              v-show="currentFormConfig.length === frontFormConfig.length"
              type="primary"
              class="mar-l16"
              :underline="false"
              @click="showHandle">
              收起&nbsp;<i style="transform: rotate(90deg)" class="el-icon-d-arrow-left"></i>
            </el-link>
          </template>
        </el-form-item>
      </el-form>
    </template>
    <template>
      <el-drawer
        title="搜索"
        :visible.sync="drawer"
        direction="rtl"
        :with-header="false"
        custom-class="query-form-drawer"
      >
        <div class="form-container" :class="[device]">
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
                :clearable="item.clearable || true"
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
                :clearable="item.clearable || true"
                v-model="queryForm[item.prop]"
                @change="handleDictSelectChange($event, item.prop)"
              />
              <el-input
                v-if="item.type === 'input'"
                v-model.trim="queryForm[item.prop]"
                :placeholder="item.placeholder || `请输入${ item.label }`"
                :clearable="item.clearable || true"
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
              <!--<el-date-picker
                v-if="item.type === 'datePicker'"
                v-model.trim="queryForm[item.prop]"
                :type="item.pickerType"
                unlink-panels
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                :format="item.format || getFormat(item.pickerType)"
                :value-format="item.valueFormat || getValFormat(item.pickerType)"
                :default-time="item.defaultTime"
                :picker-options="item.pickerOptions"
                :clearable="item.clearable || true"
                style="width: 100%;"
                :editable="device !== 'mobile'"
              >
              </el-date-picker>-->
              <easy4j-time
                v-if="item.type === 'datePicker'"
                v-model.trim="queryForm[item.prop]"
                :type="item.pickerType"
                :format="item.format"
                :value-format="item.valueFormat"
                :default-time="item.defaultTime"
                :picker-options="item.pickerOptions"
                :clearable="item.clearable || true"
                style="width: 100%;">
              </easy4j-time>
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
    </template>
  </div>
</template>

<script>
import Easy4jDictSelect from 'components/easy4j-dict-select'
import Easy4jDeptCascader from 'components/easy4j-dept-cascader'
import Easy4jTime from 'components/easy4j-time'
export default {
  components: {
    Easy4jDictSelect,
    Easy4jDeptCascader,
    Easy4jTime
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
      maxItemNum: 2, // 直接展示字段数量
      drawer: false,
      showMore: false
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
    },

    formItem () {
      if (!this.formConfig.hasOwnProperty('formItem')) {
        return []
      }

      let temp = this.formConfig.formItem
      if (!(temp instanceof Array)) {
        return []
      }

      return temp
    },

    frontFormConfig () {
      return this.currentFormConfig.slice(0, this.maxItemNum)
    },
    device () {
      return this.$store.state.device.device
    }
  },
  mounted () {
    this.showMore = this.formItem.length > this.maxItemNum
  },
  watch: {
    // 表单配置项
    formConfig: {
      deep: true,
      immediate: true,
      handler () {
        this.currentFormConfig = [...this.formItem]

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

    // 获取控件格式
    getFormat (pickerType) {
      switch (pickerType) {
        case 'datetimerange':
          return 'yyyy-MM-dd HH:mm:ss'
        case 'monthrange':
          return 'yyyy-MM'
      }
    },

    // 获取控件格式
    getValFormat (pickerType) {
      switch (pickerType) {
        case 'datetimerange':
          return 'yyyy-MM-dd HH:mm:ss'
        case 'monthrange':
          return 'yyyy-MM'
      }
    },

    // 显示更多的操作
    showHandle () {
      if (this.device === 'mobile') {
        this.maxItemNum = (this.currentFormConfig.length === this.maxItemNum ? 2 : this.currentFormConfig.length)
        return
      }
      this.drawer = !this.drawer
    }
  }
}
</script>

<style lang="scss">
  * {
    -webkit-tap-highlight-color: transparent;
  }
  .query-form-drawer {
    width: auto!important;
    -webkit-tap-highlight-color: transparent;
    .el-drawer__body {
      height: 0;
    }
    .mobile {
      .el-form-item {
        display: flex;
      }
      .el-form--inline .el-form-item__content{
        flex: 1;
      }
      .el-range-editor.el-input__inner {
        width: 100%;
      }
    }
  }
</style>

<style scoped lang="scss">
  .btn-label {
    display: inline-block;
  }
  .form-container {
    padding: 24px;
    width: 710px;
    &.mobile {
      padding: 10px 5px;
      max-width: 85vw;
      height: 100%;
      overflow-y: auto;
    }
  }
  .mobile.tableform {
    .el-form-item{
      width: 100%;
      margin-right: 0;
      display: flex;
    }
    & >>> .el-form-item__content {
      width: 80%;
      flex: 1;
      &>div {
        width: 100%;
      }
    }
    & >>> .el-form-item__content:nth-last-child(1) {
      text-align: right ;
    }
  }
</style>
