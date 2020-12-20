package cn.easy4j.framework.config;

import cn.easy4j.framework.datascope.DataScopeInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis-Plus 配置类，只有配置了spring.datasource.url属性才会执行
 *
 * @author yangzongmin
 * @since 2019-07-19
 */
@Configuration
@MapperScan("cn.easy4j.**.mapper")
@ConditionalOnProperty(prefix = "spring.datasource", name = "url")
public class MyBatisPlusAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    @Bean
    public DataScopeInterceptor dataScopeInterceptor() {
        return new DataScopeInterceptor();
    }
}
