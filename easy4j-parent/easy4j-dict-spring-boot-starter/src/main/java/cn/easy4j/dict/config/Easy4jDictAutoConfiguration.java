package cn.easy4j.dict.config;

import cn.easy4j.common.constant.CacheConstant;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * 数据字典模块自动配置类
 * @author yangzongmin
 * @since 2020/6/18 14:40
 */
@Configuration
@ComponentScan("cn.easy4j.dict")
public class Easy4jDictAutoConfiguration {

    @Bean("sysDictCache")
    @ConditionalOnMissingBean(name = "sysDictCache")
    public Cache sysDictCache() {
        return new CaffeineCache(CacheConstant.SYS_DICT, Caffeine.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).build());
    }
}
