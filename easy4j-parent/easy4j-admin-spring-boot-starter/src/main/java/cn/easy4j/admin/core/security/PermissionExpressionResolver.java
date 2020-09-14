package cn.easy4j.admin.core.security;

import cn.easy4j.admin.modular.entity.SysMenu;
import cn.easy4j.admin.modular.enums.SysMenuTypeEnum;
import cn.easy4j.admin.modular.service.SysMenuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 权限EL表达式解析器实现类，如果方法上标注@PreAuthorize注解，
 * 并命中hasPermission('', '', '') EL表达式的，保存到sys_menu表中
 * @author yangzongmin
 * @since 2020/6/12 22:30
 */
@Slf4j
@Component
public class PermissionExpressionResolver {

    @Resource
    private SysMenuService sysMenuService;

    public int resolveExpression(List<String> expressions) {
        int count = 0;
        if (CollectionUtils.isEmpty(expressions)) {
            return count;
        }

        List<SysMenu> sysMenus = sysMenuService.list();
        Set<String> existedPermissionCodes = getExistedPermissionCodes(sysMenus);
        for (String expression : expressions) {
            String[] contents = StringUtils.substringsBetween(expression, "'", "'");
            if (contents.length != 3) {
                continue;
            }

            String permissionCode = contents[2];
            if (existedPermissionCodes.contains(permissionCode)) {
                continue;
            }

            String menuName = contents[0];
            SysMenu sysMenuFromDb = getByMenuName(sysMenus, menuName);
            if (Objects.isNull(sysMenuFromDb)) {
                log.warn("未查到名称为[{}]的菜单，权限[{}]未自动录入数据库", menuName, expression);
                continue;
            }

            SysMenu sysMenuForInsert = new SysMenu();
            sysMenuForInsert.setName(contents[1]);
            sysMenuForInsert.setType(SysMenuTypeEnum.B.getValue());
            sysMenuForInsert.setParentId(sysMenuFromDb.getId());
            sysMenuForInsert.setPerms(permissionCode);
            sysMenuForInsert.setSort(1);
            if (sysMenuService.save(sysMenuForInsert)) {
                // 保存成功后，加入已存在的权限标识中，防止往数据库中重复录入
                existedPermissionCodes.add(permissionCode);
                count++;
            }
        }
        return count;
    }

    private Set<String> getExistedPermissionCodes(List<SysMenu> sysMenuList) {
        if (CollectionUtils.isEmpty(sysMenuList)) {
            return new HashSet<>();
        }
        return sysMenuList.parallelStream().map(SysMenu::getPerms).collect(Collectors.toSet());
    }

    private SysMenu getByMenuName(List<SysMenu> sysMenuList, String name) {
        if (CollectionUtils.isEmpty(sysMenuList)) {
            return null;
        }
        return sysMenuList.parallelStream()
                .filter(item -> StringUtils.equals(SysMenuTypeEnum.M.getValue(), item.getType())
                        && StringUtils.equals(item.getName(), name))
                .findAny().orElse(null);
    }

}
