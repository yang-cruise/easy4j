import http from './request/request'

// 命名规范：
// 增：post
// 删：delete
// 改：put
// 查：get

// 查询网站配置
export const getSiteConfig = params => http.get('/sys_configs/site_config', params)

// 查询登录配置
export const getLoginConfig = params => http.get('/sys_configs/login_config', params)

// 查询微信配置
export const getWechatConfig = params => http.get('/sys_configs/wechat_config', params)

// 查询系统配置详情
export const getSysConfigDetail = params => http.get('/sys_configs/detail', params)

// 更新系统配置
export const putSysConfig = params => http.put('/sys_configs', params)

// 获得验证码
export const getCaptcha = (params) => http.get('/sys_login/captcha', params)

// 密码登录
export const getTokenByAccount = (params) => http.get('/sys_login/token_by_account', params, { catch: '自定义异常处理' })

// 短信登录
export const getTokenBySms = (params) => http.get('/sys_login/token_by_sms', params, { catch: '自定义异常处理' })

// 微信登录
export const getTokenByWechat = (params) => http.get('/sys_login/token_by_wechat', params, { catch: '自定义异常处理' })

// 修改个人密码
export const putSelfPassword = params => http.put('/sys_users/self_password', params)

// 修改个人信息
export const putSelfInfo = params => http.put('/sys_users/self_info', params)

// 系统管理-用户管理-分页查询用户列表
export const getSysUsersPage = params => http.get('/sys_users/page', params)

// 系统管理-用户管理-新增系统用户
export const postSysUser = params => http.post('/sys_users', params, { catch: '自定义异常处理' })

// 系统管理-用户管理-切换状态
export const putSysUserStatus = params => http.put(`/sys_users/${ params.id }/status`, params)

// 系统管理-用户管理-更新用户
export const putSysUser = params => http.put('/sys_users', params)

// 系统管理-用户管理-根据ID查询用户详情
export const getSysUserDetail = params => http.get(`/sys_users/${ params.id }`, params)

// 系统管理-用户管理-删除用户
export const deleteSysUser = params => http.delete(`/sys_users/${ params.ids }`, params)

// 系统管理-用户管理-管理员重置密码
export const putSysUserPassword = params => http.put(`/sys_users/${ params.id }/password`, params)

// 系统管理-用户管理-查询当前用户信息
export const getSelfInfo = () => http.get('/sys_users/self_info')

// 系统管理-部门-查询全部部门树
export const getSysDeptsTree = params => http.get('/sys-depts/tree', params)

// 系统管理-部门-新增部门
export const postSysDept = params => http.post('/sys_depts', params, { catch: '自定义异常处理' })

// 系统管理-部门-更新部门
export const putSysDept = params => http.put('/sys_depts', params)

// 系统管理-部门-删除部门
export const deleteSysDept = params => http.delete(`/sys_depts/${ params.ids }`, params)

// 系统管理-部门-查询自己的部门和下级部门 （子部门级联）
export const getSysDeptsSelf = params => http.get('/sys_depts/self', params)

// 系统管理-角色管理-查询全部角色
export const getSysRoles = () => http.get('/sys_roles')

// 系统管理-角色管理-分页查询角色
export const getSysRolesPage = params => http.get('/sys_roles/page', params)

// 系统管理-角色管理-新增角色
export const postSysRole = params => http.post('/sys_roles', params)

// 系统管理-角色管理-更新角色
export const putSysRole = params => http.put('/sys_roles', params)

// 系统管理-角色管理-删除角色 多个id用， 逗号分隔
export const deleteSysRole = params => http.delete(`/sys_roles/${ params.ids }`, params)

// 系统管理-角色管理-查询指定角色的全部权限
export const getSysRolePermissions = params => http.get(`/sys_roles/${ params.roleId }/permissions`)

// 系统管理-角色管理-权限分配
export const putSysRolePermissions = params => http.put(`/sys_roles/${ params.roleId }/${params.menuIds}`)

// 系统管理-菜单管理-查询全部菜单树
export const getSysMenusTree = params => http.get('/sys_menus/tree', params)

// 系统管理-菜单管理-查询我的菜单树 仅目录和菜单
export const getSysMenusSelfTree = params => http.get('/sys_menus/self_tree')

// 系统管理-所有按钮权限
export const getSysMenusSelfPermissions = params => http.get('/sys_menus/self_permissions')

// 系统管理-菜单管理-新增菜单
export const postSysMenu = params => http.post('/sys_menus', params)

// 系统管理-菜单管理-更新菜单
export const putSysMenu = params => http.put('/sys_menus', params)

// 系统管理-菜单管理-删除菜单（ 级联删除子菜单）（ 多个id用， 逗号分隔）
export const deleteSysMenu = params => http.delete(`/sys_menus/${ params.ids }`, params)

// 系统管理-菜单管理-拖拽菜单
export const putSysMenusSort = params => http.put('/sys_menus/sort', params)

// 系统管理-数据字典-分页查询全部数据字典
export const getSysDictsPage = params => http.get('/sys_dicts/page', params)

// 系统管理-数据字典-新增
export const postSysDict = params => http.post('/sys_dicts', params)

// 系统管理-数据字典-编辑
export const putSysDict = params => http.put('/sys_dicts', params)

// 系统管理-数据字典-删除
export const deleteSysDict = params => http.delete(`/sys_dicts/${ params.ids }`, params)

// 系统管理-数据字典-数据字典项-根据字典ID查询字典项列表
export const getSysDictItemsById = params => http.get(`/sys_dicts/${ params.sysDictId }/items`, params)

// 系统管理-数据字典-数据字典项-新增字典项
export const postSysDictItem = params => http.post('/sys_dicts/items', params)

// 系统管理-数据字典-数据字典项-更新字典项
export const putSysDictItem = params => http.put('/sys_dicts/items', params)

// 系统管理-数据字典-数据字典项-删除字典项
export const deleteSysDictItem = params => http.delete(`/sys_dicts/items/${ params.ids }`, params)

// 系统管理-数据字典-数据字典项-根据字典标识查询字典项列表
export const getSysDictItemsByCode = params => http.get('/sys_dicts/items', params)
