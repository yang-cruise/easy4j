package cn.easy4j.admin.modular.service;

import cn.easy4j.admin.modular.entity.SysMenu;
import cn.easy4j.admin.modular.entity.SysRoleMenu;
import cn.easy4j.admin.modular.mapper.SysRoleMenuMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yangzongmin
 * @since 2019-07-25
 */
@Service
public class SysRoleMenuService extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> {

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private SysUserCacheService sysUserCacheService;

    @Transactional(rollbackFor = Exception.class)
    public Boolean assignPermission(Long roleId, Set<Long> menuIds) {
        sysRoleService.checkOperationRole(roleId);
        this.lambdaUpdate().eq(SysRoleMenu::getRoleId, roleId).remove();

        if (!CollectionUtils.isEmpty(menuIds)) {
            for (Long menuId : menuIds) {
                SysRoleMenu sysRoleMenuForInsert = new SysRoleMenu();
                sysRoleMenuForInsert.setRoleId(roleId);
                sysRoleMenuForInsert.setMenuId(menuId);
                this.save(sysRoleMenuForInsert);
            }
        }
        sysUserCacheService.clearCacheAll();
        return Boolean.TRUE;
    }

    public Set<Long> selectByRoleId(Long roleId) {
        List<SysRoleMenu> sysRoleMenus = this.lambdaQuery().eq(SysRoleMenu::getRoleId, roleId).list();

        if (CollectionUtils.isEmpty(sysRoleMenus)) {
            return Collections.emptySet();
        }
        Set<Long> menuIds = sysRoleMenus.parallelStream().map(SysRoleMenu::getMenuId).collect(Collectors.toSet());
        List<SysMenu> sysMenus = sysMenuService.selectByIds(menuIds);
        if (CollectionUtils.isEmpty(sysMenus)) {
            return Collections.emptySet();
        }
        return sysMenus.parallelStream().map(SysMenu::getId).collect(Collectors.toSet());
    }

}
