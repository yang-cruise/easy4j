package cn.easy4j.admin.modular.service;

import cn.easy4j.admin.modular.dto.PostSysDeptDTO;
import cn.easy4j.admin.modular.dto.PutSysDeptDTO;
import cn.easy4j.admin.modular.entity.SysDept;
import cn.easy4j.admin.modular.mapper.SysDeptMapper;
import cn.easy4j.common.exception.BusinessException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 服务类
 *
 * @author ChenYichen
 * @since 2020-02-10
 */
@Service
public class SysDeptService extends ServiceImpl<SysDeptMapper, SysDept> {

    public List<SysDept> selectAllTree() {
        List<SysDept> parentList = this.lambdaQuery().eq(SysDept::getParentId, 0L).orderByAsc(SysDept::getSort).list();
        List<SysDept> subList = this.lambdaQuery().gt(SysDept::getParentId, 0L).orderByAsc(SysDept::getSort).list();
        // 设置为树结构
        Map<String, Long> map = new HashMap<>(16);
        parentList.forEach(sysMenu -> findSubSysDeptList(sysMenu, subList, map));
        return parentList;
    }

    private void findSubSysDeptList(SysDept sysDept, List<SysDept> subList, Map<String, Long> map) {
        // 创建保存子级数据
        List<SysDept> newList = new ArrayList<>();
        // 遍历查找子级数据
        subList.stream().filter(item -> !map.containsKey(String.valueOf(item.getId())))
                .filter(p -> p.getParentId().equals(sysDept.getId()))
                .forEach(c -> {
                    // 放入Map 集合，递归循环时可以跳过这个子级数据，提高执行效率
                    map.put(String.valueOf(c.getId()), c.getParentId());
                    // 获取当前权限的子级权限
                    findSubSysDeptList(c, subList, map);
                    // 加入子级数据到集合
                    newList.add(c);
                });
        // 将子级数据设置到父级数据中
        sysDept.setSubSysDeptList(CollectionUtils.isEmpty(newList) ? null : newList);
    }

    public Boolean insertSysDept(PostSysDeptDTO postSysDeptDTO) {
        synchronized (this) {
            SysDept sysDeptFromDb = this.lambdaQuery().eq(SysDept::getDeptName, postSysDeptDTO.getDeptName()).one();

            if (Objects.nonNull(sysDeptFromDb)) {
                throw new BusinessException("该部门名称已经存在了！");
            }
            SysDept sysDeptForInsert = new SysDept();
            BeanUtils.copyProperties(postSysDeptDTO, sysDeptForInsert);
            return this.save(sysDeptForInsert);
        }
    }

    public Boolean updateSysDept(PutSysDeptDTO putSysDeptDTO) {

        SysDept sysDeptByName = this.lambdaQuery().eq(SysDept::getDeptName, putSysDeptDTO.getDeptName()).ne(SysDept::getId, putSysDeptDTO.getId()).one();

        if (Objects.nonNull(sysDeptByName)) {
            throw new BusinessException("该部门名称已经存在了！");
        }
        SysDept sysDeptForUpdate = new SysDept();
        BeanUtils.copyProperties(putSysDeptDTO, sysDeptForUpdate);
        return this.updateById(sysDeptForUpdate);
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean batchDeleteSysDept(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Boolean.FALSE;
        }
        for (Long deptId : ids) {
            Assert.isTrue(!Objects.equals(1L, deptId), "顶级部门不允许删除");
            List<SysDept> list = this.lambdaQuery().eq(SysDept::getParentId, deptId).list();
            Assert.isTrue(CollectionUtils.isEmpty(list), "请删除此部门下的成员或子部门后，再删除此部门");
            this.removeById(deptId);
        }
        return Boolean.TRUE;
    }

    public Set<Long> querySubDept(@NonNull Long deptId, @NonNull Set<Long> deptIds) {
        deptIds.add(deptId);
        List<SysDept> list = this.lambdaQuery().eq(SysDept::getParentId, deptId).list();
        if (CollectionUtils.isEmpty(list)) {
            return deptIds;
        }
        for (SysDept sysDept : list) {
            deptIds.addAll(querySubDept(sysDept.getId(), deptIds));
        }
        return deptIds;
    }

    public List<SysDept> listSelfDept(Long deptId) {
        HashSet<Long> deptIdSet = new HashSet<>();
        deptIdSet.add(deptId);
        Set<Long> deptIds = this.querySubDept(deptId, deptIdSet).stream().sorted(Long::compareTo).collect(Collectors.toCollection(LinkedHashSet::new));
        return this.buildSysDeptTree(this.lambdaQuery().in(SysDept::getId, deptIds).list(), deptIds.iterator().next());
    }

    public List<SysDept> buildSysDeptTree(List<SysDept> sysDeptList, Long maxDeptId) {
        List<SysDept> parentList = new ArrayList<>();
        List<SysDept> subList = new ArrayList<>();
        for (SysDept sysDept : sysDeptList) {
            if (sysDept.getId().equals(maxDeptId)) {
                parentList.add(sysDept);
            } else {
                subList.add(sysDept);
            }
        }
        Map<String, Long> map = new HashMap<>(16);
        parentList.forEach(sysMenu -> findSubSysDeptList(sysMenu, subList, map));
        return parentList;
    }
}
