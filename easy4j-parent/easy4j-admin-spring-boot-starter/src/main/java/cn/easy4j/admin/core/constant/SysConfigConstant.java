package cn.easy4j.admin.core.constant;

/**
 * 系统配置常量
 * @author yangzongmin
 * @since 2020/8/12 11:01
 */
public final class SysConfigConstant {

    private SysConfigConstant() {}

    public static final class Key {
        private Key() {}
        public static final String SITE_CONFIG = "SITE_CONFIG";
        public static final String LOGIN_CONFIG = "LOGIN_CONFIG";
    }

    public static final class LoginType {
        private LoginType() {}
        public static final String SMS = "sms";
        public static final String WECHAT = "wechat";
        public static final String ACCOUNT = "account";
    }
}
