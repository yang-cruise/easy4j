package cn.easy4j.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 正则常量
 *
 * @author ChenYichen
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegularConstant {

    public static final String BLANK = "^$";

    public static final String OR = "|";

    public static final String MOBILE = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9])|(16[6]))\\d{8}$";

    public static final String EMAIL = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
}
