package cn.easy4j.admin.modular.dto;/*
 * @author ChenYichen
 * @since 2020-03-29
 */

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author ChenYichen
 */
@Setter
@Getter
@ToString
@ApiModel(value = "更新菜单排序实体")
public class PutSysMenuSortDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "菜单ID不能为空")
    @ApiModelProperty(value = "菜单ID", required = true)
    private Long menuId;

    @NotNull(message = "序号不能为空")
    @ApiModelProperty(value = "序号", required = true)
    private Integer sort;
}
