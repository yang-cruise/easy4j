package cn.easy4j.wxmp.core.db;


import cn.easy4j.framework.db.BaseDbInitializer;
import cn.easy4j.wxmp.modular.entity.SysWxMp;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ChenYichen
 * @date 2020年3月23日
 */
@Component
public class SysWxMpInitializer extends BaseDbInitializer {

    @Override
    protected String getTableInitSql() {
        return "CREATE TABLE `sys_wx_mp` (\n" +
                "  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',\n" +
                "  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
                "  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',\n" +
                "  `type` tinyint(3) NOT NULL COMMENT '公众号类型',\n" +
                "  `app_id` char(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '公众号ID',\n" +
                "  `app_secret` char(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '公众号密钥',\n" +
                "  `token` varchar(128) NOT NULL DEFAULT '' COMMENT '令牌',\n" +
                "  `aes_key` varchar(128) NOT NULL DEFAULT '' COMMENT '消息加密密钥',\n" +
                "  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '公众号头像',\n" +
                "  `mp_qrcode` varchar(255) NOT NULL DEFAULT '' COMMENT '公众号二维码',\n" +
                "  PRIMARY KEY (`id`),\n" +
                "  UNIQUE KEY `uk_app_id` (`app_id`)\n" +
                ") ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='微信公众号配置表';";
    }

    @Override
    protected List<String> getDataInitSql() {
        return Lists.newArrayList();
    }

    @Override
    protected String getTableName() {
        return "sys_wx_mp";
    }

    @Override
    protected Class<?> getEntityClass() {
        return SysWxMp.class;
    }
}
