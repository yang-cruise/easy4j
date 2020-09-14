export default {
  data () {
    return {
      maxHeight: 0,
      reLayout: true,
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
  },
  methods: {
    setTableHeight () {
      this.reLayout = false
      this.$nextTick(() => {
        let height = this.$refs.basicTableContainer.offsetHeight
        this.maxHeight = height
        this.reLayout = true
      })
    },
  },
}
