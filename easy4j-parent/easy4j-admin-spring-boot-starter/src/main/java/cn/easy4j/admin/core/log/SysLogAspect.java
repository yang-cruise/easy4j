package cn.easy4j.admin.core.log;

import cn.easy4j.admin.core.util.SecurityUtil;
import cn.easy4j.admin.modular.entity.LoginUser;
import cn.easy4j.admin.modular.entity.SysBusinessLog;
import cn.easy4j.admin.modular.service.SysBusinessLogService;
import cn.easy4j.common.exception.BusinessException;
import cn.easy4j.framework.util.ApplicationUtil;
import cn.easy4j.framework.util.IpUtil;
import cn.easy4j.framework.util.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author yangzongmin
 * @since 2020/10/23 12:52
 */
@Slf4j
@Aspect
@Component
public class SysLogAspect implements BeanFactoryAware {

    private BeanFactory beanFactory;
    private final SpelExpressionParser parser = new SpelExpressionParser();
    private final LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    @Resource
    private SysBusinessLogService sysBusinessLogService;

    @Around("@annotation(cn.easy4j.admin.core.log.SysLog)")
    public Object saveSysBusinessLog(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        SysLog sysLog = method.getAnnotation(SysLog.class);
        SysBusinessLog sysBusinessLog = new SysBusinessLog();
        String entityId = parseSpEL(method, joinPoint.getArgs(), sysLog.entityId(), String.class);
        sysBusinessLog.setModular(sysLog.modular()).setEntityId(entityId);
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = className + "." + method.getName();
        sysBusinessLog.setRequestMethod(methodName);

        // ===================== before ===================
        Object before = parseSpEL(method, joinPoint.getArgs(), sysLog.query(), Object.class);
        log.info("before: {}", JacksonUtil.toJson(before));
        // ===================== before ===================

        HttpServletRequest request = ApplicationUtil.getRequest();
        if (Objects.nonNull(request)) {
            sysBusinessLog.setRequestParams(JacksonUtil.toJson(joinPoint.getArgs()))
                    .setRequestIp(IpUtil.getIp(request))
                    .setRequestUri(request.getRequestURI());
        }

        Object result = joinPoint.proceed();
        sysBusinessLog.setResponseParams(JacksonUtil.toJson(result));
        try {
            LoginUser loginUser = SecurityUtil.getLoginUser();
            sysBusinessLog.setOperatorId(loginUser.getId()).setOperatorName(loginUser.getAccount());
        } catch (BusinessException e) {
            log.info("获取登录用户异常：{}", e.getMsg());
        }

        // ===================== after ===================
        Object end = parseSpEL(method, joinPoint.getArgs(), sysLog.query(), Object.class);
        log.info("after: {}", JacksonUtil.toJson(end));
        // ===================== after ===================

        sysBusinessLogService.save(sysBusinessLog);
        return result;
    }

    private <T> T parseSpEL(Method method, Object[] arguments, String el, Class<T> clazz) {
        String[] params = discoverer.getParameterNames(method);
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setBeanResolver(new BeanFactoryResolver(beanFactory));
        for (int len = 0; len < Objects.requireNonNull(params).length; len++) {
            context.setVariable(params[len], arguments[len]);
        }
        try {
            return parser.parseExpression(el).getValue(context, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void setBeanFactory(@NonNull BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

}
