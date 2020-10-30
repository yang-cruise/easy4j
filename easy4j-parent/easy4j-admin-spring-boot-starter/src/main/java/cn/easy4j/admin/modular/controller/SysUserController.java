package cn.easy4j.admin.modular.controller;

import cn.easy4j.admin.core.base.BaseController;
import cn.easy4j.admin.core.log.SysLog;
import cn.easy4j.admin.core.util.SecurityUtil;
import cn.easy4j.admin.modular.dto.*;
import cn.easy4j.admin.modular.service.SysUserService;
import cn.easy4j.admin.modular.vo.SysUserVO;
import cn.easy4j.framework.annotation.CheckRepeatSubmit;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author yangzongmin
 * @since 2019-07-19
 */
@Api(tags = "1001.系统用户")
@RestController
@RequestMapping("/sys_users")
public class SysUserController extends BaseController {

    @Resource
    private SysUserService sysUserService;

    @ApiOperation(value = "查询个人信息")
    @GetMapping("/self_info")
    public SysUserVO getSelfInfo() {
        Long userId = SecurityUtil.getLoginUserId();
        return sysUserService.selectSysUserById(userId);
    }

    @ApiOperation(value = "分页查询用户列表")
    @GetMapping("/page")
    @PreAuthorize("hasPermission('用户管理', '查询用户', 'sys:user:select')")
    public IPage<SysUserVO> getPage(GetSysUserDTO dto) {
        return sysUserService.pageList(getPage(), dto);
    }

    @ApiOperation(value = "查询用户详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasPermission('用户管理', '查询用户', 'sys:user:select')")
    public SysUserVO detail(@PathVariable Long id) {
        return sysUserService.selectSysUserById(id);
    }

    @ApiOperation(value = "新增用户")
    @PostMapping
    @CheckRepeatSubmit
    @PreAuthorize("hasPermission('用户管理', '新增用户', 'sys:user:insert')")
    public Boolean post(@Validated @RequestBody PostSysUserDTO dto) {
        return sysUserService.insertUser(dto);
    }

    @ApiOperation(value = "修改个人信息")
    @PutMapping("/self_info")
    public Boolean putSelfInfo(@Validated @RequestBody PutSysUserSelfInfoDTO dto) {
        Long userId = SecurityUtil.getLoginUserId();
        return sysUserService.updateInfoByUserId(userId, dto);
    }

    @ApiOperation(value = "修改个人密码")
    @PutMapping("/self_password")
    public Boolean putSelfPassword(@Validated @RequestBody PutSysUserSelfPasswordDTO dto) {
        Assert.isTrue(StringUtils.equals(dto.getNewPassword(), dto.getConfirmPassword()), "两次密码不一致，请重新输入");
        Long userId = SecurityUtil.getLoginUserId();
        return sysUserService.updatePasswordByUserId(userId, dto.getOriginPassword(), dto.getNewPassword());
    }

    @SysLog(modular = "用户管理", entityId = "#dto.id", query = "@sysUserService.selectSysUserById(#dto.id)")
    @ApiOperation(value = "更新用户信息")
    @PutMapping
    @PreAuthorize("hasPermission('用户管理', '修改用户', 'sys:user:update')")
    public Boolean put(@Validated @RequestBody PutSysUserDTO dto) {
        return sysUserService.updateUserById(dto);
    }

    @ApiOperation(value = "更新用户状态")
    @PutMapping("/{id}/status")
    @PreAuthorize("hasPermission('用户管理', '修改用户', 'sys:user:update')")
    public Boolean putStatus(@PathVariable Long id, @RequestBody PutSysUserStatusDTO dto) {
        return sysUserService.changeStatus(id, dto.getStatus());
    }

    @ApiOperation(value = "更新用户密码")
    @PutMapping("/{id}/password")
    @PreAuthorize("hasPermission('用户管理', '修改用户', 'sys:user:update')")
    public Boolean putPassword(@PathVariable Long id, @RequestBody PutSysUserPasswordDTO dto) {
        return sysUserService.updatePasswordByUserId(id, dto.getNewPassword());
    }

    @ApiOperation(value = "删除用户")
    @DeleteMapping("/{ids}")
    @PreAuthorize("hasPermission('用户管理', '删除用户', 'sys:user:delete')")
    public Boolean delete(@PathVariable Set<Long> ids) {
        return sysUserService.removeSysUserByIds(ids);
    }

}
