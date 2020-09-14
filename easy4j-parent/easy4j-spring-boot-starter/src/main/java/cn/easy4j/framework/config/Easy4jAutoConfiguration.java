package cn.easy4j.framework.config;

import cn.easy4j.framework.db.DbInitializerListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author yangzongmin
 * @date 2019-08-01
 */
@Configuration
@ComponentScan("cn.easy4j.framework")
@PropertySource("classpath:/default-config.properties")
public class Easy4jAutoConfiguration {

    @Bean
    public DbInitializerListener dbInitializerListener() {
        return new DbInitializerListener();
    }

}
