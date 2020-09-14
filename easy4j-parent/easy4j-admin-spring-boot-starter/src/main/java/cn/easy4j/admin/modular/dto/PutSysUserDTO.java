package cn.easy4j.admin.modular.dto;

import cn.easy4j.common.constant.RegularConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

/**
 * @author ChenYichen
 */
@Setter
@Getter
@ToString
@ApiModel(value = "更新用户实体")
public class PutSysUserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "主键ID不能为空")
    @ApiModelProperty(value = "用户ID", required = true)
    private Long id;

    @ApiModelProperty(value = "真实姓名")
    private String realname;

    @Pattern(regexp = RegularConstant.MOBILE + RegularConstant.OR + RegularConstant.BLANK, message = "手机号码格式错误")
    @ApiModelProperty(value = "手机号码")
    private String mobile;

    @Pattern(regexp = RegularConstant.EMAIL + RegularConstant.OR + RegularConstant.BLANK, message = "邮箱地址格式错误")
    @ApiModelProperty(value = "邮箱地址")
    private String email;

    @ApiModelProperty(value = "出生日期")
    private LocalDate birthday;

    @NotNull(message = "用户性别不能为空")
    @ApiModelProperty(value = "性别 F女 M男", required = true)
    private String sex;

    @ApiModelProperty(value = "角色IDS")
    private Set<Long> roleIds;

    @NotNull(message = "请选择部门")
    @ApiModelProperty(value = "部门ID", required = true)
    private Long deptId;
}
