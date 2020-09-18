package cn.easy4j.admin.modular.entity;

import cn.easy4j.dict.core.jackson.JsonDictConvert;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 系统管理用户表
 *
 * @author yangzongmin
 * @since 2019-07-19
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "SysUser对象")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime gmtModified;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "账户")
    private String account;

    @JsonIgnore
    private String password;

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

    @ApiModelProperty(value = "用户所在部门, 关联sys_dept.id")
    private Long deptId;

    @TableLogic
    private Integer isDeleted;

}
