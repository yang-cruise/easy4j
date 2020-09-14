package cn.easy4j.admin.core.util;

import cn.easy4j.admin.modular.entity.LoginUser;
import cn.easy4j.common.exception.BusinessException;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 权限工具类
 *
 * @author ChenYichen
 */
public class SecurityUtil {

    private SecurityUtil() {}

    /**
     * 获取当前登陆用户
     */
    public static LoginUser getLoginUser() {
        try {
            return (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new BusinessException("用户账户信息异常");
        }
    }

    /**
     * 获取当前登陆用户的ID
     */
    public static Long getLoginUserId() {
        try {
            return ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        } catch (Exception e) {
            throw new BusinessException("用户账户信息异常");
        }
    }
}
