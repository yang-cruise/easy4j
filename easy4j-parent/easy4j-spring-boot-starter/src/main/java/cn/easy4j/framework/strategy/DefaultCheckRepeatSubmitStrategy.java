package cn.easy4j.framework.strategy;

import cn.easy4j.framework.util.IpUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 默认校验表单是否重复提交策略
 * @author yangzongmin
 * @since 2020/6/18 11:08
 */
public class DefaultCheckRepeatSubmitStrategy implements CheckRepeatSubmitStrategy {

    @Override
    public String getCacheKey(HttpServletRequest request) {
        if (Objects.isNull(request)) {
            return null;
        }

        String str = IpUtil.getIp(request) + request.getRequestURI() + request.getHeader(HttpHeaders.USER_AGENT);
        return DigestUtils.md5DigestAsHex(str.getBytes(StandardCharsets.UTF_8));
    }
}
