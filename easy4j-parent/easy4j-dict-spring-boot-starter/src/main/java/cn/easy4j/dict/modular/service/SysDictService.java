package cn.easy4j.dict.modular.service;

import cn.easy4j.common.exception.BusinessException;
import cn.easy4j.dict.modular.dto.GetSysDictDTO;
import cn.easy4j.dict.modular.dto.PostSysDictDTO;
import cn.easy4j.dict.modular.dto.PutSysDictDTO;
import cn.easy4j.dict.modular.entity.SysDict;
import cn.easy4j.dict.modular.entity.SysDictItem;
import cn.easy4j.dict.modular.mapper.SysDictMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author yangzongmin
 * @since 2019-08-23
 */
@Service
public class SysDictService extends ServiceImpl<SysDictMapper, SysDict> {

    @Resource
    private SysDictItemService sysDictItemService;

    /**
     * 根据字典标识查询字典项列表
     *
     * @param code 字典标识
     * @return 字典项列表
     */
    public List<SysDictItem> selectSysDictItemListByCode(String code) {
        return baseMapper.selectSysDictItemListByCode(code);
    }

    /**
     * 根据字典标识查找第一个字典项
     *
     * @param code 字典标识
     * @return 第一个字典项
     */
    public SysDictItem selectSysDictItemByCode(String code) {
        List<SysDictItem> items = this.selectSysDictItemListByCode(code);
        if (CollectionUtils.isEmpty(items)) {
            return null;
        }
        return items.get(0);
    }

    /**
     * 根据字典标识和字典项值反正显示内容
     *
     * @param code 字典标识
     * @param key  字典项值
     * @return 字典项显示内容
     */
    public String selectSysDictItemByCodeAndKey(String code, String key) {
        return baseMapper.selectSysDictItemByCodeAndKey(code, key);
    }

    /**
     * 分页查询数据字典
     *
     * @param page       分页条件
     * @param getSysDictDTO 查询条件
     * @return 字典列表
     */
    public IPage<SysDict> pageList(IPage<SysDict> page, GetSysDictDTO getSysDictDTO) {

        LambdaQueryWrapper<SysDict> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(getSysDictDTO.getCode()), SysDict::getCode, getSysDictDTO.getCode());
        wrapper.like(StringUtils.isNotBlank(getSysDictDTO.getName()), SysDict::getName, getSysDictDTO.getName());
        wrapper.orderByAsc(SysDict::getSort);
        return this.page(page, wrapper);
    }

    /**
     * 新增数据字典
     *
     * @param postSysDictDTO 字典信息
     * @return 成功true、失败false
     */
    public boolean insertSysDict(PostSysDictDTO postSysDictDTO) {

        String code = postSysDictDTO.getCode();
        SysDict sysDictFromDb = this.lambdaQuery().eq(SysDict::getCode, code).one();

        if (Objects.nonNull(sysDictFromDb)) {
            throw new BusinessException("字典编码已经存在了");
        }

        String name = postSysDictDTO.getName();
        sysDictFromDb = this.lambdaQuery().eq(SysDict::getName, name).one();

        if (Objects.nonNull(sysDictFromDb)) {
            throw new BusinessException("字典名称已经存在了");
        }

        SysDict sysDictForInsert = new SysDict();
        BeanUtils.copyProperties(postSysDictDTO, sysDictForInsert);
        return this.save(sysDictForInsert);
    }

    /**
     * 更新字典信息
     *
     * @param putSysDictDTO 字典信息
     * @return 成功true、失败false
     */
    public boolean updateSysDictById(PutSysDictDTO putSysDictDTO) {

        SysDict sysDictByCode = this.lambdaQuery().eq(SysDict::getCode, putSysDictDTO.getCode()).ne(SysDict::getId, putSysDictDTO.getId()).one();

        if (Objects.nonNull(sysDictByCode)) {
            throw new BusinessException("字典编码已经存在了");
        }

        SysDict sysDictByName = this.lambdaQuery().eq(SysDict::getName, putSysDictDTO.getName()).ne(SysDict::getId, putSysDictDTO.getId()).one();

        if (Objects.nonNull(sysDictByName)) {
            throw new BusinessException("字典名称已经存在了");
        }

        SysDict sysDictForUpdate = new SysDict();
        BeanUtils.copyProperties(putSysDictDTO, sysDictForUpdate);
        return this.updateById(sysDictForUpdate);
    }

    /**
     * 删除字典、级联删除改字典包含的字典项
     *
     * @param ids 字典ID
     * @return 成功true、失败false
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteSysDictById(Set<Long> ids) {
        this.lambdaUpdate().in(SysDict::getId, ids).remove();
        if (!CollectionUtils.isEmpty(ids)) {
            for (Long id : ids) {
                sysDictItemService.removeBySysDictId(id);
            }
        }
        return true;
    }
}
