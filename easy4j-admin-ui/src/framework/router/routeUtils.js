/*
 * @Descripttion:
 * @version:
 * @Author: Huang Lina
 * @Date: 2020-04-27 10:37:55
 * @LastEditors: Huang Lina
 * @LastEditTime: 2020-05-09 13:58:18
 */

// 配置显示在页签的路由都加缓存
const setKeepAlive = (routes) => {
  for (let route of routes) {
    // 是页签
    if (route.meta && route.meta.showOnTab) {
      // 使用路由的默认缓存，没配置就默认设置为true
      let meta = route.meta
      if (!meta.hasOwnProperty('keepAlive')) {
        // route.meta.keepAlive = true
      }
    }

    if (route.children && route.children.length) {
      setKeepAlive(route.children)
    }
  }

  return routes
}

export {
  setKeepAlive
}
