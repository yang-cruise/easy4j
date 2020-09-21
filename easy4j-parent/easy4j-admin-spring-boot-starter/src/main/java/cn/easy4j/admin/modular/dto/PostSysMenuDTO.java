package cn.easy4j.admin.modular.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author ChenYichen
 * @since 2020/2/6
 */
@Setter
@Getter
@ToString
@ApiModel(value = "新增菜单实体")
public class PostSysMenuDTO {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "菜单名称不能为空")
    @ApiModelProperty(value = "菜单名称", required = true)
    private String name;

    @ApiModelProperty(value = "父菜单ID")
    private Long parentId;

    @NotNull(message = "显示顺序不能为空")
    @ApiModelProperty(value = "显示顺序", required = true)
    private Integer sort;

    @ApiModelProperty(value = "请求地址")
    private String url;

    @ApiModelProperty(value = "菜单类型，D-目录，M-菜单，B-按钮")
    private String type;

    @ApiModelProperty(value = "权限标识")
    private String perms;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "文件路径")
    private String component;

    @ApiModelProperty(value = "是否展示")
    private Boolean visible;

}
