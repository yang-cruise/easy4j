<template>
  <div class="esay4j-time">
    <el-date-picker
      @click.native="showHandle"
      style="width: 100%"
      :placeholder="placeholder"
      v-model="time"
      :type="type"
      unlink-panels
      :readonly="readonly_loacl"
      :clearable="clearable"
      :format="format|| getValFormat(type)"
      :default-time="defaultTime"
      :value-format="valueFormat || getValFormat(type)"
      range-separator="至"
      start-placeholder="开始日期"
      end-placeholder="结束日期"
      @change="changeHandle"
      :editable="device !== 'mobile'">
    </el-date-picker>
    <!--移动端时间区间选择-->
    <template v-if="device === 'mobile' && type.indexOf('range') > -1">
      <vue-hash-calendar
        model="dialog"
        @click="getTime"
        @confirm="confirmHandle"
        markType="circle"
        :pickerType="pickerType"
        :format="formatMobile"
        :scrollChangeDate="false"
        :defaultDatetime="defaultDatetime"
        :markDate="[{color: '#1c71fb', date: markDate}]"
        :visible.sync="show">
      </vue-hash-calendar>
    </template>
  </div>
</template>

<script>
import 'vue-hash-calendar/lib/vue-hash-calendar.css'
import vueHashCalendar from 'vue-hash-calendar'
import Vue from 'vue'
Vue.use(vueHashCalendar)
export default {
  name: 'easy4j-time',
  props: {
    type: {
      default: 'date',
    },
    placeholder: {
      default: '请选择时间'
    },
    readonly: {
      default: false
    },
    clearable: {
      default: true
    },
    format: {
      default: 'yyyy-MM-dd HH:mm:ss'
    },
    valueFormat: {
      default: 'yyyy-MM-dd'
    },
    defaultTime: {},
    value: {}
  },
  data () {
    return {
      time: '',
      // 移动端时间选择
      show: false,
      markDate: [],
      defaultDatetime: new Date(),
      formatMobile: 'YY/MM/DD hh:mm',
      pickerType: 'datetime',
      chooseTime: []
    }
  },
  computed: {
    device () {
      return this.$store.state.device.device
    },
    readonly_loacl () {
      return this.device === 'mobile' && this.type.indexOf('range') > -1 ? true : this.readonly
    }
  },
  mounted () {
    this.type.indexOf('time') < 0 && (this.pickerType = 'date')
  },
  methods: {
    changeHandle (time) {
      this.$emit('change', time)
      this.$emit('input', time)
    },
    showHandle () {
      if (this.device === 'mobile') {
        this.show = !this.show
        this.markDate = []
        this.chooseTime = []
        this.defaultDatetime = new Date()
      }
    },
    // 移动端获取时间
    getTime (time) {
      if (this.type.indexOf('range') > -1) {
        if (this.markDate.length > 1) {
          this.markDate.shift()
        }
        this.markDate.push(time)
      }
      this.chooseTime.length > 1 && (this.chooseTime.shift())
      this.chooseTime.push(this.dateFormat(time, this.valueFormat))
    },
    // 移动端确认时间
    confirmHandle (time) {
      time = new Date(time)
      if (this.type.indexOf('range') > -1) {
        this.time = this.chooseTime
        if ((new Date(this.time[0])).getTime() > (new Date(this.time[1])).getTime()) {
          let temp = [this.time[1], this.time[0]]
          this.time = temp
        }
      } else {
        this.time = this.dateFormat(time, this.valueFormat)
      }
      this.changeHandle(this.time)
    },
    dateFormat (date, fmt = 'yyyy-MM-dd HH:mm:ss') {
      let ret = null
      if (!date) {
        return date
      }
      date = new Date(date)
      const opt = {
        'y+': date.getFullYear().toString(), // 年
        'M+': (date.getMonth() + 1).toString(), // 月
        'd+': date.getDate().toString(), // 日
        'H+': date.getHours().toString(), // 时
        'm+': date.getMinutes().toString(), // 分
        's+': date.getSeconds().toString() // 秒
        // 有其他格式化字符需求可以继续添加，必须转化成字符串
      }
      for (let k in opt) {
        ret = new RegExp('(' + k + ')').exec(fmt)
        if (ret) {
          fmt = fmt.replace(ret[1], (ret[1].length === 1) ? (opt[k]) : (opt[k].padStart(ret[1].length, '0')))
        }
      }
      return fmt
    },
    // 获取控件格式
    getValFormat (pickerType) {
      switch (pickerType) {
        case 'datetimerange':
          return 'yyyy-MM-dd HH:mm:ss'
        case 'monthrange':
          return 'yyyy-MM'
        default:
          return 'yyyy-MM-dd'
      }
    }
  }
}
</script>

<style scoped>
.esay4j-time {
  display: inline-block;
  width: 100%;
}
</style>
