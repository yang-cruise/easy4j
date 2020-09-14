package cn.easy4j.admin.modular.service;

import cn.easy4j.admin.core.constant.AdminConstant;
import cn.easy4j.admin.modular.dto.GetSysUserDTO;
import cn.easy4j.admin.modular.dto.PostSysUserDTO;
import cn.easy4j.admin.modular.dto.PutSysUserDTO;
import cn.easy4j.admin.modular.dto.PutSysUserSelfInfoDTO;
import cn.easy4j.admin.modular.entity.LoginUser;
import cn.easy4j.admin.modular.entity.SysUser;
import cn.easy4j.admin.modular.entity.SysUserRole;
import cn.easy4j.admin.modular.mapper.SysUserMapper;
import cn.easy4j.admin.modular.vo.SysUserVO;
import cn.easy4j.common.exception.BusinessException;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

/**
 * 系统管理用户表 服务实现类
 *
 * @author yangzongmin
 * @date 2019-07-19
 */
@Service
public class SysUserService extends ServiceImpl<SysUserMapper, SysUser> {

    @Resource
    private SysUserRoleService sysUserRoleService;

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private SysDeptService sysDeptService;

    @Resource
    private SysUserCacheService sysUserCacheService;

    public void checkOperationUser(Long userId) {
        if (Objects.equals(userId, AdminConstant.ADMIN_USER_ID)) {
            throw new BusinessException("不能操作管理员");
        }
    }

    public SysUser selectByAccount(String account) {
        return this.lambdaQuery().eq(SysUser::getAccount, account).one();
    }

    public IPage<SysUserVO> pageList(IPage<SysUser> page, GetSysUserDTO dto) {

        if (Objects.nonNull(dto.getDeptId())) {
            Set<Long> deptIds = sysDeptService.querySubDept(dto.getDeptId(), Sets.newHashSet());
            dto.setDeptIds(deptIds);
        }

        Wrapper<Object> wrapper = Wrappers.query()
                .eq(" u.is_deleted", 0)
                .like(StringUtils.isNotBlank(dto.getAccount()), "u.account", dto.getAccount())
                .like(StringUtils.isNotBlank(dto.getRealname()), "u.realname", dto.getRealname())
                .like(StringUtils.isNotBlank(dto.getMobile()), "u.mobile", dto.getMobile())
                .like(StringUtils.isNotBlank(dto.getEmail()), "u.email", dto.getEmail())
                .eq(Objects.nonNull(dto.getStatus()), "u.status", dto.getStatus())
                .in(!CollectionUtils.isEmpty(dto.getDeptIds()), "u.dept_id", dto.getDeptIds());
        return baseMapper.pageList(page, wrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean updateUserById(PutSysUserDTO dto) {
        checkOperationUser(dto.getId());
        SysUser sysUserForUpdate = new SysUser();
        BeanUtils.copyProperties(dto, sysUserForUpdate);
        sysUserRoleService.removeByUserId(dto.getId());
        userAddRole(sysUserForUpdate.getId(), dto.getRoleIds());

        sysUserCacheService.clearCacheByUserId(sysUserForUpdate.getId());
        return this.updateById(sysUserForUpdate);
    }

    public Boolean updateInfoByUserId(Long userId, PutSysUserSelfInfoDTO dto) {
        SysUser sysUserForUpdate = new SysUser();
        BeanUtils.copyProperties(dto, sysUserForUpdate);

        sysUserCacheService.clearCacheByUserId(userId);
        sysUserForUpdate.setId(userId);
        return this.updateById(sysUserForUpdate);
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean insertUser(PostSysUserDTO dto) {
        synchronized (this) {
            SysUser sysUserFromDb = this.selectByAccount(dto.getAccount());
            if (Objects.nonNull(sysUserFromDb)) {
                throw new BusinessException("用户名已存在, 请重新输入！");
            }
            SysUser sysUserForInsert = new SysUser();
            BeanUtils.copyProperties(dto, sysUserForInsert);
            sysUserForInsert.setPassword(new BCryptPasswordEncoder().encode(dto.getPassword()));
            this.save(sysUserForInsert);
            userAddRole(sysUserForInsert.getId(), dto.getRoleIds());
        }
        return Boolean.TRUE;
    }

    private void userAddRole(Long userId, Set<Long> roleIds) {
        if (!CollectionUtils.isEmpty(roleIds)) {
            for (Long roleId : roleIds) {
                SysUserRole sysUserRoleForInsert = new SysUserRole();
                sysUserRoleForInsert.setRoleId(roleId);
                sysUserRoleForInsert.setUserId(userId);
                sysUserRoleService.save(sysUserRoleForInsert);
            }
        }
    }

    public LoginUser getLoginUserByUserId(Long userId) {

        SysUser sysUserFromDb = this.getById(userId);

        if (Objects.isNull(sysUserFromDb)) {
            return null;
        }
        return getLoginUserBySysUser(sysUserFromDb);
    }

    public LoginUser getLoginUserBySysUser(SysUser sysUserFromDb) {
        LoginUser loginUser = new LoginUser();
        BeanUtils.copyProperties(sysUserFromDb, loginUser);
        Set<String> permissions = sysMenuService.selectPermissionsByUserId(sysUserFromDb.getId());
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (String permission : permissions) {
            if (StringUtils.isNotBlank(permission)) {
                authorities.add(new SimpleGrantedAuthority(permission));
            }
        }
        loginUser.setAuthorities(authorities);
        return loginUser;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean removeSysUserByIds(Set<Long> ids) {

        for (Long sysUserId : ids) {
            checkOperationUser(sysUserId);
            this.removeById(sysUserId);
            sysUserRoleService.removeByUserId(sysUserId);
            sysUserCacheService.clearCacheByUserId(sysUserId);
        }
        return Boolean.TRUE;
    }

    public SysUserVO selectSysUserById(Long id) {
        return baseMapper.selectByUserId(id);
    }

    public Boolean changeStatus(Long userId, Integer status) {
        checkOperationUser(userId);
        return this.lambdaUpdate().eq(SysUser::getId, userId).set(SysUser::getStatus, status).update();
    }

    public Boolean updatePasswordByUserId(Long userId, String originPassword, String newPassword) {
        SysUser sysUserFromDb = this.getById(userId);

        if (!new BCryptPasswordEncoder().matches(originPassword, sysUserFromDb.getPassword())) {
            throw new BusinessException("旧密码错误，请重新输入");
        }
        return this.lambdaUpdate().eq(SysUser::getId, userId).set(SysUser::getModifyPassword, 1).set(SysUser::getPassword, new BCryptPasswordEncoder().encode(newPassword)).update();
    }

    public Boolean updatePasswordByUserId(Long userId, String newPassword) {
        checkOperationUser(userId);
        return this.lambdaUpdate().eq(SysUser::getId, userId).set(SysUser::getModifyPassword, 0).set(SysUser::getPassword, new BCryptPasswordEncoder().encode(newPassword)).update();
    }

}
