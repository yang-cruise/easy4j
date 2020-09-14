package cn.easy4j.admin.modular.service;

import cn.easy4j.admin.core.constant.AdminConstant;
import cn.easy4j.admin.modular.dto.GetSysRoleDTO;
import cn.easy4j.admin.modular.dto.PostSysRoleDTO;
import cn.easy4j.admin.modular.dto.PutSysRoleDTO;
import cn.easy4j.admin.modular.entity.SysRole;
import cn.easy4j.admin.modular.mapper.SysRoleMapper;
import cn.easy4j.common.exception.BusinessException;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author yangzongmin
 * @date 2019-07-25
 */
@Service
public class SysRoleService extends ServiceImpl<SysRoleMapper, SysRole> {

    public IPage<SysRole> pageList(IPage<SysRole> page, GetSysRoleDTO getSysRoleDTO) {
        LambdaUpdateWrapper<SysRole> wrapper = new LambdaUpdateWrapper<SysRole>()
                .like(StringUtils.isNotBlank(getSysRoleDTO.getRoleName()), SysRole::getRoleName, getSysRoleDTO.getRoleName())
                .like(StringUtils.isNotBlank(getSysRoleDTO.getRoleCode()), SysRole::getRoleCode, getSysRoleDTO.getRoleCode())
                .orderByDesc(SysRole::getId);
        return this.page(page, wrapper);
    }

    public Boolean updateSysRoleById(PutSysRoleDTO putSysRoleDTO) {
        checkOperationRole(putSysRoleDTO.getId());

        SysRole sysRoleByRoleName = this.lambdaQuery().eq(SysRole::getRoleName, putSysRoleDTO.getRoleName()).ne(SysRole::getId, putSysRoleDTO.getId()).one();

        if (Objects.nonNull(sysRoleByRoleName)) {
            throw new BusinessException("该角色名称已经存在了");
        }

        SysRole sysRoleByRoleCode = this.lambdaQuery().eq(SysRole::getRoleCode, putSysRoleDTO.getRoleCode()).ne(SysRole::getId, putSysRoleDTO.getId()).one();
        if (Objects.nonNull(sysRoleByRoleCode)) {
            throw new BusinessException("该角色标识已经存在了");
        }

        SysRole sysRoleForUpdate = new SysRole();
        BeanUtils.copyProperties(putSysRoleDTO, sysRoleForUpdate);
        return this.updateById(sysRoleForUpdate);
    }

    public Boolean insertSysRole(PostSysRoleDTO postSysRoleDTO) {
        synchronized (this) {
            SysRole sysRoleFromDb = this.lambdaQuery().eq(SysRole::getRoleName, postSysRoleDTO.getRoleName()).one();

            if (Objects.nonNull(sysRoleFromDb)) {
                throw new BusinessException("该角色名称已经存在了");
            }

            sysRoleFromDb = this.lambdaQuery().eq(SysRole::getRoleCode, postSysRoleDTO.getRoleCode()).one();
            if (Objects.nonNull(sysRoleFromDb)) {
                throw new BusinessException("该角色标识已经存在了");
            }

            SysRole sysRoleForInsert = new SysRole();
            BeanUtils.copyProperties(postSysRoleDTO, sysRoleForInsert);
            return this.save(sysRoleForInsert);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean removeRoleByIds(Set<Long> ids) {
        for (Long roleId : ids) {
            checkOperationRole(roleId);
            this.removeById(roleId);
            baseMapper.deleteRoleMenuByRoleId(roleId);
        }
        return Boolean.TRUE;
    }

    public void checkOperationRole(Long roleId) {
        if (Objects.equals(roleId, AdminConstant.ADMIN_ROLE_ID)) {
            throw new BusinessException("不能操作管理员角色");
        }
    }

    public List<SysRole> listAll() {
        return Optional.ofNullable(baseMapper.listUnContainsAdminRole()).orElse(Collections.emptyList());
    }
}
