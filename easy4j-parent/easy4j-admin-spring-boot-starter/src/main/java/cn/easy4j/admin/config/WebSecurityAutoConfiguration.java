package cn.easy4j.admin.config;

import cn.easy4j.admin.config.properties.Easy4jAdminProperties;
import cn.easy4j.admin.core.security.Easy4jPermissionEvaluator;
import cn.easy4j.admin.core.security.JwtAuthenticationFilter;
import cn.easy4j.common.enums.HttpStatusEnum;
import cn.easy4j.common.response.FailedResult;
import cn.easy4j.framework.util.JacksonUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.annotation.Resource;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author yangzongmin
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityAutoConfiguration extends WebSecurityConfigurerAdapter {

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private Easy4jAdminProperties easy4jAdminProperties;

    @Resource
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 禁用 csrf, 由于使用的是JWT，我们这里不需要csrf
        http.cors().and().csrf().disable().authorizeRequests()
                // 跨域预检请求
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // 其他所有请求需要身份认证
                .anyRequest().authenticated();
        // 退出登录处理器
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
        // 403异常处理器
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());
        // 401异常处理器
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint());
        // 登录认证过滤器
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        // 禁用Session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }

    @Bean
    public PermissionEvaluator permissionEvaluator() {
        return new Easy4jPermissionEvaluator();
    }

    @Override
    public void configure(WebSecurity web) {
        String[] easy4jIgnoreUrls = new String[]{
                "/sys_files/stream/**", "/sys_files/base64/**",
                "/sys_login/**", "/sys_configs/site_config", "/sys_configs/login_config", "/sys_configs/wechat_config",
                "/swagger-ui.html", "/swagger-resources/**", "/swagger/**", "/**/v2/api-docs", "/webjars/springfox-swagger-ui/**"
        };
        web.ignoring().antMatchers(easy4jIgnoreUrls);

        String[] customIgnoreUrls = easy4jAdminProperties.getSecurity().getIgnoreUrls();
        if (Objects.nonNull(customIgnoreUrls)) {
            web.ignoring().antMatchers(customIgnoreUrls);
        }
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            PrintWriter writer = response.getWriter();
            writer.print(JacksonUtil.toJson(new FailedResult<>(HttpStatusEnum.FORBIDDEN)));
            writer.flush();
        };
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            PrintWriter writer = response.getWriter();
            writer.print(JacksonUtil.toJson(new FailedResult<>(HttpStatusEnum.UNAUTHORIZED)));
            writer.flush();
        };
    }

}
