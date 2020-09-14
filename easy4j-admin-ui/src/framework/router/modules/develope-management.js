const develope = [
  {
    path: '/dev-create-form',
    name: 'CreateForm',
    component: resolve => require(['@/framework/views/develope-management/create-form/index.vue'], resolve),
    meta: {
      module: 'FORM-MANAGEMENT',
      title: '表单生成',
      showOnTab: true,
    }
  },
]

export default develope
