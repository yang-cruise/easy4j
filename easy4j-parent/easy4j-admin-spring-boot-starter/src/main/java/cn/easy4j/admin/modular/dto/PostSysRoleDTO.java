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
 * @date 2020/2/5
 */
@Setter
@Getter
@ToString
@ApiModel(value = "新增角色实体")
public class PostSysRoleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "角色名称不能为空")
    @ApiModelProperty(value = "角色名称", required = true)
    private String roleName;

    @NotBlank(message = "角色标识不能为空")
    @ApiModelProperty(value = "角色标识", required = true)
    private String roleCode;

    @ApiModelProperty(value = "角色描述")
    private String description;
}
