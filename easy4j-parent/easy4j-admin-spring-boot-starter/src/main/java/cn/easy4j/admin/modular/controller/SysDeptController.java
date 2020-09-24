package cn.easy4j.admin.modular.controller;

import cn.easy4j.admin.core.util.SecurityUtil;
import cn.easy4j.admin.modular.dto.PostSysDeptDTO;
import cn.easy4j.admin.modular.dto.PutSysDeptDTO;
import cn.easy4j.admin.modular.entity.SysDept;
import cn.easy4j.admin.modular.service.SysDeptService;
import cn.easy4j.framework.annotation.CheckRepeatSubmit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @author ChenYichen
 * @since 2020/2/10
 */
@Api(tags = "1002.系统部门")
@RestController
@RequestMapping(value = "/sys_depts")
public class SysDeptController {

    @Resource
    private SysDeptService sysDeptService;

    @ApiOperation(value = "查询自己的部门和下级部门 （子部门级联）")
    @GetMapping("/self")
    public List<SysDept> getSelf() {
        return sysDeptService.listSelfDept(SecurityUtil.getLoginUser().getDeptId());
    }

    @ApiOperation(value = "查询全部部门树")
    @GetMapping("/tree")
    @PreAuthorize("hasPermission('用户管理', '查询部门', 'sys:dept:select')")
    public List<SysDept> getTree() {
        return sysDeptService.selectAllTree();
    }

    @ApiOperation(value = "新增部门")
    @PostMapping
    @CheckRepeatSubmit
    @PreAuthorize("hasPermission('用户管理', '新增部门', 'sys:dept:insert')")
    public Boolean post(@Validated @RequestBody PostSysDeptDTO dto) {
        return sysDeptService.insertSysDept(dto);
    }

    @ApiOperation(value = "更新部门")
    @PutMapping
    @PreAuthorize("hasPermission('用户管理', '修改部门', 'sys:dept:update')")
    public Boolean put(@Validated @RequestBody PutSysDeptDTO dto) {
        return sysDeptService.updateSysDept(dto);
    }

    @ApiOperation(value = "删除部门")
    @DeleteMapping("/{ids}")
    @PreAuthorize("hasPermission('用户管理', '删除部门', 'sys:dept:delete')")
    public Boolean delete(@PathVariable Set<Long> ids) {
        return sysDeptService.batchDeleteSysDept(ids);
    }

}
