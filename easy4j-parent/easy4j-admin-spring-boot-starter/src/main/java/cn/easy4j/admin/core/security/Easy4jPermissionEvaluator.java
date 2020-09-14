package cn.easy4j.admin.core.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

/**
 * @author yangzongmin
 * @since 2020/6/5 22:25
 */
@Slf4j
public class Easy4jPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        log.warn("Denying user " + authentication.getName() + " permission '" + permission + "' on object " + targetDomainObject);
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        Collection<? extends GrantedAuthority> userAuthorities = authentication.getAuthorities();
        Assert.notNull(userAuthorities, "userAuthorities cannot be null");
        for (GrantedAuthority authority : userAuthorities) {
            if (Objects.equals(authority.getAuthority(), permission)) {
                return true;
            }
        }
        return false;
    }

}
