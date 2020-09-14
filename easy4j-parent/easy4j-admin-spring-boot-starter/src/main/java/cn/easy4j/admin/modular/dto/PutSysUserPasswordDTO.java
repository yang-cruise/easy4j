package cn.easy4j.admin.modular.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author ChenYichen
 */
@Setter
@Getter
@ToString
@ApiModel(value = "更新用户密码实体")
public class PutSysUserPasswordDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6~20位之间")
    @ApiModelProperty(value = "新密码", required = true)
    private String newPassword;

}
