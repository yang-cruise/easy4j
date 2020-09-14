package cn.easy4j.admin.core.security;

import cn.easy4j.common.constant.OrderedConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.util.ReflectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 权限标识扫描监听器
 * @author yangzongmin
 * @since 2020/6/7 19:25
 */
@Slf4j
@Component
public class ScanPermissionListener implements ApplicationListener<ApplicationReadyEvent>, Ordered {

    @Resource
    private PermissionExpressionResolver permissionExpressionResolver;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Map<String, Object> beans = event.getApplicationContext().getBeansWithAnnotation(Controller.class);

        List<String> expressions = new ArrayList<>();
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            Method[] methods = ReflectionUtils.getAllDeclaredMethods(AopUtils.getTargetClass(entry.getValue()));
            for (Method method : methods) {
                PreAuthorize preAuthorize = AnnotationUtils.findAnnotation(method, PreAuthorize.class);
                if (Objects.nonNull(preAuthorize) && StringUtils.startsWith(preAuthorize.value(), "hasPermission")) {
                    expressions.add(preAuthorize.value());
                }
            }
        }

        int count = permissionExpressionResolver.resolveExpression(expressions);
        log.info("扫描权限注解完成，本次新增录入[{}]条", count);
    }

    @Override
    public int getOrder() {
        return OrderedConstant.SCAN_PERMISSION_CODE_ORDER;
    }

}
