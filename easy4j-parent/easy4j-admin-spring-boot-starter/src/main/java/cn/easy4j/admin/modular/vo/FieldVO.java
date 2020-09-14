package cn.easy4j.admin.modular.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author ChenYichen
 */
@Setter
@Getter
@ToString
@ApiModel(value = "字段实体")
public class FieldVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "字段名称")
    private String fieldName;

    @ApiModelProperty(value = "字段类型")
    private String fieldType;

}
