package cn.easy4j.admin.modular.mapper;

import cn.easy4j.admin.modular.entity.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * @author yangzongmin
 * @date 2019-07-25
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 根据用户ID、查询角色列表
     *
     * @param id 用户ID
     * @return 角色列表
     */
    @Select("select  r.* from sys_role r left join sys_user_role ur on ur.role_id = r.id where ur.user_id = #{id}")
    Set<SysRole> selectByUserId(@Param("id") Long id);

    /**
     * 查询全部角色、不包含管理员
     *
     * @return 角色列表
     */
    @Select("select * from sys_role where id != 1 order by gmt_create")
    List<SysRole> listUnContainsAdminRole();

    /**
     * 删除角色与菜单关联关系
     * @param roleId 角色ID
     */
    @Delete("delete from sys_role_menu where role_id = #{roleId} ")
    void deleteRoleMenuByRoleId(@Param("roleId") Long roleId);

}
