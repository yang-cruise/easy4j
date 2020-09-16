package cn.easy4j.admin.modular.entity;

import cn.easy4j.dict.core.jackson.JsonDictConvert;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yangzongmin
 * @date 2019-08-13
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "SysMenu对象")
public class SysMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime gmtModified;

    @ApiModelProperty(value = "菜单名称")
    private String name;

    @ApiModelProperty(value = "父菜单ID")
    private Long parentId;

    @ApiModelProperty(value = "显示顺序")
    private Integer sort;

    @ApiModelProperty(value = "请求地址")
    private String url;

    @JsonDictConvert(code = "sys_menu.type")
    @ApiModelProperty(value = "菜单类型，D-目录，M-菜单，B-按钮")
    private String type;

    @ApiModelProperty(value = "权限标识")
    private String perms;

    @ApiModelProperty(value = "图标")
    private String icon;

    /**
     * 子菜单列表
     */
    @TableField(exist = false, select = false)
    private List<SysMenu> subSysMenuList;
}
