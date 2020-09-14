<!--
 * @Descripttion:
 * @version:
 * @Author: Huang Lina
 * @Date: 2020-03-10 11:14:30
 * @LastEditors: Huang Lina
 * @LastEditTime: 2020-05-14 10:29:45
 -->
<template>
  <!-- 链接按钮 -->
  <el-link
    v-if="showBtn && linkBtn"
    :type="type"
    :underline="false"
    @click="handleClick"
  >
    {{ text }}
  </el-link>

  <!-- 普通按钮 -->
  <el-button
    v-else-if="showBtn && !linkBtn"
    :type="type"
    :icon="icon"
    :plain="plain"
    :size="size"
    @click="handleClick"
  >
    {{ text }}
  </el-button>
</template>

<script>
export default {
  components: {

  },
  props: {
    type: {
      type: String,
      default: 'primary',
    },
    permission: {
      type: String,
      default: '',
    },
    text: {
      type: String,
      default: '按钮',
    },
    icon: {
      type: String,
      default: '',
    },
    linkBtn: {
      type: Boolean,
      default: false,
    },
    plain: {
      type: Boolean,
      default: false,
    },
    size: {
      type: String,
      default: '',
    },
  },
  data () {
    return {
      mySysMenu: [],
    }
  },
  computed: {
    showBtn () {
      return this.$getPermission(this.permission)
    }
  },
  mounted () {
    this.mySysMenu = JSON.parse(localStorage.getItem('mySysMenu'))
  },
  watch: {

  },
  methods: {
    handleClick () {
      this.$emit('click')
    }
  },
}
</script>

<style scoped lang="scss">

</style>
