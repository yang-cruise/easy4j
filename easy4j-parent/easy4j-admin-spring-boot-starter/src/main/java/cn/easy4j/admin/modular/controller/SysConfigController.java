package cn.easy4j.admin.modular.controller;

import cn.easy4j.admin.modular.dto.PostSysConfigDTO;
import cn.easy4j.admin.modular.dto.PutSysConfigDTO;
import cn.easy4j.admin.modular.service.SysConfigCacheService;
import cn.easy4j.admin.modular.service.SysConfigService;
import cn.easy4j.admin.modular.vo.LoginConfigVO;
import cn.easy4j.admin.modular.vo.SiteConfigVO;
import cn.easy4j.admin.modular.vo.WechatConfigVO;
import cn.easy4j.framework.annotation.CheckRepeatSubmit;
import cn.easy4j.framework.util.JacksonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ChenYichen
 * @since 2020/2/7
 */
@Api(tags = "2001.系统配置")
@RestController
@RequestMapping(value = "/sys_configs")
public class SysConfigController {

    @Resource
    private SysConfigService sysConfigService;

    @Resource
    private SysConfigCacheService sysConfigCacheService;

    @ApiOperation(value = "查询网站配置")
    @GetMapping("/site_config")
    public SiteConfigVO getSiteConfig() {
        return sysConfigService.getSiteConfig();
    }

    @ApiOperation(value = "查询登录配置")
    @GetMapping("/login_config")
    public List<LoginConfigVO> getLoginConfig() {
        return sysConfigService.getLoginConfig();
    }

    @ApiOperation(value = "查询微信配置")
    @GetMapping("/wechat_config")
    public WechatConfigVO getWechatConfig() {
        return sysConfigService.getWechatLoginConfig();
    }

    @ApiOperation(value = "查询系统配置详情")
    @GetMapping("/detail")
    @PreAuthorize("hasPermission('系统配置', '查询配置', 'sys:config:select')")
    public Object getDetail(@RequestParam String configKey) {
        String configContent = sysConfigCacheService.getConfigContentByConfigKey(configKey);
        return JacksonUtil.parse(configContent);
    }

    @ApiOperation(value = "新增系统配置")
    @PostMapping
    @CheckRepeatSubmit
    @PreAuthorize("hasPermission('系统配置', '新增配置', 'sys:config:insert')")
    public Boolean post(@RequestBody PostSysConfigDTO dto) {
        return sysConfigService.insertSysConfig(dto);
    }

    @ApiOperation(value = "更新系统配置")
    @PutMapping
    @PreAuthorize("hasPermission('系统配置', '修改配置', 'sys:config:update')")
    public Boolean put(@RequestBody PutSysConfigDTO dto) {
        return sysConfigService.updateSysConfigByConfigKey(dto);
    }
}
