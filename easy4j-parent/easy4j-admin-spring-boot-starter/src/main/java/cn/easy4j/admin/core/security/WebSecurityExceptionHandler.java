package cn.easy4j.admin.core.security;

import cn.easy4j.common.constant.OrderedConstant;
import cn.easy4j.common.enums.HttpStatusEnum;
import cn.easy4j.common.response.FailedResult;
import cn.easy4j.common.response.Result;
import cn.easy4j.framework.util.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yangzongmin
 * @date 2019/8/28
 */
@Slf4j
@RestControllerAdvice
@Order(value = OrderedConstant.WEB_SECURITY_EXCEPTION_HANDLER_ORDER)
public class WebSecurityExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public Result<Object> handleBadCredentialsException(BadCredentialsException e, HttpServletRequest request) {
        log.info("请求路径：{}，请求参数：{}，未授权异常：{}", request.getRequestURI(), JacksonUtil.toJson(request.getParameterMap()), e.getMessage());
        return new FailedResult<>(HttpStatusEnum.UNAUTHORIZED.code(), e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Result<Object> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        log.info("请求路径：{}，请求参数：{}，无权限异常：{}", request.getRequestURI(), JacksonUtil.toJson(request.getParameterMap()), e.getMessage());
        return new FailedResult<>(HttpStatusEnum.FORBIDDEN.code(), HttpStatusEnum.FORBIDDEN.msg());
    }

}
