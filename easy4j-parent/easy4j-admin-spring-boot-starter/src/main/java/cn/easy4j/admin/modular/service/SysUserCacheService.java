package cn.easy4j.admin.modular.service;

import cn.easy4j.admin.modular.entity.LoginUser;
import cn.easy4j.common.constant.CacheConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 本类是关于SysUser缓存相关的API
 *
 * @author ChenYichen
 */
@Slf4j
@Service
public class SysUserCacheService {

    @Resource
    private SysUserService sysUserService;

    @Cacheable(value = CacheConstant.SYS_USER, key = "#userId")
    public LoginUser getLoginUserByUserId(Long userId) {
        return sysUserService.getLoginUserByUserId(userId);
    }

    @CachePut(value = CacheConstant.SYS_USER, key = "#userId")
    public LoginUser refreshLoginUserByUserId(Long userId) {
        log.info("刷新缓存成功，name = [{}], key = [{}]", CacheConstant.SYS_USER, userId);
        return sysUserService.getLoginUserByUserId(userId);
    }

    @CacheEvict(value = CacheConstant.SYS_USER, key = "#userId", beforeInvocation = true)
    public void clearCacheByUserId(Long userId) {
        log.info("清除缓存成功，name = [{}], key = [{}]", CacheConstant.SYS_USER, userId);
    }

    @CacheEvict(value = CacheConstant.SYS_USER, allEntries = true, beforeInvocation = true)
    public void clearCacheAll() {
        log.info("清除缓存成功，name = [{}]", CacheConstant.SYS_USER);
    }
}
