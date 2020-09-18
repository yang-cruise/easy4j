package cn.easy4j.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * AOP拦截器执行顺序常量，值越小，越先执行
 *
 * @author yangzongmin
 * @since 2019-07-19
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OrderedConstant {
    /** Spring Security异常处理拦截器 */
    public static final int WEB_SECURITY_EXCEPTION_HANDLER_ORDER = 199;

    /** 全局异常处理拦截器 */
    public static final int GLOBAL_EXCEPTION_HANDLER_ORDER = 200;

    /** 响应结果包装拦截器 */
    public static final int RESULT_WRAPPER_ORDER = 299;

    /** 初始化数据库脚本 */
    public static final int INIT_DB_ORDER = 1000;

    /** 扫描权限标识入库 */
    public static final int SCAN_PERMISSION_CODE_ORDER = 1001;
}
