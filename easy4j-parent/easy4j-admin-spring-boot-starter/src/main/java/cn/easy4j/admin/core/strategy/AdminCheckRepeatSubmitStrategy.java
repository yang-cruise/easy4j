package cn.easy4j.admin.core.strategy;

import cn.easy4j.admin.modular.entity.LoginUser;
import cn.easy4j.framework.strategy.CheckRepeatSubmitStrategy;
import cn.easy4j.framework.util.IpUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * admin模块自定义校验重复提交策略
 * @author yangzongmin
 * @since 2020/6/18 15:17
 */
public class AdminCheckRepeatSubmitStrategy implements CheckRepeatSubmitStrategy {

    @Override
    public String getCacheKey(HttpServletRequest request) {
        if (Objects.isNull(request)) {
            return null;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication)) {
            String str = IpUtil.getIp(request) + request.getRequestURI() + request.getHeader(HttpHeaders.USER_AGENT);
            return DigestUtils.md5DigestAsHex(str.getBytes(StandardCharsets.UTF_8));
        }

        return ((LoginUser) authentication.getPrincipal()).getId() + request.getRequestURI();
    }
}
