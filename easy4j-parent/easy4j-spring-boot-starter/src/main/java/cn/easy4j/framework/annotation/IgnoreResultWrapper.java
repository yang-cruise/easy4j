package cn.easy4j.framework.annotation;

import cn.easy4j.framework.interceptor.ResultWrapperResponseBodyAdvice;

import java.lang.annotation.*;

/**
 * @see ResultWrapperResponseBodyAdvice
 * @author yangzongmin
 * @since 2020/1/10
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreResultWrapper {
}
