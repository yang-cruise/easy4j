package cn.easy4j.admin.modular.mapper;

import cn.easy4j.admin.modular.entity.SysDept;
import cn.easy4j.admin.modular.entity.SysUser;
import cn.easy4j.admin.modular.vo.SysUserVO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.*;

import java.util.Set;

/**
 * 系统管理用户表 Mapper 接口
 *
 * @author yangzongmin
 * @since 2019-07-19
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 查询分页列表
     *
     * @param page    分页对象
     * @param wrapper 查询条件
     * @return 查询结果
     */
    @Select("select distinct u.* from sys_user u ${ew.customSqlSegment} order by u.id desc")
    @Results(id="sysUserMap", value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "dept", column = "dept_id", javaType = SysDept.class, one = @One(select = "cn.easy4j.admin.modular.mapper.SysDeptMapper.getById")),
            @Result(property = "roles", column = "id", javaType = Set.class, many = @Many(select = "cn.easy4j.admin.modular.mapper.SysRoleMapper.selectByUserId"))
    })
    IPage<SysUserVO> pageList(@Param(value = "page") IPage<SysUser> page, @Param(value = Constants.WRAPPER) Wrapper<?> wrapper);

    /**
     * 根据用户ID查询用户详情
     *
     * @param id 用户ID
     * @return 用户详情对象
     */
    @Select("select u.* from  sys_user u  where u.id = #{id}")
    @ResultMap(value = "sysUserMap")
    SysUserVO selectByUserId(@Param("id") Long id);

}
