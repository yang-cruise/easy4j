package cn.easy4j.framework.config;

import cn.easy4j.common.constant.CacheConstant;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 缓存配置类
 * @author yangzongmin
 * @since 2020/6/16 17:54
 */
@EnableCaching
@Configuration
public class CacheAutoConfiguration {

    @Bean
    public CacheManager cacheManager(List<Cache> caches) {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        simpleCacheManager.setCaches(caches);
        return simpleCacheManager;
    }

    @Bean("defaultCache")
    @ConditionalOnMissingBean(name = "defaultCache")
    public Cache defaultCache() {
        return new CaffeineCache(CacheConstant.DEFAULT, Caffeine.newBuilder().maximumSize(1000L).expireAfterWrite(10L, TimeUnit.MINUTES).build());
    }

    @Bean("checkRepeatSubmitCache")
    @ConditionalOnMissingBean(name = "checkRepeatSubmitCache")
    public Cache checkRepeatSubmitCache() {
        return new CaffeineCache(CacheConstant.CHECK_REPEAT_SUBMIT, Caffeine.newBuilder().expireAfterWrite(30L, TimeUnit.SECONDS).build());
    }

}
