package cn.easy4j.admin.core.db;

import cn.easy4j.admin.modular.entity.SysUser;
import cn.easy4j.framework.db.BaseDbInitializer;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 用户表初始化脚本
 *
 * @author yangzongmin
 * @date 2019-07-19
 */
@Component
public class SysUserInitializer extends BaseDbInitializer {

    @Override
    protected String getTableInitSql() {
        return "CREATE TABLE `sys_user` (" +
                "  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID'," +
                "  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'," +
                "  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'," +
                "  `dept_id` bigint(20) NOT NULL COMMENT '部门ID'," +
                "  `avatar` varchar(255) NOT NULL DEFAULT '' COMMENT '头像'," +
                "  `account` char(64) NOT NULL COMMENT '账号'," +
                "  `password` char(64) NOT NULL COMMENT '密码'," +
                "  `realname` varchar(32) NOT NULL DEFAULT '' COMMENT '真实姓名'," +
                "  `birthday` datetime DEFAULT NULL COMMENT '生日'," +
                "  `sex` char(1) NOT NULL DEFAULT '' COMMENT '性别，字典(sys_common_sex)'," +
                "  `email` varchar(128) NOT NULL DEFAULT '' COMMENT '电子邮件'," +
                "  `mobile` char(16) NOT NULL DEFAULT '' COMMENT '手机号码'," +
                "  `status` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '状态，字典(sys_user_status)'," +
                "  `modify_password` tinyint(3) NOT NULL DEFAULT '0' COMMENT '是否修改初始密码：0 未修改 | 1 已修改'," +
                "  `is_deleted` tinyint(3) NOT NULL DEFAULT '0'," +
                "  PRIMARY KEY (`id`)," +
                "  UNIQUE KEY `uk_account` (`account`)," +
                "  KEY `idx_dept_id` (`dept_id`) USING BTREE" +
                ") ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统管理用户表';";
    }

    @Override
    protected List<String> getDataInitSql() {
        return Arrays.asList("INSERT INTO `sys_user`(`id`, `gmt_create`, `gmt_modified`, `dept_id`,`avatar`, `account`, `password`, `realname`, `birthday`, `sex`, `email`, `mobile`, `status`, `modify_password`, `is_deleted`) VALUES (1, '2019-07-21 19:18:51', '2020-02-22 18:49:16',1 , '', 'admin', '$2a$10$1esoKvsUvvtayD/dfov1zuqaxXZz7KRGeTc4Lp1pkJ7/8jun9Ilfm', '管理员', NULL, 'M', 'test@163.com', '13888888888', 1, 0, 0);",
                "INSERT INTO `sys_user`(`id`, `gmt_create`, `gmt_modified`, `dept_id`, `avatar`, `account`, `password`, `realname`, `birthday`, `sex`, `email`, `mobile`, `status`, `modify_password`, `is_deleted`) VALUES (2, '2019-08-01 11:55:34', '2020-02-22 18:49:17', 1, '', 'test', '$2a$10$dz4YwbH/tAKq3XvzxEOvxuHrykP80uaaTcpJWavEsWhjRczCdVOoK', '测试账号', NULL, 'M', 'test@126.com', '13888888888', 1, 0, 0);");
    }

    @Override
    protected String getTableName() {
        return "sys_user";
    }

    @Override
    protected Class<?> getEntityClass() {
        return SysUser.class;
    }

}
