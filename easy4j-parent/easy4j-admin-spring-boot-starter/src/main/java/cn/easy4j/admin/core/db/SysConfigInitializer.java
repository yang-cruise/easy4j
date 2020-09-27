package cn.easy4j.admin.core.db;

import cn.easy4j.admin.modular.entity.SysConfig;
import cn.easy4j.framework.db.BaseDbInitializer;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 系统配置
 *
 * @author ChenYichen
 */
@Component
public class SysConfigInitializer extends BaseDbInitializer {

    @Override
    protected String getTableInitSql() {
        return "CREATE TABLE `sys_config` (" +
                "  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID'," +
                "  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'," +
                "  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'," +
                "  `config_key` varchar(64) CHARACTER SET utf8 NOT NULL COMMENT '关键字'," +
                "  `config_content` json NOT NULL COMMENT '配置内容'," +
                "  PRIMARY KEY (`id`)," +
                "  UNIQUE KEY `uk_config_key` (`config_key`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统配置表';";
    }

    @Override
    protected List<String> getDataInitSql() {
        return Arrays.asList("INSERT INTO `sys_config`(`config_key`, `config_content`) VALUES ('SITE_CONFIG', '{\"logo\": \"https://element.eleme.cn/favicon.ico\", \"beian\": \"粤ICP备12013459号-5\", \"slogan\": \"简单，美！\", \"siteName\": \"Easy4j\", \"copyright\": \"© 版权所有 2020-2021 easy4j.cn\"}');",
                "INSERT INTO `sys_config`(`config_key`, `config_content`) VALUES ('LOGIN_CONFIG', '[{\"name\": \"密码登录\", \"sort\": 1, \"type\": \"account\", \"enable\": true, \"params\": {\"lockAccountCount\": 5, \"showCaptchaCount\": 3}}, {\"name\": \"短信登录\", \"sort\": 2, \"type\": \"sms\", \"enable\": false, \"params\": {\"appKey\": \"\", \"appSecret\": \"\"}}, {\"name\": \"微信登录\", \"sort\": 3, \"type\": \"wechat\", \"enable\": false, \"params\": {\"appId\": \"\", \"appSecret\": \"\", \"redirectUri\": \"\"}}]');");
    }

    @Override
    protected String getTableName() {
        return "sys_config";
    }

    @Override
    protected Class<?> getEntityClass() {
        return SysConfig.class;
    }

}
