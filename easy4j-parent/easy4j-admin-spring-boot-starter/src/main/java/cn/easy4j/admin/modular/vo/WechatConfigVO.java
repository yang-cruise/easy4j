package cn.easy4j.admin.modular.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author yangzongmin
 * @since 2020/7/30 15:24
 */
@Setter
@Getter
@ToString
@ApiModel(value = "微信配置实体")
public class WechatConfigVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "appId")
    private String appId;

    @JsonIgnore
    private String appSecret;

    @ApiModelProperty(value = "redirectUri")
    private String redirectUri;

}
