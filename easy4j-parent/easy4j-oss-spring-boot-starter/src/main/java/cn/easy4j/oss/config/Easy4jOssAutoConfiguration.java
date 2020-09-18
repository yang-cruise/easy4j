package cn.easy4j.oss.config;

import cn.easy4j.oss.config.properties.Easy4jOssProperties;
import cn.easy4j.oss.core.storage.FileStorageStrategy;
import cn.easy4j.oss.core.storage.LocalFileStorageStrategy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云oss自动配置
 *
 * @author ChenYichen
 */
@Configuration
@ComponentScan("cn.easy4j.oss")
public class Easy4jOssAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "easy4j.oss")
    public Easy4jOssProperties ossAliyunProperties() {
        return new Easy4jOssProperties();
    }

    @Bean
    @ConditionalOnMissingBean(FileStorageStrategy.class)
    public FileStorageStrategy fileStorage(Easy4jOssProperties easy4jOssProperties) {
        return new LocalFileStorageStrategy(easy4jOssProperties.getLocal());
    }
}
