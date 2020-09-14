package cn.easy4j.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 缓存常量
 * @author yangzongmin
 * @since 2020/6/18 17:15
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CacheConstant {
    public static final String DEFAULT = "default";

    public static final String SYS_USER = "sys_user";

    public static final String SYS_DICT = "sys_dict";

    public static final String SYS_CONFIG = "sys_config";

    public static final String CAPTCHA = "captcha";

    public static final String SECURITY = "security";

    public static final String ACCOUNT = "account";

    public static final String CHECK_REPEAT_SUBMIT = "check_repeat_submit";
}
