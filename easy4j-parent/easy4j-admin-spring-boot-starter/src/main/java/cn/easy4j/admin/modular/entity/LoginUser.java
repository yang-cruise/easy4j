package cn.easy4j.admin.modular.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

/**
 * 登陆用户
 *
 * @author ChenYichen
 */
@Data
public class LoginUser implements UserDetails {

    private static final long serialVersionUID = 1L;

    public LoginUser() {
    }

    public LoginUser(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @ApiModelProperty(value = "用户主键ID")
    private Long id;

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

    @ApiModelProperty(value = "权限列表")
    private Collection<? extends GrantedAuthority> authorities;

    @ApiModelProperty(value = "部门列表")
    private List<SysDept> depts;

    @ApiModelProperty(value = "角色列表")
    private List<SysRole> roles;

    @ApiModelProperty(value = "部门ID")
    private Long deptId;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.account;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}
