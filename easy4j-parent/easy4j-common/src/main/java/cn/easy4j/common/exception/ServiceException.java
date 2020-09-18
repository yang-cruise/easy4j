package cn.easy4j.common.exception;

import cn.easy4j.common.enums.Easy4jHttpStatusEnum;
import cn.easy4j.common.enums.HttpStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 服务异常
 *
 * @author yangzongmin
 * @since 2019-07-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ServiceException extends RuntimeException {

    private final Integer code;
    private final String msg;

    public ServiceException() {
        this(HttpStatusEnum.INTERNAL_SERVER_ERROR.code(), HttpStatusEnum.INTERNAL_SERVER_ERROR.msg());
    }

    public ServiceException(String msg) {
        this(HttpStatusEnum.INTERNAL_SERVER_ERROR.code(), msg);
    }

    public ServiceException(Easy4jHttpStatusEnum httpStatusEnum) {
        this(httpStatusEnum.code(), httpStatusEnum.msg());
    }

    public ServiceException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

}
