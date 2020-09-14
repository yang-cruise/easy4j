package cn.easy4j.framework.annotation;

import java.lang.annotation.*;

/**
 * 校验重复提交注解
 * @author yangzongmin
 * @since 2020/6/16 16:01
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckRepeatSubmit {
}
