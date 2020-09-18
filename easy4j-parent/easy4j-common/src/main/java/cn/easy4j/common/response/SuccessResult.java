package cn.easy4j.common.response;

import cn.easy4j.common.enums.Easy4jHttpStatusEnum;
import cn.easy4j.common.enums.HttpStatusEnum;

/**
 * @author yangzongmin
 * @since 2019-07-19
 */
public class SuccessResult<T> extends Result<T> {

    public SuccessResult() {
        super(true, HttpStatusEnum.OK.code(), HttpStatusEnum.OK.msg(), null);
    }

    public SuccessResult(T data) {
        super(true, HttpStatusEnum.OK.code(), HttpStatusEnum.OK.msg(), data);
    }

    public SuccessResult(String msg, T data) {
        super(true, HttpStatusEnum.OK.code(), msg, data);
    }

    public SuccessResult(Easy4jHttpStatusEnum statusEnum, T data) {
        super(true, statusEnum.code(), statusEnum.msg(), data);
    }
}
