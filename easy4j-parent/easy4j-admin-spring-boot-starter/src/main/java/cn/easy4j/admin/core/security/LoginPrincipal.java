package cn.easy4j.admin.core.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.security.Principal;

/**
 * @author yangzongmin
 * @since 2020/7/31 16:28
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginPrincipal implements Principal, Serializable {

    private static final long serialVersionUID = 1L;

    private String type;
    private String principal;

    @Override
    public String getName() {
        return "{\"type\":\"" + this.type + "\",\"principal\":\"" + this.principal + "\"}";
    }

}
