package cn.easy4j.oss.config.properties;

import lombok.Data;

/**
 * oss 阿里云相关配置
 *
 * @author ChenYichen
 */
@Data
public class Easy4jOssProperties {

    private String type;
    private Local local = new Local();
    private Aliyun aliyun = new Aliyun();

    @Data
    public static class Local {
        private String path;
    }

    @Data
    public static class Aliyun {
        private String bucketName;
        private String accessKeyId;
        private String accessKeySecret;
        private String endpoint;
    }

}
