package cn.easy4j.oss.core.db;


import cn.easy4j.framework.db.BaseDbInitializer;
import cn.easy4j.oss.modular.entity.SysFile;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 文件信息表的初始化程序
 *
 * @author ChenYichen
 */
@Component
public class SysFileInitializer extends BaseDbInitializer {

    @Override
    protected String getTableInitSql() {
        return "CREATE TABLE `sys_file` (" +
                "  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID'," +
                "  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'," +
                "  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'," +
                "  `file_origin_name` varchar(255) NOT NULL DEFAULT '' COMMENT '文件名称'," +
                "  `file_suffix` varchar(64) NOT NULL DEFAULT '' COMMENT '文件后缀'," +
                "  `file_size` decimal(10,0) NOT NULL DEFAULT '0' COMMENT '文件大小'," +
                "  `file_storage_name` varchar(255) NOT NULL DEFAULT '' COMMENT '文件唯一名称'," +
                "  PRIMARY KEY (`id`)," +
                "  UNIQUE KEY `uk_file_storage_name` (`file_storage_name`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文件表';";
    }

    @Override
    protected List<String> getDataInitSql() {
        return Collections.emptyList();
    }

    @Override
    public String getTableName() {
        return "sys_file";
    }

    @Override
    public Class<?> getEntityClass() {
        return SysFile.class;
    }
}
