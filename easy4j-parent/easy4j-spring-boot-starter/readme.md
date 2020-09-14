# Easy4j

# Code规范

## 代码层面
1. 日期类型全部使用LocalDate或者LocalDateTime
2. 权限标识规则：
    - 菜单显示：view结尾
    - 增：insert结尾
    - 删：delete结尾
    - 查：select结尾
    - 改：update结尾
```text
sys:user:view		/sysUser

	查询用户  sys:user:select 
	新增用户  sys:user:insert 
	修改用户  sys:user:update
	删除用户  sys:user:delete
```

## 文档层面
1. 如果需要对全局的code码进行扩展、实现Easy4jHttpStatusEnum类、重写code()和msg()
