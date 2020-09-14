package cn.easy4j.admin.modular.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author ChenYichen
 */
@Setter
@Getter
@ToString
@ApiModel(value = "更新用户状态实体")
public class PutSysUserStatusDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "状态不能为空")
    @ApiModelProperty(value = "状态", required = true)
    private Integer status;

}
