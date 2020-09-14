package cn.easy4j.admin.modular.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Set;

/**
 * @author yangzongmin
 * @date 2019/8/26
 */
@Setter
@Getter
@ToString
@ApiModel(value = "查询用户实体")
public class GetSysUserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "账号")
    private String account;

    @ApiModelProperty(value = "手机号码")
    private String mobile;

    @ApiModelProperty(value = "邮箱地址")
    private String email;

    @ApiModelProperty(value = "真实姓名")
    private String realname;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "部门ID")
    private Long deptId;

    @ApiModelProperty(value = "部门ID列表")
    private Set<Long> deptIds;

}
