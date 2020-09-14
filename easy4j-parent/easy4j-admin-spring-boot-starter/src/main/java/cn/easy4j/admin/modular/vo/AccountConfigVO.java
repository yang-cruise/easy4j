package cn.easy4j.admin.modular.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author yangzongmin
 * @since 2020/8/14 16:00
 */
@Setter
@Getter
@ToString
@ApiModel(value = "密码登录配置实体")
public class AccountConfigVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "登录{0}次锁定账户")
    private Integer lockAccountCount;

    @ApiModelProperty(value = "密码错误{0}次出现验证码")
    private Integer showCaptchaCount;

}
