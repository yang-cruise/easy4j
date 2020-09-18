package cn.easy4j.admin.modular.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author ChenYichen
 * @since 2020/2/5
 */
@Setter
@Getter
@ToString
@ApiModel(value = "更新系统配置实体")
public class PutSysConfigDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "关键字不能为空")
    @ApiModelProperty(value = "关键字", required = true)
    private String configKey;

    @NotBlank(message = "配置内容不能为空")
    @ApiModelProperty(value = "配置内容", required = true)
    private String configContent;

}
