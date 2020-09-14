package cn.easy4j.admin.modular.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

/**
 * @author yangzongmin
 * @since 2020/7/29 17:56
 */
@Setter
@Getter
@ToString
@ApiModel(value = "登录配置实体")
public class LoginConfigVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "启用")
    private Boolean enable;

    @ApiModelProperty(value = "类型")
    private String type;

    @JsonIgnore
    private Map<String, Object> params;

}
