package cn.easy4j.admin.modular.dto;

import cn.easy4j.common.constant.RegularConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author ChenYichen
 */
@Setter
@Getter
@ToString
@ApiModel(value = "更新个人信息实体")
public class PutSysUserSelfInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "真实姓名")
    private String realname;

    @ApiModelProperty(value = "手机号码")
    @Pattern(regexp = RegularConstant.MOBILE + RegularConstant.OR + RegularConstant.BLANK, message = "手机号码格式错误")
    private String mobile;

    @ApiModelProperty(value = "邮箱地址")
    @Pattern(regexp = RegularConstant.EMAIL + RegularConstant.OR + RegularConstant.BLANK, message = "邮箱地址格式错误")
    private String email;

    @NotNull(message = "用户性别不能为空")
    @ApiModelProperty(value = "性别 F女 M男", required = true)
    private String sex;

}
