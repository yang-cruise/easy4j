package cn.easy4j.framework.exception;

import cn.easy4j.common.constant.OrderedConstant;
import cn.easy4j.common.enums.HttpStatusEnum;
import cn.easy4j.common.exception.BusinessException;
import cn.easy4j.common.exception.ServiceException;
import cn.easy4j.common.response.FailedResult;
import cn.easy4j.common.response.Result;
import cn.easy4j.common.util.LocalDateUtil;
import cn.easy4j.framework.util.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @author yangzongmin
 * @since 2019-07-19
 */
@Slf4j
@RestControllerAdvice
@Order(value = OrderedConstant.GLOBAL_EXCEPTION_HANDLER_ORDER)
public class GlobalExceptionHandler {

    @InitBinder
    public void initBinderLocalDateTime(WebDataBinder binder) {

        binder.registerCustomEditor(LocalDateTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(LocalDateTime.parse(text, DateTimeFormatter.ofPattern(LocalDateUtil.YYYY_MM_DD_HH_MM_SS)));
            }
        });
    }


    @InitBinder
    public void initBinderLocalDate(WebDataBinder binder) {

        binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(LocalDate.parse(text, DateTimeFormatter.ofPattern(LocalDateUtil.YYYY_MM_DD)));
            }
        });
    }

    @InitBinder
    public void initBinderLocalTime(WebDataBinder binder) {

        binder.registerCustomEditor(LocalTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(LocalTime.parse(text, DateTimeFormatter.ofPattern(LocalDateUtil.HH_MM_SS)));
            }
        });
    }

    @ExceptionHandler(BusinessException.class)
    public Result<Object> handBusinessException(BusinessException e, HttpServletRequest request) {
        log.info("请求路径：{}，请求参数：{}，业务异常：{}", request.getRequestURI(), JacksonUtil.toJson(request.getParameterMap()), e.getMsg());
        return new FailedResult<>(e.getCode(), e.getMsg());
    }

    @ExceptionHandler(ServiceException.class)
    public Result<Object> handServiceException(ServiceException e, HttpServletRequest request) {
        log.info("请求路径：{}，请求参数：{}，服务异常：{}", request.getRequestURI(), JacksonUtil.toJson(request.getParameterMap()), e.getMsg());
        return new FailedResult<>(e.getCode(), e.getMsg());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Result<Object> handIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        log.info("请求路径：{}，请求参数：{}，参数错误：{}", request.getRequestURI(), JacksonUtil.toJson(request.getParameterMap()), e.getMessage());
        return new FailedResult<>(HttpStatusEnum.PARAM_BAD.code(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<Object> handException(Exception e, HttpServletRequest request) {
        log.error("请求路径：{}，请求参数：{}，未知异常：", request.getRequestURI(), JacksonUtil.toJson(request.getParameterMap()), e);
        return new FailedResult<>(HttpStatusEnum.INTERNAL_SERVER_ERROR.code(), HttpStatusEnum.INTERNAL_SERVER_ERROR.msg());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<Object> handException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.warn("请求路径：{}，请求参数：{}，请求类型：{}，不支持的请求方法：{}", request.getRequestURI(), JacksonUtil.toJson(request.getParameterMap()), request.getMethod(), e.getMessage());
        return new FailedResult<>(HttpStatusEnum.METHOD_NOT_ALLOWED.code(), HttpStatusEnum.METHOD_NOT_ALLOWED.msg());
    }

    @ExceptionHandler(BindException.class)
    public Result<Object> handException(BindException e, HttpServletRequest request) {
        log.warn("请求路径：{}，请求参数：{}，请求类型：{}，请求参数错误：{}", request.getRequestURI(), JacksonUtil.toJson(request.getParameterMap()), request.getMethod(), e.getMessage());
        return new FailedResult<>(HttpStatusEnum.PARAM_BAD.code(), e.getAllErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Object> handException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.warn("请求路径：{}，请求参数：{}，请求类型：{}，请求参数错误：{}", request.getRequestURI(), JacksonUtil.toJson(request.getParameterMap()), request.getMethod(), e.getMessage());
        FieldError error = e.getBindingResult().getFieldError();
        return new FailedResult<>(HttpStatusEnum.PARAM_BAD.code(), Objects.isNull(error) ? "参数校验失败" : error.getDefaultMessage());
    }
}
