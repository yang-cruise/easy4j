package cn.easy4j.admin.core.db;

import cn.easy4j.admin.modular.entity.SysUserRole;
import cn.easy4j.framework.db.BaseDbInitializer;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 用户与角色关联表初始化脚本
 *
 * @author yangzongmin
 * @since 2019-07-19
 */
@Component
public class SysUserRoleInitializer extends BaseDbInitializer {

    @Override
    protected String getTableInitSql() {
        return "CREATE TABLE `sys_user_role` (" +
                "  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT," +
                "  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
                "  `user_id` bigint(20) NOT NULL COMMENT '用户ID，关联sys_user.id'," +
                "  `role_id` bigint(20) NOT NULL COMMENT '角色ID，关联sys_role.id'," +
                "  PRIMARY KEY (`id`)," +
                "  KEY `idx_user_id` (`user_id`)" +
                ") ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户与角色关联表';";
    }

    @Override
    protected List<String> getDataInitSql() {
        return Arrays.asList("INSERT INTO `sys_user_role`(`id`, `gmt_create`, `gmt_modified`, `user_id`, `role_id`) VALUES (1, '2020-02-22 00:22:11', '2020-02-22 00:22:11', 1, 1);",
                "INSERT INTO `sys_user_role`(`id`, `gmt_create`, `gmt_modified`, `user_id`, `role_id`) VALUES (2, '2020-02-22 00:22:15', '2020-02-22 00:22:15', 2, 2);");
    }

    @Override
    protected String getTableName() {
        return "sys_user_role";
    }

    @Override
    protected Class<?> getEntityClass() {
        return SysUserRole.class;
    }

}
