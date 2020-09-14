export default function (data) {
  // 预定义字段
  let showForm = false
  let ruleForm = {}
  let ruleFormStr = ''

  let list = data.list
  console.log(list, '生成')

  let formItems = []

  // let str = ''
  for (let item of list) {
    console.log(item, 'item')
    let itemStr = ''
    if (item.type === 'input') {
      showForm = true

      // 获取表单对象属性
      if (item.field) {
        ruleForm[item.field] = 'haha'
      }

      itemStr = `  <el-form-item label="${ item.name }" prop="${ item.field }">
        <el-input v-model="ruleForm.name"></el-input>
      </el-form-item>`
      formItems.push(itemStr)
    }
  }

  let formItemArr = []
  if (showForm) {
    for (let formItem in ruleForm) {
      formItemArr.push(`  ${ formItem }: ${ ruleForm[formItem] }`)
    }
    ruleFormStr = `ruleForm: {
      ${ formItemArr.join(', \n') }
      }`
  } else {
    ruleFormStr = ''
  }

  let formTemplate =
  `<el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
    ${ formItems.join('\n') }
    </el-form>`

  console.log(formTemplate, 'formTemplate')
  console.log(ruleForm, 'ruleForm')

  return `<template>
  <div>
    ${ formTemplate }
  </div>
</template>

<script>
export default {
  components: {

  },
  props: {

  },
  data () {
    return {
      ${ ruleFormStr },
    }
  },
  watch: {

  },
  computed: {

  },
  mounted () {

  },
  methods: {

  }
}
</script>

<style scoped>

</style>
`
}
