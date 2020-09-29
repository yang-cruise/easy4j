package cn.easy4j.admin.config.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ChenYichen
 */
@Setter
@Getter
public class Easy4jAdminProperties {

    private Swagger swagger = new Swagger();
    private Security security = new Security();

    @Setter
    @Getter
    public static class Swagger {
        private Boolean enable;
        private String host;
    }

    @Setter
    @Getter
    public static class Security {
        private String[] ignoreUrls;
    }
}
