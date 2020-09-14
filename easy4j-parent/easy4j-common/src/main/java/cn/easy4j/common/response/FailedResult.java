package cn.easy4j.common.response;

import cn.easy4j.common.enums.Easy4jHttpStatusEnum;
import cn.easy4j.common.enums.HttpStatusEnum;

/**
 * @author yangzongmin
 * @date 2019-07-19
 */
public class FailedResult<T> extends Result<T> {

    public FailedResult() {
        super(false, HttpStatusEnum.BAD_REQUEST.code(), HttpStatusEnum.BAD_REQUEST.msg(), null);
    }

    public FailedResult(String msg) {
        super(false, HttpStatusEnum.BAD_REQUEST.code(), msg, null);
    }

    public FailedResult(T data) {
        super(false, HttpStatusEnum.BAD_REQUEST.code(), HttpStatusEnum.BAD_REQUEST.msg(), data);
    }

    public FailedResult(Easy4jHttpStatusEnum httpStatusEnum) {
        super(false, httpStatusEnum.code(), httpStatusEnum.msg(), null);
    }

    public FailedResult(Integer code, String msg) {
        super(false, code, msg, null);
    }

    public FailedResult(Integer code, String msg, T data) {
        super(false, code, msg, data);
    }

}
