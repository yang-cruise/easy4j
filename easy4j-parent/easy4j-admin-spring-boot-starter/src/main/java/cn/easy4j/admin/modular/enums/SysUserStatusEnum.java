package cn.easy4j.admin.modular.enums;

import lombok.AllArgsConstructor;

/**
 * @author ChenYichen
 */
@AllArgsConstructor
public enum SysUserStatusEnum {
    /** 正常 */
    NORMAL(1, "正常"),
    /** 冻结 */
    LOCKED(2, "冻结");

    private final Integer value;
    private final String desc;

    public Integer getValue() {
        return this.value;
    }

    public String getDesc() {
        return desc;
    }
}
