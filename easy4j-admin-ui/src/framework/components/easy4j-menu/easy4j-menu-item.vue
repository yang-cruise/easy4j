<!--
 * @Descripttion:
 * @version:
 * @Author: Huang Lina
 * @Date: 2020-03-10 11:14:30
 * @LastEditors: Huang Lina
 * @LastEditTime: 2020-04-26 15:56:55
 -->
<template>
  <!-- 叶子结点 -->
  <el-menu-item
    :index="getIndex(parentData.id, itemData.id)"
    :route="{ path: itemData.url }"
    :key="itemData.id"
    v-if="!itemData.subSysMenuList || !itemData.subSysMenuList.length"
  >
    <i :class="`${ itemData.icon }`"></i>
    <span slot="title">{{ itemData.name }}</span>
  </el-menu-item>

  <!-- 还有下一级 -->
  <el-submenu :index="`${ itemData.id }`" :key="itemData.id" v-else>
    <template slot="title">
      <i :class="`${ itemData.icon }`"></i>
      <span slot="title">{{ itemData.name }}</span>
    </template>
    <easy4j-menu-item
      v-for="item in itemData.subSysMenuList"
      :key="item.id"
      :itemData="item"
      :parentData="itemData"
    />
  </el-submenu>
</template>

<script>
export default {
  name: 'easy4jMenuItem',
  components: {

  },
  props: {
    itemData: {
      type: Object,
      default: () => {},
    },
    parentData: {
      type: Object,
      default: () => {},
    },
  },
  data () {
    return {

    }
  },
  watch: {

  },
  computed: {

  },
  mounted () {

  },
  methods: {
    getIndex (parentSort, sonSort) {
      return parentSort ? `${ parentSort }-${ sonSort }` : `${ sonSort }`
    },
  }
}
</script>

<style scoped>

</style>
