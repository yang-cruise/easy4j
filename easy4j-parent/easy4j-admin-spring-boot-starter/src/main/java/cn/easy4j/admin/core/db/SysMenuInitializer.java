package cn.easy4j.admin.core.db;

import cn.easy4j.admin.modular.entity.SysMenu;
import cn.easy4j.framework.db.BaseDbInitializer;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author yangzongmin
 * @since 2019-08-13
 */
@Component
public class SysMenuInitializer extends BaseDbInitializer {

    @Override
    protected String getTableInitSql() {
        return "CREATE TABLE `sys_menu` (" +
                "  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID'," +
                "  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'," +
                "  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'," +
                "  `name` varchar(64) NOT NULL COMMENT '菜单名称'," +
                "  `parent_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '父菜单ID'," +
                "  `sort` tinyint(4) unsigned NOT NULL COMMENT '显示顺序'," +
                "  `url` varchar(255) NOT NULL DEFAULT '' COMMENT '请求地址'," +
                "  `type` char(1) NOT NULL COMMENT '菜单类型，D-目录，M-菜单，B-按钮'," +
                "  `perms` varchar(128) NOT NULL DEFAULT '' COMMENT '权限标识'," +
                "  `icon` varchar(64) NOT NULL DEFAULT '' COMMENT '图标'," +
                "  `component` varchar(64) NOT NULL DEFAULT '' COMMENT '前端文件位置',\n" +
                "  `visible` tinyint(3) NOT NULL DEFAULT '1' COMMENT '是否展示',\n" +
                "  PRIMARY KEY (`id`)," +
                "  KEY `idx_sort` (`sort`)" +
                ") ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单表';";
    }

    @Override
    protected List<String> getDataInitSql() {
        return Arrays.asList("INSERT INTO `sys_menu`(`id`, `gmt_create`, `gmt_modified`, `name`, `parent_id`, `sort`, `url`, `type`, `perms`, `icon`) VALUES (1, '2020-02-22 17:57:03', '2020-02-22 17:57:03', '系统管理', 0, 98, '', 'D', '', 'el-icon-setting');",
                "INSERT INTO `sys_menu`(`id`, `gmt_create`, `gmt_modified`, `name`, `parent_id`, `sort`, `url`, `type`, `perms`, `icon`) VALUES (2, '2020-02-22 17:57:23', '2020-02-22 17:57:23', '开发管理', 0, 99, '', 'D', '', 'el-icon-sugar');",
                "INSERT INTO `sys_menu`(`id`, `gmt_create`, `gmt_modified`, `name`, `parent_id`, `sort`, `url`, `type`, `perms`, `icon`) VALUES (3, '2020-02-22 17:58:01', '2020-02-22 17:58:01', '用户管理', 1, 1, '/sys-user', 'M', '', '');",
                "INSERT INTO `sys_menu`(`id`, `gmt_create`, `gmt_modified`, `name`, `parent_id`, `sort`, `url`, `type`, `perms`, `icon`) VALUES (4, '2020-02-22 17:58:24', '2020-02-22 17:59:06', '角色管理', 1, 2, '/sys-role', 'M', '', '');",
                "INSERT INTO `sys_menu`(`id`, `gmt_create`, `gmt_modified`, `name`, `parent_id`, `sort`, `url`, `type`, `perms`, `icon`) VALUES (5, '2020-02-22 17:58:51', '2020-02-22 17:58:51', '菜单管理', 1, 3, '/sys-menu', 'M', '', '');",
                "INSERT INTO `sys_menu`(`id`, `gmt_create`, `gmt_modified`, `name`, `parent_id`, `sort`, `url`, `type`, `perms`, `icon`) VALUES (6, '2020-02-22 17:59:37', '2020-02-22 17:59:37', '数据字典', 1, 4, '/sys-dict', 'M', '', '');",
                "INSERT INTO `sys_menu`(`id`, `gmt_create`, `gmt_modified`, `name`, `parent_id`, `sort`, `url`, `type`, `perms`, `icon`) VALUES (7, '2020-02-22 17:59:37', '2020-02-22 17:59:37', '系统配置', 1, 4, '/sys-config', 'M', '', '');",
                "INSERT INTO `sys_menu`(`id`, `gmt_create`, `gmt_modified`, `name`, `parent_id`, `sort`, `url`, `type`, `perms`, `icon`) VALUES (8, '2020-02-22 18:13:40', '2020-02-22 18:13:40', '表单生成', 2, 1, '/dev-create-form', 'M', '', '');");
    }

    @Override
    protected String getTableName() {
        return "sys_menu";
    }

    @Override
    protected Class<?> getEntityClass() {
        return SysMenu.class;
    }
}
