package cn.easy4j.admin.modular.mapper;

import cn.easy4j.admin.modular.entity.SysDept;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Mapper 接口
 *
 * @author ChenYichen
 * @since 2020-02-10
 */
@Mapper
public interface SysDeptMapper extends BaseMapper<SysDept> {

    /**
     * 根据ID查询部门
     *
     * @param id 主键
     * @return 部门信息
     */
    @Select("select * from sys_dept where id = #{id}")
    SysDept getById(@Param("id") Long id);

}
