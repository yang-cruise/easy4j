package cn.easy4j.dict.modular.service;

import cn.easy4j.common.exception.BusinessException;
import cn.easy4j.dict.modular.dto.PostSysDictItemDTO;
import cn.easy4j.dict.modular.dto.PutSysDictItemDTO;
import cn.easy4j.dict.modular.entity.SysDictItem;
import cn.easy4j.dict.modular.mapper.SysDictItemMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author yangzongmin
 * @since 2019-08-21
 */
@Service
public class SysDictItemService extends ServiceImpl<SysDictItemMapper, SysDictItem> {

    /**
     * 根据字典ID查询字典项列表
     *
     * @param id 字典ID
     * @return 字典项列表
     */
    public List<SysDictItem> selectBySysDictId(Long id) {
        return this.lambdaQuery().eq(SysDictItem::getDictId, id).list();
    }

    /**
     * 更新字典项
     *
     * @param putSysDictItemDTO 字典项信息
     * @return 成功true、失败false
     */
    public boolean updateSysDictItemById(PutSysDictItemDTO putSysDictItemDTO) {
        SysDictItem sysDictItemFromDb = this.getById(putSysDictItemDTO.getId());

        SysDictItem sysDictItemByKey = this.lambdaQuery().eq(SysDictItem::getDictId, sysDictItemFromDb.getDictId()).eq(SysDictItem::getKey, putSysDictItemDTO.getKey()).one();

        if (Objects.nonNull(sysDictItemByKey) && !sysDictItemFromDb.getKey().equalsIgnoreCase(sysDictItemByKey.getKey())) {
            throw new BusinessException("该字典key已经存在了");
        }

        SysDictItem sysDictItemForUpdate = new SysDictItem();
        BeanUtils.copyProperties(putSysDictItemDTO, sysDictItemForUpdate);
        return this.updateById(sysDictItemForUpdate);
    }

    /**
     * 新增字典项
     *
     * @param postSysDictItemDTO 字典项信息
     * @return 成功true、失败false
     */
    public boolean insertSysDictItem(PostSysDictItemDTO postSysDictItemDTO) {

        SysDictItem sysDictItemFromDb = this.lambdaQuery().eq(SysDictItem::getDictId, postSysDictItemDTO.getDictId()).eq(SysDictItem::getKey, postSysDictItemDTO.getKey()).one();

        if (Objects.nonNull(sysDictItemFromDb)) {
            throw new BusinessException("该字典key已经存在了");
        }

        SysDictItem sysDictItemForInsert = new SysDictItem();
        BeanUtils.copyProperties(postSysDictItemDTO, sysDictItemForInsert);
        return this.save(sysDictItemForInsert);
    }

    /**
     * 根据字典ID删除字典项
     *
     * @param sysDictId 字典ID
     * @return 成功true、失败false
     */
    public boolean removeBySysDictId(Long sysDictId) {
        return this.lambdaUpdate().eq(SysDictItem::getDictId, sysDictId).remove();
    }
}
