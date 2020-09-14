package cn.easy4j.framework.strategy;

import javax.servlet.http.HttpServletRequest;

/**
 * 校验表单重复提交策略
 * @author yangzongmin
 * @since 2020/6/18 11:04
 */
public interface CheckRepeatSubmitStrategy {

    /**
     * 获得缓存key
     * @param request HttpServletRequest
     * @return 缓存key
     */
    String getCacheKey(HttpServletRequest request);

}
