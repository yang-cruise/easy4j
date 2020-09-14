package cn.easy4j.admin.modular.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author yangzongmin
 * @since 2020/7/29 17:56
 */
@Setter
@Getter
@ToString
@ApiModel(value = "网站配置实体")
public class SiteConfigVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "网站名称")
    private String siteName;

    @ApiModelProperty(value = "网站标语")
    private String slogan;

    @ApiModelProperty(value = "网站LOGO")
    private String logo;

    @ApiModelProperty(value = "网站备案号")
    private String beian;

    @ApiModelProperty(value = "版权信息")
    private String copyright;

}
