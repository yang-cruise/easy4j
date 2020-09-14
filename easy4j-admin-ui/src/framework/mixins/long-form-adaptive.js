export default {
  data () {
    return {
      reLayout: true,
      formMaxHeight: 0,
      screenWidth: window.innerWidth,
      screenHeight: window.innerHeight,
    }
  },
  watch: {
    screenWidth (val) {
      this.setTableHeight()
    },

    screenHeight (val) {
      this.setTableHeight()
    },
  },
  computed: {

  },
  mounted () {
    const vm = this
    window.onresize = () => {
      return (() => {
        // 宽度
        window.screenWidth = window.innerWidth
        vm.screenWidth = window.screenWidth

        // 高度
        window.screenHeight = window.innerHeight
        vm.screenHeight = window.screenHeight
      })()
    }

    this.setTableHeight()
  },
  methods: {
    setTableHeight () {
      this.reLayout = false
      this.$nextTick(() => {
        let height = this.$refs.pageFormContainer.offsetHeight

        // 减去按钮高度
        this.formMaxHeight = height - 40
        this.reLayout = true
      })
    },
  },
}
