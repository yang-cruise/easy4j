package cn.easy4j.framework.interceptor;

import cn.easy4j.common.enums.HttpStatusEnum;
import cn.easy4j.common.exception.BusinessException;
import cn.easy4j.framework.annotation.CheckRepeatSubmit;
import cn.easy4j.framework.strategy.CheckRepeatSubmitStrategy;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 校验重复提交拦截器
 * @author yangzongmin
 * @since 2020/6/16 15:52
 */
@Component
@SuppressWarnings("NullableProblems")
public class CheckRepeatSubmitInterceptor extends HandlerInterceptorAdapter {

    @Resource(name = "checkRepeatSubmitCache")
    private Cache checkRepeatSubmitCache;

    @Resource
    private CheckRepeatSubmitStrategy checkRepeatSubmitStrategy;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        CheckRepeatSubmit checkRepeatSubmit = ((HandlerMethod) handler).getMethodAnnotation(CheckRepeatSubmit.class);
        if (Objects.nonNull(checkRepeatSubmit) && isRepeatSubmit(request)) {
            throw new BusinessException(HttpStatusEnum.REPEAT_SUBMIT_ERROR);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        if (!(handler instanceof HandlerMethod)) {
            return;
        }

        CheckRepeatSubmit checkRepeatSubmit = ((HandlerMethod) handler).getMethodAnnotation(CheckRepeatSubmit.class);
        if (Objects.isNull(checkRepeatSubmit)) {
            return;
        }

        String key = checkRepeatSubmitStrategy.getCacheKey(request);
        if (StringUtils.isNotBlank(key)) {
            checkRepeatSubmitCache.evict(key);
        }
    }

    private boolean isRepeatSubmit(HttpServletRequest request) {
        String key = checkRepeatSubmitStrategy.getCacheKey(request);
        if (StringUtils.isBlank(key)) {
            return false;
        }

        if (Objects.isNull(checkRepeatSubmitCache.get(key))) {
            checkRepeatSubmitCache.put(key, 1);
            return false;
        }

        return true;
    }
}
