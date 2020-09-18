package cn.easy4j.dict.modular.mapper;

import cn.easy4j.dict.modular.entity.SysDict;
import cn.easy4j.dict.modular.entity.SysDictItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author yangzongmin
 * @since 2019-08-21
 */
@Mapper
public interface SysDictMapper extends BaseMapper<SysDict> {

    /**
     * 根据字典标识查询字典数据列表
     *
     * @param code 字典标识
     * @return 字典数据列表
     */
    @Select("select sdi.* from sys_dict sd join sys_dict_item sdi on sd.id = sdi.dict_id where sd.code = #{code} and sd.is_deleted = 0 and sdi.is_deleted = 0 order by sort asc")
    List<SysDictItem> selectSysDictItemListByCode(@Param("code") String code);

    /**
     * 根据字典标识与关键字查询字典信息
     *
     * @param code 字典标识
     * @param key  数据库中的值
     * @return 显示的信息
     */
    @Select("select sdi.value from sys_dict sd join sys_dict_item sdi on sd.id = sdi.dict_id where sd.code = #{code} and sdi.key = #{key}")
    String selectSysDictItemByCodeAndKey(@Param("code") String code, @Param("key") String key);

}
