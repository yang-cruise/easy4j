<template>
  <div class="create-form">
    <database-management/>
    <el-container>
      <el-aside width="200px">
        <!-- 可拖拽列表 -->
        <draggable-lists/>
      </el-aside>
      <el-main class="widget-form-main">
        <div>
          <el-button type="primary" size="medium" @click="showDialog">生成代码</el-button>
        </div>
        <!-- 拖拽后效果展示区 -->
        <widget-form :widgetForm="widgetForm" :select.sync="widgetFormSelect"/>
      </el-main>
      <el-aside width="200px">
        <!-- 配置当前拖拽的元素的相关信息 -->
        <CopyCode/>
        <widget-config :widgetFormSelect="widgetFormSelect"/>
      </el-aside>
    </el-container>

    <el-dialog
      title="生成代码"
      :visible.sync="dialogVisible"
      width="80%"
      :before-close="handleClose">
      <editor
        v-model="htmlTemplate"
        @init="editorInit"
        lang="html"
        theme="chrome"
        width="100%"
        height="400px"
        :options="options"
      ></editor>
      <span>*禁止编辑</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="copyCode" class="tag">复制代码</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
// 第三方组件
import draggable from 'vuedraggable'
import editor from 'vue2-ace-editor'
import Clipboard from 'clipboard'

// 自定义组件
import DatabaseManagement from './database'
import DraggableLists from './draggable-lists'
import WidgetForm from './widget-form'
import WidgetConfig from './widget-config'
import CopyCode from './copy'

// 自定义函数
import generateCode from '@/framework/utils/generateCode'

export default {
  components: {
    draggable,
    editor,
    DatabaseManagement,
    DraggableLists,
    WidgetForm,
    WidgetConfig,
    CopyCode,
  },
  props: {

  },
  data () {
    return {
      widgetForm: {
        list: [],
      },
      widgetFormSelect: null,
      dialogVisible: false,
      htmlTemplate: '',
      options: {
        readOnly: true,
      },
    }
  },
  watch: {

  },
  computed: {

  },
  mounted () {

  },
  methods: {
    handleWidgetAdd (event) {
      console.log(event, 'haha')
    },

    handleClose () {
      //
    },

    editorInit () {
      require('brace/ext/language_tools') // language extension prerequsite...
      require('brace/mode/html')
      require('brace/mode/javascript') // language
      require('brace/mode/less')
      require('brace/theme/chrome')
      require('brace/snippets/javascript') // snippet
    },

    showDialog () {
      this.handleGenerateCode()
      this.dialogVisible = true
    },

    handleGenerateCode () {
      // this.htmlTemplate = generateCode(JSON.stringify(this.widgetForm))
      this.htmlTemplate = generateCode(this.widgetForm)
      this.$nextTick(() => {
        // const editor = ace.edit('codeeditor')
        // editor.session.setMode('ace/mode/html')
      })
    },

    // 复制代码
    copyCode () {
      // todo
      // this.handleGenerateCode()
      const vm = this
      let clipboard = new Clipboard('.tag', {
        text: () => {
          // return JSON.stringify(vm.htmlTemplate)
          return vm.htmlTemplate
        }
      })
      clipboard.on('success', function () {
        vm.$message({
          type: 'success',
          message: '复制成功！！！'
        })

        // 释放内存
        clipboard.destroy()
      })
    },

    copyLink (data) {
      console.log(1)
      let clipboard = new Clipboard('.tag', {
        text: function () {
          return 'aa'
        }
      })
      clipboard.on('success', e => {
        this.$message({message: '复制成功', showClose: true, type: 'success'})
        // 释放内存
        clipboard.destroy()
      })
      clipboard.on('error', e => {
        this.$message({message: '复制失败,', showClose: true, type: 'error'})
        clipboard.destroy()
      })
    }
  }
}
</script>

<style scoped>

</style>
