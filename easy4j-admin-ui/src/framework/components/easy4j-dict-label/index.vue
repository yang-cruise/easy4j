<template>
  <div>
    {{ label }}
  </div>
</template>

<script>
import { mapGetters, mapMutations } from 'vuex'

import {
  getSysDictItemsByCode,
} from '@/framework/api/common'

export default {
  components: {

  },
  props: {
    dict: {
      type: String,
      default: '',
    },
    value: {
      type: [String, Number],
      default: '',
    },
  },
  data () {
    return {
      label: '',
    }
  },
  computed: {
    ...mapGetters(['loadingLabel']),

    // 将字典名转换成驼峰格式
    dictCamel () {
      return this.$utils.toCamel(this.dict)
    },

    loadingFlag () {
      return this.loadingLabel[this.dictCamel]
    },
  },
  mounted () {

  },
  watch: {
    loadingFlag: {
      immediate: true,
      handler () {
        this.getDict()
      },
    },

    value: {
      immediate: true,
      handler () {
        if (`${ this.value }`) {
          this.getDict()
        }
      },
    }
  },
  methods: {
    ...mapMutations(['setLoadingLabel']),

    getDict () {
      if (!this.dict) {
        return
      }

      // 获取缓存数据
      let localDict = localStorage.getItem(this.dictCamel)
      if (!localDict) {
        // 没有缓存数据
        // 判断是否有请求在进行中
        if (!this.loadingFlag) {
          this.getSysDictItemsByCode()
        }
      } else {
        // 使用缓存数据
        this.setLabel(JSON.parse(localDict))
      }
    },

    getSysDictItemsByCode () {
      // 记录请求状态
      this.setLoadingLabel({ [this.dictCamel]: true })

      const vm = this
      getSysDictItemsByCode({ code: this.dict }).then(res => {
        let options = res.data

        // 保存到localstorage中
        localStorage.setItem(vm.dictCamel, JSON.stringify(options))

        // 初始化请求状态标识
        this.setLoadingLabel({ [this.dictCamel]: false })

        vm.setLabel(options)
      }).catch(error => {
        console.log(error)
      })
    },

    // 匹配标签
    setLabel (options) {
      if (!options) {
        this.label = ''
        return
      }

      // 匹配
      let arr = options.filter(item => `${ item.key }` === `${ this.value }`)
      if (arr && arr.length) {
        this.label = arr[0].value
      }
    },
  },
}
</script>

<style scoped lang="scss">

</style>
