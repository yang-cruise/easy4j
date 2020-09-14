
// index: 位置-可用于菜单排序。列表内的此字段可以为不连续的数字，遍历时按数字大小排序。必填。
// title: 菜单名-展示的菜单名。必填。
// iconName: 图标名，为空时认为不展示图标（疑问：不展示图标时，菜单收起需要展示一个菜单logo，这个时候怎么处理？？？没有icon展示时，菜单名会往前挤，出现菜单名不对齐的问题，怎么解决？？？）
// routePath: 路由地址-点击之后跳转的界面，为空时认为不能跳转。（疑问：路径生效的前提是前端有这个界面，后台配置前是否要前台先写好界面？？？是否可以在前端写个路由的配置文件，后台返回路由名字即可？？？）
// children: 子菜单-长度为空时认为没有子菜单。这个字段长度不为0认为没有路由跳转，可以不加routePath字段。
const leftMenuList = [
  {
    index: '1',
    title: '系统管理',
    iconName: 'el-icon-setting',
    routePath: '',
    children: [
      {
        index: '1-1',
        title: '用户管理',
        iconName: '',
        routePath: '/user',
        children: [],
      },
      {
        index: '1-2',
        title: '角色管理',
        iconName: '',
        routePath: '/role',
        children: [],
      },
      {
        index: '1-3',
        title: '菜单管理',
        iconName: '',
        routePath: '/menu',
        children: [],
      },
    ],
  },
  {
    index: '2',
    title: '开发管理',
    iconName: '',
    routePath: '',
    children: [{
      index: '2-1',
      title: '表单生成',
      iconName: '',
      routePath: '/createForm',
      children: [],
    }],
  }
]

export {
  leftMenuList
}
