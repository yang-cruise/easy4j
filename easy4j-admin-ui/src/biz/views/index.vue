<template>
  <div class="table-main-box">
    <easy4j-query-form
      v-if="Object.keys(formConfig).length"
      :formConfig="formConfig"
      @queryTable="submitQuery"
    />
  </div>
</template>

<script>
import Easy4jQueryForm from '@framework/components/easy4j-query-form'
export default {
  components: {
    Easy4jQueryForm,
  },
  data () {
    return {
      time: '',
      tableData: [
        {
          date: '2016-05-03',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1518 弄'
        }
      ],
      multipleSelection: [],
      queryform: {
        current: 1,
        total: 100,
        size: 10
      },
      formConfig: {
        formItem: [
          { label: '订单号', prop: 'id' },
          { label: '下单时间', prop: 'time', type: 'datePicker', pickerType: 'datetimerange', valueFormat: 'yyyy-MM-dd HH:mm:ss' },
          { label: '身份证号', prop: 'idcard' },
          { label: '产品名称', prop: 'productTitle' },
          { label: '一级合伙人', prop: 'stage1' },
          { label: '二级合伙人', prop: 'stage2' },
          { label: '播种人', prop: 'salesOfficeStaffName' },
          { label: '营业部名称', prop: 'salesOfficeName' },
          { label: '分局名称', prop: 'branch' },
          { label: '订单状态', prop: 'orderStatus', type: 'select', dict: 'orders.order_status' },
          { label: '结算状态', prop: 'settled', type: 'select', dict: 'orders.settled' },
          { label: '运营商', prop: 'operator', type: 'select', dict: 'product.operator' },
          { label: '下单时间', prop: 'time', type: 'datePicker', pickerType: 'datetime', valueFormat: 'yyyy-MM-dd HH:mm:ss' },
        ]
      }
    }
  },
  computed: {

  },
  mounted () {
    this.tableData.push(...this.tableData)
  },
  methods: {
    handleSelectionChange (val) {
      this.multipleSelection = val
    },
    submitQuery (queryform) {
      Object.assign(this.queryform, queryform)
    }
  },
}
</script>

<style scoped lang="scss">
.tablet{
  height: 600px !important;
}
</style>
