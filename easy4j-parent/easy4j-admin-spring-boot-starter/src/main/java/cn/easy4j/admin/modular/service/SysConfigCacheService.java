package cn.easy4j.admin.modular.service;

import cn.easy4j.admin.modular.entity.SysConfig;
import cn.easy4j.common.constant.CacheConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 系统配置缓存服务类
 * @author yangzongmin
 */
@Slf4j
@Service
public class SysConfigCacheService {

    @Resource
    private SysConfigService sysConfigService;

    @Cacheable(value = CacheConstant.SYS_CONFIG, key = "#configKey")
    public String getConfigContentByConfigKey(final String configKey) {
        SysConfig sysConfig = sysConfigService.selectByConfigKey(configKey);
        return Objects.isNull(sysConfig) ? StringUtils.EMPTY : sysConfig.getConfigContent();
    }

    @CacheEvict(value = CacheConstant.SYS_CONFIG, key = "#configKey", beforeInvocation = true)
    public void clearCacheByConfigKey(final String configKey) {
        log.info("清除缓存成功，name = [{}], key = [{}]", CacheConstant.SYS_CONFIG, configKey);
    }

    @CacheEvict(value = CacheConstant.SYS_CONFIG, allEntries = true, beforeInvocation = true)
    public void clearAllSysConfigCache() {
        log.info("清除缓存成功，name = [{}]", CacheConstant.SYS_CONFIG);
    }
}
