package cn.easy4j.admin.core.db;

import cn.easy4j.admin.modular.entity.SysRole;
import cn.easy4j.framework.db.BaseDbInitializer;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 角色表初始化脚本
 *
 * @author yangzongmin
 * @date 2019-07-19
 */
@Component
public class SysRoleInitializer extends BaseDbInitializer {

    @Override
    protected String getTableInitSql() {
        return "CREATE TABLE `sys_role` (" +
                "  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT," +
                "  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
                "  `role_name` char(32) NOT NULL COMMENT '角色名称，例：管理员'," +
                "  `role_code` char(32) NOT NULL COMMENT '角色标识，例：admin'," +
                "  `description` varchar(255) NOT NULL DEFAULT '' COMMENT '描述'," +
                "  PRIMARY KEY (`id`)" +
                ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色表';";
    }

    @Override
    protected List<String> getDataInitSql() {
        return Arrays.asList("INSERT INTO `sys_role`(`role_name`, `role_code`, `description`) VALUES ('管理员', 'administrator', '');",
                "INSERT INTO `sys_role`(`role_name`, `role_code`, `description`) VALUES ('测试人员', 'tester', '');");
    }

    @Override
    protected String getTableName() {
        return "sys_role";
    }

    @Override
    protected Class<?> getEntityClass() {
        return SysRole.class;
    }

}
