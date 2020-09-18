package cn.easy4j.dict.core.db;

import cn.easy4j.dict.modular.entity.SysDict;
import cn.easy4j.framework.db.BaseDbInitializer;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author yangzongmin
 * @since 2019-08-21
 */
@Component
public class SysDictInitializer extends BaseDbInitializer {

    @Override
    protected String getTableInitSql() {
        return "CREATE TABLE `sys_dict` (" +
                "  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID'," +
                "  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'," +
                "  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'," +
                "  `code` char(64)  NOT NULL COMMENT '字典编码'," +
                "  `name` varchar(64)  NOT NULL COMMENT '字典名称'," +
                "  `sort` tinyint(4) unsigned NOT NULL COMMENT '显示顺序'," +
                "  `description` varchar(255)  NOT NULL DEFAULT '' COMMENT '描述'," +
                "  `is_deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除，0-未删除，1-已删除'," +
                "  PRIMARY KEY (`id`)" +
                ") ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字典表';";
    }

    @Override
    protected List<String> getDataInitSql() {
        String sql = "INSERT INTO `sys_dict`(`id`, `gmt_create`, `gmt_modified`, `code`, `name`, `sort`, `description`, `is_deleted`) VALUES (1, '2019-08-21 17:17:53', '2019-08-21 17:25:38', 'sys', '系统配置', 1, '', 0);" +
                "INSERT INTO `sys_dict`(`id`, `gmt_create`, `gmt_modified`, `code`, `name`, `sort`, `description`, `is_deleted`) VALUES (2, '2019-08-26 09:06:10', '2019-08-26 09:06:17', 'common', '公共配置', 2, '', 0);" +
                "INSERT INTO `sys_dict`(`id`, `gmt_create`, `gmt_modified`, `code`, `name`, `sort`, `description`, `is_deleted`) VALUES (3, '2019-08-26 09:04:28', '2019-08-26 09:06:28', 'biz', '业务配置', 3, '', 0);" +
                "INSERT INTO `sys_dict`(`id`, `gmt_create`, `gmt_modified`, `code`, `name`, `sort`, `description`, `is_deleted`) VALUES (21, '2019-08-21 18:05:00', '2019-08-26 09:07:47', 'common.is_deleted', '删除标识', 1, '', 0);" +
                "INSERT INTO `sys_dict`(`id`, `gmt_create`, `gmt_modified`, `code`, `name`, `sort`, `description`, `is_deleted`) VALUES (31, '2019-08-21 17:28:31', '2019-08-26 09:09:13', 'sys_user', '系统用户', 1, '', 0);" +
                "INSERT INTO `sys_dict`(`id`, `gmt_create`, `gmt_modified`, `code`, `name`, `sort`, `description`, `is_deleted`) VALUES (121, '2019-08-21 17:27:52', '2019-08-26 09:03:15', 'sys_user.status', '状态', 1, '', 0);" +
                "INSERT INTO `sys_dict`(`id`, `gmt_create`, `gmt_modified`, `code`, `name`, `sort`, `description`, `is_deleted`) VALUES (122, '2019-08-26 09:03:49', '2019-08-26 09:03:49', 'sys_user.sex', '性别', 2, '', 0);" +
                "INSERT INTO `sys_dict`(`id`, `gmt_create`, `gmt_modified`, `code`, `name`, `sort`, `description`, `is_deleted`) VALUES (1005, '2020-02-11 14:54:06', '2020-02-11 14:54:06', 'sys_menu.type', '菜单类型', 1, '', 0);";
        return Arrays.asList(sql.split(";"));
    }

    @Override
    protected String getTableName() {
        return "sys_dict";
    }

    @Override
    protected Class<?> getEntityClass() {
        return SysDict.class;
    }
}
