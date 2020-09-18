package cn.easy4j.dict.modular.dto;

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
 * @since 2020/2/7
 */
@Setter
@Getter
@ToString
@ApiModel(value = "新增数字字典实体")
public class PostSysDictDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "字典编码不能为空")
    @ApiModelProperty(value = "字典编码")
    private String code;

    @NotBlank(message = "字典名称不能为空")
    @ApiModelProperty(value = "字典名称")
    private String name;

    @NotNull(message = "显示顺序不能为空")
    @ApiModelProperty(value = "显示顺序")
    private Integer sort;

    @ApiModelProperty(value = "描述")
    private String description;
}
