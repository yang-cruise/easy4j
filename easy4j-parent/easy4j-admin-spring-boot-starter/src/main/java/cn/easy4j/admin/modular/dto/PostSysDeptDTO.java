package cn.easy4j.admin.modular.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author ChenYichen
 * @since 2020/2/10
 */
@Setter
@Getter
@ToString
@ApiModel(value = "新增部门实体")
public class PostSysDeptDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "父部门不能为空")
    @ApiModelProperty(value = "父部门", required = true)
    private Long parentId;

    @NotBlank(message = "部门名称不能为空")
    @Size(max = 10, message = "部门名称必须在10个字符之内")
    @ApiModelProperty(value = "部门名称", required = true)
    private String deptName;

    @NotNull(message = "显示顺序不能为空")
    @ApiModelProperty(value = "显示顺序", required = true)
    private Integer sort;

    @ApiModelProperty(value = "部门描述")
    private String description;
}
