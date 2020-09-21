package cn.easy4j.admin.modular.service;

import cn.easy4j.admin.core.constant.AdminConstant;
import cn.easy4j.admin.modular.dto.GetSysMenuDTO;
import cn.easy4j.admin.modular.dto.PostSysMenuDTO;
import cn.easy4j.admin.modular.dto.PutSysMenuDTO;
import cn.easy4j.admin.modular.dto.PutSysMenuSortDTO;
import cn.easy4j.admin.modular.entity.SysMenu;
import cn.easy4j.admin.modular.entity.SysUser;
import cn.easy4j.admin.modular.enums.SysMenuTypeEnum;
import cn.easy4j.admin.modular.mapper.SysMenuMapper;
import cn.easy4j.common.exception.BusinessException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author yangzongmin
 * @since 2019-08-13
 */
@Service
public class SysMenuService extends ServiceImpl<SysMenuMapper, SysMenu> {

    @Resource
    private SysUserService sysUserService;

    /**
     * 根据用户ID查询所有权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    public Set<String> selectPermissionsByUserId(Long userId) {
        SysUser sysUser = sysUserService.getById(userId);
        if (sysUser == null) {
            return Collections.emptySet();
        }
        if (AdminConstant.ADMIN_ACCOUNT.equalsIgnoreCase(sysUser.getAccount())) {
            // admin账号拥有所有权限
            return Optional.ofNullable(baseMapper.selectAllPermissions()).orElse(Collections.emptySet());
        }
        return Optional.ofNullable(baseMapper.selectPermissionsByUserId(userId)).orElse(Collections.emptySet());
    }

    public List<SysMenu> listSysMenu(GetSysMenuDTO getSysMenuDTO) {
        QueryWrapper<Object> wrapper = Wrappers.query()
                .like(StringUtils.isNotBlank(getSysMenuDTO.getName()), "m.name", getSysMenuDTO.getName())
                .eq(StringUtils.isNotBlank(getSysMenuDTO.getType()), "m.type", getSysMenuDTO.getType());
        List<SysMenu> sysMenus = Optional.of(baseMapper.listSysMenu(wrapper)).orElse(Collections.emptyList());
        return buildSysMenuTree(sysMenus);
    }

    /**
     * 根据菜单集合构建菜单树
     *
     * @param sysMenus 菜单集合
     * @return 树形菜单列表
     */
    public List<SysMenu> buildSysMenuTree(final List<SysMenu> sysMenus) {
        List<SysMenu> resultList = new ArrayList<>();
        for (SysMenu sysMenu : sysMenus) {
            // 根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (sysMenu.getParentId() == 0) {
                recursionSetSubSysMenuList(sysMenus, sysMenu);
                resultList.add(sysMenu);
            }
        }
        if (CollectionUtils.isEmpty(resultList)) {
            return sysMenus;
        }
        return resultList;
    }

    /**
     * 递归列表
     *
     * @param sysMenus 菜单集合
     * @param sysMenu 菜单
     */
    private void recursionSetSubSysMenuList(final List<SysMenu> sysMenus, SysMenu sysMenu) {
        // 得到子节点列表
        List<SysMenu> childList = getChildList(sysMenus, sysMenu);
        sysMenu.setChildren(childList);
        for (SysMenu child : childList) {
            if (hasChild(sysMenus, child)) {
                // 判断是否有子节点
                for (SysMenu n : childList) {
                    recursionSetSubSysMenuList(sysMenus, n);
                }
            }
        }
    }

    private List<SysMenu> getChildList(final List<SysMenu> sysMenus, final SysMenu sysMenu) {
        List<SysMenu> resultList = new ArrayList<>();
        for (SysMenu child : sysMenus) {
            if (Objects.equals(child.getParentId(), sysMenu.getId())) {
                resultList.add(child);
            }
        }
        return resultList;
    }

    private boolean hasChild(final List<SysMenu> sysMenus, final SysMenu sysMenu) {
        List<SysMenu> childList = getChildList(sysMenus, sysMenu);
        return !CollectionUtils.isEmpty(childList);
    }

    public List<SysMenu> selectSysMenuTreeByUserId(Long userId) {

        SysUser sysUser = sysUserService.getById(userId);
        if (sysUser == null) {
            return Collections.emptyList();
        }

        List<SysMenu> parentList = new ArrayList<>();
        List<SysMenu> subList = new ArrayList<>();
        if (AdminConstant.ADMIN_ACCOUNT.equalsIgnoreCase(sysUser.getAccount())) {
            parentList.addAll(this.lambdaQuery().in(SysMenu::getType, SysMenuTypeEnum.D.getValue(), SysMenuTypeEnum.M.getValue()).eq(SysMenu::getParentId, 0L).orderByAsc(SysMenu::getSort).list());
            subList.addAll(this.lambdaQuery().in(SysMenu::getType, SysMenuTypeEnum.D.getValue(), SysMenuTypeEnum.M.getValue()).gt(SysMenu::getParentId, 0L).orderByAsc(SysMenu::getSort).list());
        } else {
            String columnParentId = "m.parent_id";
            String columnType = "m.type";
            parentList.addAll(baseMapper.selectSysUserMenuListByUserId(Wrappers.query().eq("um.user_id", sysUser.getId()).eq(columnParentId, 0).in(columnType, SysMenuTypeEnum.D.getValue(), SysMenuTypeEnum.M.getValue())));
            parentList.addAll(baseMapper.selectSysUserRoleMenuListByUserId(Wrappers.query().eq("ur.user_id", sysUser.getId()).eq(columnParentId, 0).in(columnType, SysMenuTypeEnum.D.getValue(), SysMenuTypeEnum.M.getValue())));

            subList.addAll(baseMapper.selectSysUserMenuListByUserId(Wrappers.query().eq("um.user_id", sysUser.getId()).gt(columnParentId, 0).in(columnType, SysMenuTypeEnum.D.getValue(), SysMenuTypeEnum.M.getValue())));
            subList.addAll(baseMapper.selectSysUserRoleMenuListByUserId(Wrappers.query().eq("ur.user_id", sysUser.getId()).gt(columnParentId, 0).in(columnType, SysMenuTypeEnum.D.getValue(), SysMenuTypeEnum.M.getValue())));
        }

        // 设置为树结构
        Map<String, Long> map = new HashMap<>(16);
        parentList.forEach(sysMenu -> findSubSysMenuList(sysMenu, subList, map));

        return parentList;
    }

    private void findSubSysMenuList(SysMenu sysMenu, List<SysMenu> subList, Map<String, Long> map) {
        // 创建保存子级数据
        List<SysMenu> newList = new ArrayList<>();
        // 遍历查找子级数据
        subList.stream().filter(item -> !map.containsKey(String.valueOf(item.getId())))
                .filter(p -> p.getParentId().equals(sysMenu.getId()))
                .forEach(c -> {
                    // 放入Map 集合，递归循环时可以跳过这个子级数据，提高执行效率
                    map.put(String.valueOf(c.getId()), c.getParentId());
                    // 获取当前权限的子级权限
                    findSubSysMenuList(c, subList, map);
                    // 加入子级数据到集合
                    newList.add(c);
                });
        // 将子级数据设置到父级数据中
        sysMenu.setChildren(newList);
    }

    public Boolean deleteByIds(Set<Long> ids) {

        if (CollectionUtils.isEmpty(ids)) {
            return Boolean.FALSE;
        }
        this.removeByIds(ids);
        this.lambdaUpdate().in(SysMenu::getParentId, ids).remove();
        return Boolean.TRUE;
    }

    public Boolean insertSysMenu(PostSysMenuDTO dto) {
        synchronized (this) {
            SysMenu sysMenuByName = selectByParentIdAndName(dto.getParentId(), dto.getName());

            if (Objects.nonNull(sysMenuByName)) {
                throw new BusinessException("同级别菜单名称已经存在");
            }

            SysMenu sysMenuByPid = this.getById(dto.getParentId());
            if (Objects.nonNull(sysMenuByPid) && Objects.equals(sysMenuByPid.getType(), SysMenuTypeEnum.B.getValue())) {
                throw new BusinessException("不能为按钮菜单添加子元素");
            }
            SysMenu sysMenuForInsert = new SysMenu();
            BeanUtils.copyProperties(dto, sysMenuForInsert);
            return this.save(sysMenuForInsert);
        }
    }

    public Boolean updateSysMenuById(PutSysMenuDTO dto) {
        if (Objects.equals(dto.getParentId(), dto.getId())) {
            throw new BusinessException("上级菜单不能是自己");
        }

        SysMenu sysMenuByName = this.lambdaQuery().eq(SysMenu::getParentId, dto.getParentId()).eq(SysMenu::getName, dto.getName()).ne(SysMenu::getId, dto.getId()).one();

        if (Objects.nonNull(sysMenuByName)) {
            throw new BusinessException("同级别菜单名称已经存在");
        }

        SysMenu sysMenuForUpdate = new SysMenu();
        BeanUtils.copyProperties(dto, sysMenuForUpdate);
        return this.updateById(sysMenuForUpdate);
    }

    public List<SysMenu> selectByIds(Set<Long> menuIds) {
        return Optional.ofNullable(this.lambdaQuery().in(SysMenu::getId, menuIds).list()).orElse(Collections.emptyList());
    }

    public Boolean sort(List<PutSysMenuSortDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return Boolean.FALSE;
        }

        for (PutSysMenuSortDTO putSysMenuSortDTO : dtoList) {
            SysMenu sysMenuForUpdate = new SysMenu();
            sysMenuForUpdate.setId(putSysMenuSortDTO.getMenuId());
            sysMenuForUpdate.setSort(putSysMenuSortDTO.getSort());
            this.updateById(sysMenuForUpdate);
        }
        return Boolean.TRUE;

    }

    public SysMenu selectByParentIdAndName(Long parentId, String name) {
        return this.lambdaQuery().eq(SysMenu::getParentId, parentId).eq(SysMenu::getName, name).one();
    }

}
