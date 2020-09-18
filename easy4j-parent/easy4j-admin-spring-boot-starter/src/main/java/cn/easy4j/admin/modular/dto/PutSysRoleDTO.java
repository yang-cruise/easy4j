package cn.easy4j.admin.modular.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author ChenYichen
 * @since 2020/2/5
 */
@Setter
@Getter
@ToString
@ApiModel(value = "更新角色实体")
public class PutSysRoleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "角色主键ID不能为空")
    @ApiModelProperty(value = "主键ID", required = true)
    private Long id;

    @NotBlank(message = "角色名称不能为空")
    @ApiModelProperty(value = "角色名称", required = true)
    private String roleName;

    @NotBlank(message = "角色标识不能为空")
    @ApiModelProperty(value = "角色标识", required = true)
    private String roleCode;

    @ApiModelProperty(value = "角色描述")
    private String description;
}
