package cn.easy4j.admin.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * @author ChenYichen
 * @date 2020/2/23
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ConditionalOnProperty(prefix = "easy4j.admin.security", value = "enable", havingValue = "true", matchIfMissing = true)
public class GlobalMethodSecurityAutoConfiguration {
}
