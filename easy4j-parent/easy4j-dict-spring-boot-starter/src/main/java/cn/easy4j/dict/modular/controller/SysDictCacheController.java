package cn.easy4j.dict.modular.controller;

import cn.easy4j.dict.modular.entity.SysDictItem;
import cn.easy4j.dict.modular.service.SysDictCacheService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yangzongmin
 * @since 2020/6/2 16:32
 */
@Api(tags = "1006.数据字典（带缓存）")
@RestController
@RequestMapping(value = "/sys_dicts/cache")
public class SysDictCacheController {

    @Resource
    private SysDictCacheService sysDictCacheService;

    @ApiImplicitParam(name = "code", value = "字典编码", required = true, paramType = "query")
    @ApiOperation(value = "查询数据字典")
    @GetMapping("/items")
    public List<SysDictItem> getItems(@RequestParam String code) {
        return sysDictCacheService.selectSysDictItemListByCode(code);
    }

    @ApiOperation(value = "删除数据字典缓存")
    @DeleteMapping
    public void delete() {
        sysDictCacheService.clearCacheAll();
    }

}
