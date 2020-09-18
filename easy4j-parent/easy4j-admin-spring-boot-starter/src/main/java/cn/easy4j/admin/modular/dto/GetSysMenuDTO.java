package cn.easy4j.admin.modular.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author ChenYichen
 * @since 2020/2/10
 */
@Setter
@Getter
@ToString
@ApiModel(value = "查询菜单实体")
public class GetSysMenuDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "菜单名称")
    private String name;

    @ApiModelProperty(value = "菜单类型，D-目录，M-菜单，B-按钮")
    private String type;

}
