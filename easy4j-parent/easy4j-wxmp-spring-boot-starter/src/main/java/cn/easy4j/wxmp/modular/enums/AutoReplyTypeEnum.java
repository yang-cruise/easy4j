package cn.easy4j.wxmp.modular.enums;

import lombok.AllArgsConstructor;

/**
 * @author ChenYichen
 * @since 2020/3/24
 */
@AllArgsConstructor
public enum AutoReplyTypeEnum {
    /**
     * 关键字自动回复
     */
    KEYWORD_AUTO_REPLAY(1, "关键字自动回复"),
    /**
     * 首次访问回复
     */
    FIRST_REPLY(2, "首次访问回复"),
    /**
     * 默认回复
     */
    DEFAULT_REPLAY(3, "默认回复");

    Integer key;
    String desc;

    public Integer getKey() {
        return key;
    }
}
