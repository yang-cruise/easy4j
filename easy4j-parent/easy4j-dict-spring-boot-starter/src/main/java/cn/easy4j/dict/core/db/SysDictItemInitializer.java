package cn.easy4j.dict.core.db;

import cn.easy4j.dict.modular.entity.SysDictItem;
import cn.easy4j.framework.db.BaseDbInitializer;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author yangzongmin
 * @since 2019-08-21
 */
@Component
public class SysDictItemInitializer extends BaseDbInitializer {

    @Override
    protected String getTableInitSql() {
        return "CREATE TABLE `sys_dict_item` (" +
                "  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID'," +
                "  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'," +
                "  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'," +
                "  `dict_id` bigint(20) NOT NULL COMMENT '字典ID，关联sys_dict.id'," +
                "  `key` char(64) NOT NULL COMMENT '字典值'," +
                "  `value` varchar(64) NOT NULL COMMENT '显示内容'," +
                "  `sort` tinyint(4) unsigned NOT NULL COMMENT '显示顺序'," +
                "  `is_deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除，0-未删除，1-已删除'," +
                "  PRIMARY KEY (`id`)," +
                "  KEY `idx_dist_id` (`dict_id`)" +
                ") ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字典数据表';";
    }

    @Override
    protected List<String> getDataInitSql() {
        String sql = "INSERT INTO `sys_dict_item` VALUES (6, '2019-08-21 18:07:00', '2019-08-21 18:07:58', 101, '0', '未删除', 1, 0);" +
                "INSERT INTO `sys_dict_item` VALUES (7, '2019-08-21 18:07:15', '2019-08-21 18:07:24', 101, '1', '已删除', 2, 0);" +
                "INSERT INTO `sys_dict_item` VALUES (3, '2019-08-21 17:58:00', '2019-08-21 18:06:25', 201, '0', '已删除', 1, 0);" +
                "INSERT INTO `sys_dict_item` VALUES (4, '2019-08-21 17:58:19', '2019-08-21 18:06:29', 201, '1', '正常', 2, 0);" +
                "INSERT INTO `sys_dict_item` VALUES (5, '2019-08-21 17:58:34', '2019-08-21 18:06:31', 201, '2', '冻结', 3, 0);" +
                "INSERT INTO `sys_dict_item` VALUES (1, '2019-08-21 17:33:25', '2019-08-21 18:06:39', 202, 'M', '男', 1, 0);" +
                "INSERT INTO `sys_dict_item` VALUES (2, '2019-08-21 17:35:31', '2019-08-21 18:06:40', 202, 'F', '女', 2, 0);" +
                "INSERT INTO `sys_dict_item` VALUES (8, '2020-02-11 14:54:35', '2020-02-11 14:54:35', 203, 'D', '目录', 1, 0);" +
                "INSERT INTO `sys_dict_item` VALUES (9, '2020-02-11 14:54:46', '2020-02-11 14:54:46', 203, 'M', '菜单', 2, 0);" +
                "INSERT INTO `sys_dict_item` VALUES (10, '2020-02-11 14:54:59', '2020-02-11 14:54:59', 203, 'B', '按钮', 3, 0);";
        return Arrays.asList(sql.split(";"));
    }

    @Override
    protected String getTableName() {
        return "sys_dict_item";
    }

    @Override
    protected Class<?> getEntityClass() {
        return SysDictItem.class;
    }
}
