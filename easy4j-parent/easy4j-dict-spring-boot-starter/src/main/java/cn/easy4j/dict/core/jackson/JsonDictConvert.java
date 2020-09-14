package cn.easy4j.dict.core.jackson;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数字字典转换标识，在属性上添加此注解，接口响应json序列化时，会增加一个后缀为Text的字段
 * @author yangzongmin
 * @since 2020/6/2 16:53
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = JsonDictConvertSerializer.class)
public @interface JsonDictConvert {

    /**
     * 字典编码，对应 sys_dict.code
     */
    String code();

    /**
     * json序列化时新增的字段名称，默认在原字段名后追加Text，例：orderStatusText
     */
    String fieldName() default "";

}
