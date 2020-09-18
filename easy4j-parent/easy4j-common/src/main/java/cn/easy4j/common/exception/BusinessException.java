package cn.easy4j.common.exception;

import cn.easy4j.common.enums.Easy4jHttpStatusEnum;
import cn.easy4j.common.enums.HttpStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务异常
 *
 * @author yangzongmin
 * @since 2019-07-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException {

    private final Integer code;
    private final String msg;

    public BusinessException() {
        this(HttpStatusEnum.BAD_REQUEST.code(), HttpStatusEnum.BAD_REQUEST.msg());
    }

    public BusinessException(String msg) {
        this(HttpStatusEnum.BAD_REQUEST.code(), msg);
    }

    public BusinessException(Easy4jHttpStatusEnum httpStatusEnum) {
        this(httpStatusEnum.code(), httpStatusEnum.msg());
    }

    public BusinessException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

}
