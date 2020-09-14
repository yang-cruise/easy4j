package cn.easy4j.admin.config;

import cn.easy4j.admin.config.properties.Easy4jAdminProperties;
import cn.easy4j.admin.core.strategy.AdminCheckRepeatSubmitStrategy;
import cn.easy4j.common.constant.CacheConstant;
import cn.easy4j.framework.strategy.CheckRepeatSubmitStrategy;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author ChenYichen
 */
@Configuration
@ComponentScan("cn.easy4j.admin")
public class Easy4jAdminAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "easy4j.admin")
    public Easy4jAdminProperties easy4jAdminProperties () {
        return new Easy4jAdminProperties();
    }

    @Bean
    public CheckRepeatSubmitStrategy checkRepeatSubmitStrategy() {
        return new AdminCheckRepeatSubmitStrategy();
    }

    @Bean("sysUserCache")
    @ConditionalOnMissingBean(name = "sysUserCache")
    public Cache sysUserCache() {
        return new CaffeineCache(CacheConstant.SYS_USER, Caffeine.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).build());
    }

    @Bean("sysConfigCache")
    @ConditionalOnMissingBean(name = "sysConfigCache")
    public Cache sysConfigCache() {
        return new CaffeineCache(CacheConstant.SYS_CONFIG, Caffeine.newBuilder().expireAfterWrite(30, TimeUnit.MINUTES).build());
    }

    @Bean("captchaCache")
    @ConditionalOnMissingBean(name = "captchaCache")
    public Cache captchaCache() {
        return new CaffeineCache(CacheConstant.CAPTCHA, Caffeine.newBuilder().expireAfterWrite(3, TimeUnit.MINUTES).build());
    }

    @Bean("accountCache")
    @ConditionalOnMissingBean(name = "accountCache")
    public Cache accountCache() {
        return new CaffeineCache(CacheConstant.ACCOUNT, Caffeine.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).build());
    }

    @Bean("securityCache")
    @ConditionalOnMissingBean(name = "securityCache")
    public Cache securityCache() {
        return new CaffeineCache(CacheConstant.SECURITY, Caffeine.newBuilder().expireAfterAccess(10, TimeUnit.MINUTES).build());
    }

}
