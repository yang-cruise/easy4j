package cn.easy4j.admin.modular.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author yangzongmin
 * @date 2020/8/14 14:29
 */
@Setter
@Getter
@ToString
@ApiModel(value = "查询用户实体")
public class GetTokenByAccountDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "账号不能为空")
    @ApiModelProperty(value = "账号", required = true)
    private String account;

    @NotNull(message = "密码不能为空")
    @ApiModelProperty(value = "密码", required = true)
    private String password;

    @ApiModelProperty(value = "验证码")
    private String captcha;

    @ApiModelProperty(value = "请求ID，提交验证码时必须")
    private String uuid;

    @ApiModelProperty(value = "绑定微信openId")
    private String openId;

}
