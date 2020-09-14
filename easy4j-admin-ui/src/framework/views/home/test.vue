<!--
 * @Descripttion:
 * @version:
 * @Author: Huang Lina
 * @Date: 2020-03-11 17:57:51
 * @LastEditors: Huang Lina
 * @LastEditTime: 2020-04-29 10:31:55
 -->
<template>
  <div>
    <!-- dialog -->
    <el-button @click="dialogBasicVisible = true">button</el-button>
    <easy4j-basic-dialog
      v-if="dialogBasicVisible"
      :dialogBasicVisible.sync="dialogBasicVisible"
      :basicDialog="{}"
      @submit="dialogBasicVisible = false"
    >
      哈哈
    </easy4j-basic-dialog>
    {{ aa }}--

    <easy4j-city-cascader
      v-model="aa"
      level="0"
      @change="handleChange"
    />
  </div>
</template>

<script>
import Easy4jBasicDialog from 'components/easy4j-basic-dialog'
import Easy4jCityCascader from 'components/easy4j-city-cascader'

export default {
  components: {
    Easy4jBasicDialog,
    Easy4jCityCascader,
  },
  props: {

  },
  data () {
    return {
      dialogBasicVisible: false,
      // aa: '',
      // aa: 120000,
      // aa: 120100,
      // aa: '130300',
      // aa: '120000',
      // aa: '120100',
      // aa: [],
      aa: ['120000', '130000'],
      // aa: [120000, 130300]
    }
  },
  watch: {

  },
  computed: {

  },
  mounted () {

  },
  methods: {
    handleChange (val) {
      console.log('11111', val)
    }
  }
}
</script>

<style scoped>

</style>>
