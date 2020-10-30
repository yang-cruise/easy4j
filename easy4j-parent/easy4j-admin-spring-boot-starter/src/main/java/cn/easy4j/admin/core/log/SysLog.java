package cn.easy4j.admin.core.log;

import java.lang.annotation.*;

/**
 * @author yangzongmin
 * @since 2020/10/23 12:01
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SysLog {
    /**
     * @return 功能模块
     */
    String modular();

    /**
     * 示例：#dto.id
     * @return 被操作实体的ID
     */
    String entityId();

    /**
     * 示例：@sysUserService.selectSysUserById(#dto.id)
     * @return 查询EL表达式，设置此字段后，会在切面中执行两次查询，将前后两次查询结果进行比较，提取出差异字段
     */
    String query() default "";
}
