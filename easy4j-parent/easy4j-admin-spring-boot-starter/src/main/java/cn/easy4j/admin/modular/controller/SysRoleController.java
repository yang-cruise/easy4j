package cn.easy4j.admin.modular.controller;

import cn.easy4j.admin.core.base.BaseController;
import cn.easy4j.admin.modular.dto.GetSysRoleDTO;
import cn.easy4j.admin.modular.dto.PostSysRoleDTO;
import cn.easy4j.admin.modular.dto.PutSysRoleDTO;
import cn.easy4j.admin.modular.entity.SysRole;
import cn.easy4j.admin.modular.service.SysRoleMenuService;
import cn.easy4j.admin.modular.service.SysRoleService;
import cn.easy4j.framework.annotation.CheckRepeatSubmit;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
 * @since 2019-07-19
 */
@Api(tags = "1003.系统角色")
@RestController
@RequestMapping("/sys_roles")
public class SysRoleController extends BaseController {

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private SysRoleMenuService sysRoleMenuService;

    @ApiOperation(value = "查询全部角色")
    @GetMapping
    @PreAuthorize("hasPermission('角色管理', '查询角色', 'sys:role:select')")
    public List<SysRole> get() {
        return sysRoleService.listAll();
    }

    @ApiOperation(value = "分页查询角色列表")
    @GetMapping("/page")
    @PreAuthorize("hasPermission('角色管理', '查询角色', 'sys:role:select')")
    public IPage<SysRole> getPage(GetSysRoleDTO getSysRoleDTO) {
        return sysRoleService.pageList(getPage(), getSysRoleDTO);
    }

    @ApiOperation(value = "查询指定角色的全部权限")
    @GetMapping("/{id}/permissions")
    @PreAuthorize("hasPermission('角色管理', '查询角色', 'sys:role:select')")
    public Set<Long> getSysRolePermissions(@PathVariable Long id) {
        return sysRoleMenuService.selectByRoleId(id);
    }

    @ApiOperation(value = "新增角色")
    @PostMapping
    @CheckRepeatSubmit
    @PreAuthorize("hasPermission('角色管理', '新增角色', 'sys:role:insert')")
    public Boolean post(@Validated @RequestBody PostSysRoleDTO postSysRoleDTO) {
        return sysRoleService.insertSysRole(postSysRoleDTO);
    }

    @ApiOperation(value = "更新角色")
    @PutMapping
    @PreAuthorize("hasPermission('角色管理', '修改角色', 'sys:role:update')")
    public Boolean put(@Validated @RequestBody PutSysRoleDTO putSysRoleDTO) {
        return sysRoleService.updateSysRoleById(putSysRoleDTO);
    }

    @ApiOperation(value = "权限分配")
    @PutMapping("/{id}/{menuIds}")
    @PreAuthorize("hasPermission('角色管理', '权限分配', 'sys:role:permission')")
    public Boolean putSysRoleMenu(@PathVariable Long id, @PathVariable Set<Long> menuIds) {
        return sysRoleMenuService.assignPermission(id, menuIds);
    }

    @ApiOperation(value = "删除角色 多个id用，逗号分隔")
    @DeleteMapping("/{ids}")
    @PreAuthorize("hasPermission('角色管理', '删除角色', 'sys:role:delete')")
    public boolean delete(@PathVariable Set<Long> ids) {
        return sysRoleService.removeRoleByIds(ids);
    }

}
