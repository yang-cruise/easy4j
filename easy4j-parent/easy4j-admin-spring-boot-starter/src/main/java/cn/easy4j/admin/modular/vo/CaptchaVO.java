package cn.easy4j.admin.modular.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author yangzongmin
 * @since 2020/7/30 15:24
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
@ApiModel(value = "验证码实体")
public class CaptchaVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "是否显示")
    private Boolean show;

    @ApiModelProperty(value = "请求ID")
    private String uuid;

    @ApiModelProperty(value = "base64图片")
    private String image;
}
