package cn.easy4j.admin.modular.mapper;

import cn.easy4j.admin.modular.entity.SysMenu;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * @author yangzongmin
 * @since 2019-08-13
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 查询用户直接拥有的权限
     *
     * @param wrapper 条件构造器
     * @return 权限列表
     */
    @Select("select distinct m.* from sys_menu m join sys_user_menu um on um.menu_id = m.id ${ew.customSqlSegment} order by m.sort asc")
    List<SysMenu> selectSysUserMenuListByUserId(@Param(Constants.WRAPPER) Wrapper<?> wrapper);

    /**
     * 查询角色对应的所有权限
     *
     * @param wrapper 条件构造器
     * @return 权限列表
     */
    @Select("select distinct m.* from sys_menu m join sys_role_menu rm on rm.menu_id = m.id join sys_user_role ur on ur.role_id = rm.role_id ${ew.customSqlSegment} order by m.sort asc")
    List<SysMenu> selectSysUserRoleMenuListByUserId(@Param(Constants.WRAPPER) Wrapper<?> wrapper);

    /**
     * 根据用户ID查询所有权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Select("select distinct m.perms from sys_menu m join sys_role_menu rm on rm.menu_id = m.id join sys_user_role ur on ur.role_id = rm.role_id where ur.user_id = #{userId} and m.perms != '' " +
            "union all " +
            "select distinct m.perms from sys_menu m join sys_user_menu um on um.menu_id = m.id where um.user_id = #{userId} and m.perms != ''")
    Set<String> selectPermissionsByUserId(@Param("userId") Long userId);

    /**
     * 查询所有权限列表
     *
     * @return 权限列表
     */
    @Select("select distinct perms from sys_menu where perms != ''")
    Set<String> selectAllPermissions();

    /**
     * 查询菜单列表
     *
     * @param wrapper 条件构造器
     * @return 菜单列表
     */
    @Select("select * from sys_menu m ${ew.customSqlSegment} order by m.sort asc")
    List<SysMenu> listSysMenu(@Param(Constants.WRAPPER) Wrapper<?> wrapper);
}
