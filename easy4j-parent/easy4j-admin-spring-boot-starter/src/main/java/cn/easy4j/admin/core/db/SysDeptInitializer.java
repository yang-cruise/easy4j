package cn.easy4j.admin.core.db;

import cn.easy4j.admin.modular.entity.SysDept;
import cn.easy4j.framework.db.BaseDbInitializer;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author ChenYichen
 */
@Component
public class SysDeptInitializer extends BaseDbInitializer {

    @Override
    protected String getTableInitSql() {
        return "CREATE TABLE `sys_dept` (" +
                "  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID'," +
                "  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'," +
                "  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'," +
                "  `parent_id` bigint(20) NOT NULL COMMENT '父部门'," +
                "  `dept_name` varchar(50) NOT NULL DEFAULT '' COMMENT '部门名称'," +
                "  `sort` tinyint(3) NOT NULL COMMENT '显示顺序'," +
                "  `description` varchar(200) NOT NULL DEFAULT '' COMMENT '部门描述'," +
                "  PRIMARY KEY (`id`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='部门表';";
    }

    @Override
    protected List<String> getDataInitSql() {
        return Collections.singletonList("INSERT INTO `sys_dept`(`parent_id`, `dept_name`, `sort`, `description`) VALUES (0, '顶级', 1, '顶级部门');");
    }

    @Override
    protected String getTableName() {
        return "sys_dept";
    }

    @Override
    protected Class<?> getEntityClass() {
        return SysDept.class;
    }
}
