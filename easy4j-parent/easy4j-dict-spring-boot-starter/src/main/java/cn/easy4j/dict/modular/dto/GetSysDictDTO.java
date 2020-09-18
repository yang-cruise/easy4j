package cn.easy4j.dict.modular.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author ChenYichen
 * @since 2020/2/7
 */
@Setter
@Getter
@ToString
@ApiModel(value = "查询数字字典实体")
public class GetSysDictDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "字典编码")
    private String code;

    @ApiModelProperty(value = "字典名称")
    private String name;
}
