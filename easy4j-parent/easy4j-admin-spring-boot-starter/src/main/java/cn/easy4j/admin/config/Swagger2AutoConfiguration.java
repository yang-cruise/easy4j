package cn.easy4j.admin.config;

import cn.easy4j.admin.config.properties.Easy4jAdminProperties;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @author ChenYichen
 */
@Configuration
@EnableSwagger2
@ConditionalOnProperty(prefix = "easy4j.admin.swagger", value = {"enable"}, havingValue = "true")
public class Swagger2AutoConfiguration {

    private static final String AUTHORIZATION = "Authorization";

    @Resource
    private Environment environment;

    @Resource
    private Easy4jAdminProperties easy4jAdminProperties;

    @Bean
    public Docket clientDocket() {
        String port = environment.getProperty("server.port");
        Easy4jAdminProperties.Swagger swagger = easy4jAdminProperties.getSwagger();
        String host = StringUtils.isNotBlank(swagger.getHost()) ? swagger.getHost() : "127.0.0.1:" + port;
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(clientApiInfo())
                .host(host)
                .groupName("easy4j-admin模块接口文档")
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(security())
                .securityContexts(securityContexts());
    }

    private List<ApiKey> security() {
        return Collections.singletonList(new ApiKey(AUTHORIZATION, AUTHORIZATION, "header"));
    }

    private List<SecurityContext> securityContexts() {
        return Collections.singletonList(SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.regex("^(?!auth).*$")).build());
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope[] scopes = new AuthorizationScope[]{new AuthorizationScope("global", "accessEverything")};
        return Collections.singletonList(new SecurityReference(AUTHORIZATION, scopes));
    }

    private ApiInfo clientApiInfo() {
        return new ApiInfoBuilder()
                .title("easy4j-admin模块接口文档")
                .description("所有响应会默认加上ResultWrapper，参考cn.easy4j.framework.interceptor.ResultWrapperResponseBodyAdvice")
                .version("0.5.0")
                .build();
    }
}
