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
 * @since 2020/2/6
 */
@Setter
@Getter
@ToString
@ApiModel(value = "更新数字字典实体")
public class PutSysDictItemDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "字典项ID不能为空")
    @ApiModelProperty(value = "字典项ID")
    private Long id;

    @ApiModelProperty(value = "字典值")
    @NotBlank(message = "字典值不能为空")
    private String key;

    @ApiModelProperty(value = "显示内容")
    @NotBlank(message = "显示内容不能为空")
    private String value;

    @ApiModelProperty(value = "显示排序")
    @NotNull(message = "显示顺序不能为空")
    private Integer sort;
}
