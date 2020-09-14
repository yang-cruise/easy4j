const systemManagement = [
  {
    path: '/sys-user',
    name: 'User',
    component: resolve => require(['@/framework/views/system-management/user/index.vue'], resolve),
    meta: {
      module: 'USER-MANAGEMENT',
      title: '用户管理',
      showOnTab: true
    }
  },
  {
    path: '/sys-role',
    name: 'Role',
    component: resolve => require(['@/framework/views/system-management/role/index.vue'], resolve),
    meta: {
      module: 'ROLE-MANAGEMENT',
      title: '角色管理',
      showOnTab: true
    }
  },
  {
    path: '/sys-menu',
    name: 'Menu',
    component: resolve => require(['@/framework/views/system-management/menu/index.vue'], resolve),
    meta: {
      module: 'MENU-MANAGEMENT',
      title: '菜单管理',
      showOnTab: true
    }
  },
  {
    path: '/sys-dict',
    name: 'DataDictionary',
    component: resolve => require(['@/framework/views/system-management/data-dictionary/index.vue'], resolve),
    meta: {
      module: 'DATA-DICTIONARY',
      title: '数据字典',
      showOnTab: true
    }
  },
  {
    path: '/sys-config',
    name: 'SysConfig',
    component: resolve => require(['@/framework/views/system-management/config/index.vue'], resolve),
    meta: {
      module: 'SYS-CONFIG',
      title: '系统配置',
      showOnTab: true
    }
  },
]

export default systemManagement
