package cn.easy4j.common.enums;

/**
 * http状态码枚举，对应org.springframework.http.HttpStatus类，相当于翻译为中文
 *
 * @author yangzongmin
 * @date 2019-07-19
 */
public enum HttpStatusEnum implements Easy4jHttpStatusEnum {

    /**
     * OK
     */
    OK(200, "请求成功"),
    /**
     * BAD_REQUEST
     */
    BAD_REQUEST(400, "错误的请求"),
    /**
     * UNAUTHORIZED
     */
    UNAUTHORIZED(401, "未授权"),
    /**
     * FORBIDDEN
     */
    FORBIDDEN(403, "无权访问"),
    /**
     * NOT_FOUND
     */
    NOT_FOUND(404, "未找到"),
    /**
     * METHOD_NOT_ALLOWED
     */
    METHOD_NOT_ALLOWED(405, "不支持的请求方法"),
    /**
     * INTERNAL_SERVER_ERROR
     */
    INTERNAL_SERVER_ERROR(500, "网络异常, 请刷新后再试"),
    /**
     * REPEAT_SUBMIT_ERROR
     */
    REPEAT_SUBMIT_ERROR(10000, "重复的请求"),

    /**
     * PARSE_TOKEN_FAIL
     */
    PARSE_TOKEN_FAIL(10005, "认证失败，请重新登录"),

    /**
     * LOGIN_EXPIRE
     */
    LOGIN_EXPIRE(10010, "登录过期、请重新登录"),

    /**
     * PARAM_BAD
     */
    PARAM_BAD(10015, "请求参数错误"),
    /**
     * BIND_REPEAT_PHONE_NUMBER
     */
    BIND_REPEAT_PHONE_NUMBER(10020, "手机号码已经被绑定"),
    /**
     * BAD_CAPTCHA
     */
    BAD_CAPTCHA(10021, "验证码错误或已失效"),
    /**
     * NOT_BIND_ACCOUNT
     */
    NOT_BIND_ACCOUNT(10025, "未绑定账号");

    private final int code;
    private final String msg;

    HttpStatusEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int code() {
        return this.code;
    }

    @Override
    public String msg() {
        return this.msg;
    }
}
