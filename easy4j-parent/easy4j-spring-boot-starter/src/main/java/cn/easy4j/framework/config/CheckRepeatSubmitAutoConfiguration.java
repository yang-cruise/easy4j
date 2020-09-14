package cn.easy4j.framework.config;

import cn.easy4j.framework.interceptor.CheckRepeatSubmitInterceptor;
import cn.easy4j.framework.strategy.CheckRepeatSubmitStrategy;
import cn.easy4j.framework.strategy.DefaultCheckRepeatSubmitStrategy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * 校验重复提交配置类
 * @author yangzongmin
 * @since 2020/6/18 12:53
 */
@Configuration
public class CheckRepeatSubmitAutoConfiguration implements WebMvcConfigurer {

    @Resource
    private CheckRepeatSubmitInterceptor checkRepeatSubmitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(checkRepeatSubmitInterceptor).addPathPatterns("/**").excludePathPatterns("/webjars");
    }

    @Bean
    @ConditionalOnMissingBean(CheckRepeatSubmitStrategy.class)
    public CheckRepeatSubmitStrategy checkRepeatSubmitStrategy() {
        return new DefaultCheckRepeatSubmitStrategy();
    }

}
