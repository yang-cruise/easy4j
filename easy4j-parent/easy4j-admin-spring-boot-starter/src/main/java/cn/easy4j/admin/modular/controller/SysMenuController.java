package cn.easy4j.admin.modular.controller;

import cn.easy4j.admin.core.base.BaseController;
import cn.easy4j.admin.core.util.SecurityUtil;
import cn.easy4j.admin.modular.dto.GetSysMenuDTO;
import cn.easy4j.admin.modular.dto.PostSysMenuDTO;
import cn.easy4j.admin.modular.dto.PutSysMenuDTO;
import cn.easy4j.admin.modular.dto.PutSysMenuSortDTO;
import cn.easy4j.admin.modular.entity.SysMenu;
import cn.easy4j.admin.modular.service.SysMenuService;
import cn.easy4j.admin.modular.service.SysUserCacheService;
import cn.easy4j.framework.annotation.CheckRepeatSubmit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @author yangzongmin
 * @since 2019/8/18
 */
@Api(tags = "1004.系统菜单")
@RestController
@RequestMapping("/sys_menus")
public class SysMenuController extends BaseController {

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private SysUserCacheService sysUserCacheService;

    @ApiOperation(value = "查询自己拥有的全部菜单权限标识")
    @GetMapping("/self_permissions")
    public Set<String> getSelfPermissions() {
        return sysMenuService.selectPermissionsByUserId(SecurityUtil.getLoginUserId());
    }

    @ApiOperation(value = "查询我的菜单树 仅目录和菜单")
    @GetMapping("/self_tree")
    public List<SysMenu> getSelfTree() {
        return sysMenuService.selectSysMenuTreeByUserId(SecurityUtil.getLoginUserId());
    }

    @ApiOperation(value = "查询全部菜单树")
    @GetMapping("/tree")
    @PreAuthorize("hasPermission('菜单管理', '查询菜单', 'sys:menu:select')")
    public List<SysMenu> getTree(GetSysMenuDTO dto) {
        return sysMenuService.listSysMenu(dto);
    }

    @ApiOperation(value = "新增菜单")
    @PostMapping
    @CheckRepeatSubmit
    @PreAuthorize("hasPermission('菜单管理', '新增菜单', 'sys:menu:insert')")
    public Boolean post(@Validated @RequestBody PostSysMenuDTO dto) {
        sysUserCacheService.clearCacheAll();
        return sysMenuService.insertSysMenu(dto);
    }

    @ApiOperation(value = "更新菜单")
    @PutMapping
    @PreAuthorize("hasPermission('菜单管理', '修改菜单', 'sys:menu:update')")
    public Boolean put(@Validated @RequestBody PutSysMenuDTO dto) {
        sysUserCacheService.clearCacheAll();
        return sysMenuService.updateSysMenuById(dto);
    }

    @ApiOperation(value = "菜单排序")
    @PutMapping("/sort")
    @PreAuthorize("hasPermission('菜单管理', '修改菜单', 'sys:menu:update')")
    public Boolean sort(@Validated @RequestBody List<PutSysMenuSortDTO> dtoList) {
        return sysMenuService.sort(dtoList);
    }

    @ApiOperation(value = "删除菜单（级联删除子菜单）")
    @DeleteMapping("/{ids}")
    @PreAuthorize("hasPermission('菜单管理', '删除菜单', 'sys:menu:delete')")
    public Boolean delete(@PathVariable Set<Long> ids) {
        sysUserCacheService.clearCacheAll();
        return sysMenuService.deleteByIds(ids);
    }
}
