<template>
  <el-dialog
    class="abow_dialog"
    :title="`${ basicDialog.title || '标题' }`"
    :visible.sync="dialogVisible"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    :width="dialogWidth"
  >
    <!-- <el-scrollbar
      class="dialog-scrollbar"
      :native="false"
      wrapStyle=""
      wrapClass="x-hidden mar-b5-im"
      viewClass="scrollbar-height"
      viewStyle=""
      :noresize="false"
      tag="div"
    >
      <slot/>
    </el-scrollbar> -->
    <slot/>
    <span slot="footer" class="dialog-footer">
      <el-button
        class="dialog-btn"
        v-for="(item, index) in btnsList"
        :key="index"
        :type="item.btnType"
        :loading="item.loading || false"
        @click="submit(item.type)"
      >
        {{ item.text }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  components: {

  },
  props: {
    dialogBasicVisible: {
      type: Boolean,
      default: false,
    },
    basicDialog: {
      type: Object,
      default: () => {
        return {
          title: '标题',
          btns: [],
        }
      },
    },
    dialogWidth: {
      type: String,
      default: '720px',
    },
  },
  data () {
    return {
      dialogVisible: false,
    }
  },
  computed: {
    btnsList () {
      if (!this.basicDialog.btns || !this.basicDialog.btns.length) {
        return [
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

      return this.basicDialog.btns
    }
  },
  mounted () {

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
  updated () {
    // this.$refs.scrollbar.update()
  },
  methods: {
    submit (val) {
      this.$emit('submit', val)
    }
  },
}
</script>

<style lang="scss">
  .abow_dialog {
    display: flex;
    justify-content: center;
    overflow: hidden;
    flex-direction: column;
    .el-dialog {
      margin: 0 auto !important;
      max-height: 90%;
      overflow: hidden;
      display: flex;
      flex-direction: column;
      .el-dialog__header {
        padding: 0 20px!important;
        line-height: 47px;
        border-bottom: 1px solid rgba(0, 0, 0, 0.08);
      }
      .el-dialog__body {
        flex: 1;
        overflow: hidden;
        padding: 24px 18px 42px 18px!important;
        position: relative;
      }
      .el-dialog__footer {
        padding: 0 18px 18px 18px!important;
      }
    }
  }

  .dialog-btn.el-button + .dialog-btn.el-button {
    margin-left: 24px;
  }

  .dialog-scrollbar.scrollbar-height {
    height: calc(100% - 42px);
    position: absolute;
    top: 24px;
    left: 18px;
  }
</style>

<style scoped lang="scss">
  .dialog-scrollbar {
    // height: 100%;
    flex: 1;
  }

  .scrollbar-height {
    height: 100%;
  }

  .left{
        width:200px;
        height:100%;
        background:#f00;
        float:left;
    }
   .page-component__scroll{
       height:100%;
    }
   .box{
height:200px;
background-color:#000000;
width:200px;
color:#fff;
    }
   .page-component__scroll .el-scrollbar__wrap {
     overflow-x: hidden;
    }
</style>
