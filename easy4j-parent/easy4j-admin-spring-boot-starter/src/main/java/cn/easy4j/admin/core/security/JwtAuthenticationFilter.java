package cn.easy4j.admin.core.security;


import cn.easy4j.admin.modular.entity.LoginUser;
import cn.easy4j.admin.modular.service.SysUserCacheService;
import cn.easy4j.common.enums.Easy4jHttpStatusEnum;
import cn.easy4j.common.enums.HttpStatusEnum;
import cn.easy4j.common.response.FailedResult;
import cn.easy4j.framework.util.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

/**
 * @author ChenYichen
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Resource
    private SysUserCacheService sysUserCacheService;

    @SuppressWarnings("NullableProblems")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.debug("收到请求: method = {}, URI = {}, params = {}", request.getMethod(), request.getRequestURI(), JacksonUtil.toJson(request.getParameterMap()));

        try {
            String token = JwtUtil.getToken(request);
            if (StringUtils.isNotBlank(token)) {
                Map<String, String> claims = JwtUtil.verifyToken(token);
                Long userId = Long.valueOf(claims.get("sub"));
                LoginUser loginUser = sysUserCacheService.getLoginUserByUserId(userId);
                if (Objects.nonNull(loginUser)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (CredentialsExpiredException e) {
            returnJson(response, HttpStatusEnum.LOGIN_EXPIRE);
            return;
        } catch (PreAuthenticatedCredentialsNotFoundException e) {
            returnJson(response, HttpStatusEnum.PARSE_TOKEN_FAIL);
            return;
        } catch (Exception e) {
            log.error("认证异常：{}", e.getMessage());
            returnJson(response, HttpStatusEnum.UNAUTHORIZED);
            return;
        }
        chain.doFilter(request, response);
    }

    private void returnJson(HttpServletResponse response, Easy4jHttpStatusEnum easy4jHttpStatusEnum) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        try (PrintWriter writer = response.getWriter()) {
            writer.print(JacksonUtil.toJson(new FailedResult<>(easy4jHttpStatusEnum)));
            writer.flush();
        } catch (IOException e) {
            log.error("拦截器异常：", e);
        }
    }
}
