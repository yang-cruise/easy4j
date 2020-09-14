package cn.easy4j.admin.modular.service;

import cn.easy4j.admin.modular.entity.SysUserRole;
import cn.easy4j.admin.modular.mapper.SysUserRoleMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author yangzongmin
 * @date 2019-07-25
 */
@Service
public class SysUserRoleService extends ServiceImpl<SysUserRoleMapper, SysUserRole> {

    public List<SysUserRole> selectByUserId(Long userId) {
        return Optional.ofNullable(this.lambdaQuery().eq(SysUserRole::getUserId, userId).list()).orElse(Collections.emptyList());
    }

    public Boolean removeByUserId(Long userId) {
        return this.lambdaUpdate().eq(SysUserRole::getUserId, userId).remove();
    }
}
