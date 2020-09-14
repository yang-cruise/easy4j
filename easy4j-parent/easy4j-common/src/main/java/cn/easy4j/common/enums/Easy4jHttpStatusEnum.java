package cn.easy4j.common.enums;

/**
 * Result类中code、需要统一管理、实现该该接口覆盖2个方法进行业务的code管理
 * @author ChenYichen
 */
public interface Easy4jHttpStatusEnum {

    /**
     * 获取消息
     *
     * @return 消息
     */
    String msg();

    /**
     * 获取Http状态码
     *
     * @return 错误码
     */
    int code();
}
