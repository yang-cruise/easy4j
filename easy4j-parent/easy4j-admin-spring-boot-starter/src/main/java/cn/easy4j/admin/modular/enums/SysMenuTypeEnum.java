package cn.easy4j.admin.modular.enums;

import lombok.AllArgsConstructor;

/**
 * @author ChenYichen
 */
@AllArgsConstructor
public enum SysMenuTypeEnum {
    /** 目录*/
    D("D", "目录"),
    /** 菜单*/
    M("M", "菜单"),
    /** 按钮*/
    B("B", "按钮");

    private final String value;
    private final String desc;

    public String getValue() {
        return this.value;
    }

    public String getDesc() {
        return desc;
    }
}
