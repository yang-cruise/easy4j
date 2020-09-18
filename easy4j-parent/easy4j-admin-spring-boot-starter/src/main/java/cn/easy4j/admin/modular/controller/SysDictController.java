package cn.easy4j.admin.modular.controller;

import cn.easy4j.dict.modular.dto.*;
import cn.easy4j.dict.modular.entity.SysDict;
import cn.easy4j.dict.modular.entity.SysDictItem;
import cn.easy4j.dict.modular.service.SysDictItemService;
import cn.easy4j.dict.modular.service.SysDictService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
 * @since 2020/2/6
 */
@Api(tags = "1005.数据字典")
@RestController
@RequestMapping(value = "/sys_dicts")
public class SysDictController {

    @Resource
    private SysDictService sysDictService;

    @Resource
    private SysDictItemService sysDictItemService;

    @ApiOperation(value = "分页查询数据字典列表")
    @GetMapping("/page")
    @PreAuthorize("hasPermission('数据字典', '查询字典', 'sys:dict:select')")
    public IPage<SysDict> getPage(Page<SysDict> page, GetSysDictDTO dto) {
        return sysDictService.pageList(page, dto);
    }

    @ApiOperation(value = "根据字典标识查询字典项列表")
    @GetMapping("/items")
    public List<SysDictItem> getItems(@RequestParam String code) {
        return sysDictService.selectSysDictItemListByCode(code);
    }

    @ApiOperation(value = "根据字典ID查询字典项列表")
    @GetMapping("/{id}/items")
    public List<SysDictItem> getItemsById(@PathVariable Long id) {
        return sysDictItemService.selectBySysDictId(id);
    }

    @ApiOperation(value = "新增字典")
    @PostMapping
    @PreAuthorize("hasPermission('数据字典', '新增字典', 'sys:dict:insert')")
    public Boolean post(@Validated @RequestBody PostSysDictDTO dto) {
        return sysDictService.insertSysDict(dto);
    }

    @ApiOperation(value = "新增字典项")
    @PostMapping("/items")
    @PreAuthorize("hasPermission('数据字典', '新增字典', 'sys:dict:insert')")
    public Boolean postItems(@Validated @RequestBody PostSysDictItemDTO dto) {
        return sysDictItemService.insertSysDictItem(dto);
    }

    @ApiOperation(value = "更新字典")
    @PutMapping
    @PreAuthorize("hasPermission('数据字典', '修改字典', 'sys:dict:update')")
    public Boolean put(@Validated @RequestBody PutSysDictDTO dto) {
        return sysDictService.updateSysDictById(dto);
    }

    @ApiOperation(value = "更新字典项")
    @PutMapping("/items")
    @PreAuthorize("hasPermission('数据字典', '修改字典', 'sys:dict:update')")
    public Boolean update(@Validated @RequestBody PutSysDictItemDTO dto) {
        return sysDictItemService.updateSysDictItemById(dto);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("/{ids}")
    @PreAuthorize("hasPermission('数据字典', '删除字典', 'sys:dict:delete')")
    public Boolean delete(@PathVariable Set<Long> ids) {
        return sysDictService.deleteSysDictById(ids);
    }

    @ApiOperation(value = "删除字典项")
    @DeleteMapping("/items/{ids}")
    @PreAuthorize("hasPermission('数据字典', '删除字典', 'sys:dict:delete')")
    public Boolean deleteItems(@PathVariable Set<Long> ids) {
        return sysDictItemService.removeByIds(ids);
    }
}
