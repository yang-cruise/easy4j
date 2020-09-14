package cn.easy4j.admin.core.db;

import cn.easy4j.admin.modular.entity.SysWxOpenUser;
import cn.easy4j.framework.db.BaseDbInitializer;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 微信授权登录表初始化脚本
 *
 * @author yangzongmin
 * @since 2020/7/30 22:45
 */
@Component
public class SysWxOpenUserInitializer extends BaseDbInitializer {

    @Override
    protected String getTableInitSql() {
        return "CREATE TABLE `sys_wx_open_user` (" +
                "  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID'," +
                "  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'," +
                "  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'," +
                "  `user_id` bigint(20) NOT NULL COMMENT '用户ID，关联sys_user.id'," +
                "  `open_id` char(64) NOT NULL COMMENT '唯一标识'," +
                "  `union_id` char(64) NOT NULL DEFAULT '' COMMENT '开放平台唯一标识'," +
                "  `nickname` varchar(255) NOT NULL DEFAULT '' COMMENT '昵称'," +
                "  `sex` tinyint(3) NOT NULL DEFAULT '0' COMMENT '性别，0-未知，1-男，2-女'," +
                "  `country` varchar(64) NOT NULL DEFAULT '' COMMENT '国家'," +
                "  `province` varchar(64) NOT NULL DEFAULT '' COMMENT '省份'," +
                "  `city` varchar(64) NOT NULL DEFAULT '' COMMENT '城市'," +
                "  `language` varchar(16) NOT NULL DEFAULT '' COMMENT '语言'," +
                "  `head_img_url` varchar(255) NOT NULL DEFAULT '' COMMENT '头像'," +
                "  PRIMARY KEY (`id`)," +
                "  UNIQUE KEY `uk_open_id` (`open_id`)," +
                "  UNIQUE KEY `uk_union_id` (`union_id`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='微信授权登录用户表';";
    }

    @Override
    protected List<String> getDataInitSql() {
        return Collections.emptyList();
    }

    @Override
    protected String getTableName() {
        return "sys_wx_open_user";
    }

    @Override
    protected Class<?> getEntityClass() {
        return SysWxOpenUser.class;
    }

}
