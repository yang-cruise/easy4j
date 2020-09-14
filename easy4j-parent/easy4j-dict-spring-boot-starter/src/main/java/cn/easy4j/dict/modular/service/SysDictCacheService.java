package cn.easy4j.dict.modular.service;

import cn.easy4j.common.constant.CacheConstant;
import cn.easy4j.dict.modular.entity.SysDictItem;
import cn.easy4j.dict.modular.mapper.SysDictMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 数据字典查询服务类（带缓存）
 * @author yangzongmin
 * @since 2020/6/2 17:55
 */
@Slf4j
@Service
public class SysDictCacheService {

    @Resource
    private SysDictMapper sysDictMapper;

    /**
     * 根据字典标识和字典项值反正显示内容
     *
     * @param code 字典标识
     * @param key  字典项值
     * @return 字典项显示内容
     */
    @Cacheable(value = CacheConstant.SYS_DICT, key = "#code + '_' + #key")
    public String selectSysDictItemByCodeAndKey(String code, String key) {
        return sysDictMapper.selectSysDictItemByCodeAndKey(code, key);
    }

    /**
     * 根据字典标识查询字典项列表
     *
     * @param code 字典标识
     * @return 字典项列表
     */
    @Cacheable(value = CacheConstant.SYS_DICT, key = "#code")
    public List<SysDictItem> selectSysDictItemListByCode(String code) {
        return sysDictMapper.selectSysDictItemListByCode(code);
    }

    /**
     * 清除数据字典所有缓存
     */
    @CacheEvict(value = CacheConstant.SYS_DICT, allEntries = true, beforeInvocation = true)
    public void clearCacheAll() {
        log.info("清除缓存成功，name = [{}]", CacheConstant.SYS_DICT);
    }

}
