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
 * @date 2020/2/6
 */
@Setter
@Getter
@ToString
@ApiModel(value = "新增数字字典项实体")
public class PostSysDictItemDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "字典ID")
    @NotNull(message = "字典ID不能为空")
    private Long dictId;

    @ApiModelProperty(value = "字典值")
    @NotBlank(message = "字典值不能为空")
    private String key;

    @ApiModelProperty(value = "显示内容")
    @NotBlank(message = "显示内容不能为空")
    private String value;

    @ApiModelProperty(value = "显示排序")
    private Integer sort;
}
