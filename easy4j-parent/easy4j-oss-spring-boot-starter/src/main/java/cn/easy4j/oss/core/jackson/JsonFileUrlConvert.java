package cn.easy4j.oss.core.jackson;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 文件链接转换标识，在属性上添加此注解，接口响应json序列化时，会增加一个后缀为FileUrl的字段
 *
 * @author yangzongmin
 * @since 2020/9/9 00:11
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = JsonFileUrlConvertSerializer.class)
public @interface JsonFileUrlConvert {

    /**
     * json序列化时新增的字段名称，默认在原字段名后追加FileUrl，例：avatarFileUrl
     * @return 字段名
     */
    String fieldName() default "";

}
