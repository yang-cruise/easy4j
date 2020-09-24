package cn.easy4j.admin.modular.vo;

import cn.easy4j.admin.modular.entity.SysDept;
import cn.easy4j.admin.modular.entity.SysRole;
import cn.easy4j.dict.core.jackson.JsonDictConvert;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author ChenYichen
 */
@Setter
@Getter
@ToString
@ApiModel(value = "用户实体")
public class SysUserVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModified;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "账户")
    private String account;

    @JsonIgnore
    private String password;

    @ApiModelProperty(value = "部门ID")
    private Long deptId;

    @ApiModelProperty(value = "真实姓名")
    private String realname;

    @ApiModelProperty(value = "生日")
    private LocalDate birthday;

    @JsonDictConvert(code = "sys_user.sex")
    @ApiModelProperty(value = "性别 1男  2女")
    private String sex;

    @ApiModelProperty(value = "邮箱地址")
    private String email;

    @ApiModelProperty(value = "电话号码")
    private String mobile;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "是否修改初始密码：0 未修改 | 1 已修改")
    private Integer modifyPassword;

    @ApiModelProperty(value = "角色列表")
    private Set<SysRole> roles;

    @ApiModelProperty(value = "部门")
    private SysDept dept;
}
