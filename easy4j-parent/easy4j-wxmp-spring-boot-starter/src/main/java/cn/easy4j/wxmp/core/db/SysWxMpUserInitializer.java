package cn.easy4j.wxmp.core.db;


import cn.easy4j.framework.db.BaseDbInitializer;
import cn.easy4j.wxmp.modular.entity.SysWxMpUser;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ChenYichen
 * @since 2020年3月23日
 */
@Component
public class SysWxMpUserInitializer extends BaseDbInitializer {

    @Override
    protected String getTableInitSql() {
        return "CREATE TABLE `sys_wx_mp_user` (" +
                "  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID'," +
                "  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'," +
                "  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'," +
                "  `wx_mp_id` bigint(20) NOT NULL COMMENT '微信公众号ID，关联sys_wx_mp.id'," +
                "  `subscribe` tinyint(1) unsigned DEFAULT '0' COMMENT '是否关注，0-未关注，1-已关注'," +
                "  `open_id` char(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '唯一标识'," +
                "  `union_id` char(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '开放平台唯一标识'," +
                "  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '昵称'," +
                "  `sex` tinyint(3) NOT NULL DEFAULT '0' COMMENT '性别，0-未知，1-男，2-女'," +
                "  `country` varchar(64) NOT NULL DEFAULT '' COMMENT '国家'," +
                "  `province` varchar(64) NOT NULL DEFAULT '' COMMENT '省份'," +
                "  `city` varchar(64) NOT NULL DEFAULT '' COMMENT '城市'," +
                "  `language` varchar(16) NOT NULL DEFAULT '' COMMENT '语言'," +
                "  `head_img_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '头像'," +
                "  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '公众号运营者对粉丝的备注'," +
                "  `group_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户所在的分组ID'," +
                "  `qr_scene` varchar(128) NOT NULL DEFAULT '' COMMENT '二维码扫码场景'," +
                "  `qr_scene_str` varchar(255) NOT NULL DEFAULT '' COMMENT '二维码扫码场景描述'," +
                "  `subscribe_scene` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '用户关注的渠道来源，ADD_SCENE_SEARCH-公众号搜索，ADD_SCENE_ACCOUNT_MIGRATION-公众号迁移，ADD_SCENE_PROFILE_CARD-名片分享，ADD_SCENE_QR_CODE-扫描二维码，ADD_SCENE_PROFILE_ LINK-图文页内名称点击，ADD_SCENE_PROFILE_ITEM-图文页右上角菜单，ADD_SCENE_PAID-支付后关注，ADD_SCENE_OTHERS-其他'," +
                "  `subscribe_time` datetime DEFAULT NULL COMMENT '关注时间'," +
                "  PRIMARY KEY (`id`)," +
                "  UNIQUE KEY `uk_open_id` (`open_id`)" +
                ") ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='微信用户表';";
    }

    @Override
    protected List<String> getDataInitSql() {
        return Lists.newArrayList();
    }

    @Override
    protected String getTableName() {
        return "sys_wx_mp_user";
    }

    @Override
    protected Class<?> getEntityClass() {
        return SysWxMpUser.class;
    }
}
