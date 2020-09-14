package cn.easy4j.wxmp.modular.controller;

import cn.easy4j.wxmp.modular.service.impl.SysWxMpUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author yangzongmin
 * @since 2020/5/15 22:34
 */
@Slf4j
@Api(tags = "微信公众号粉丝管理")
@RestController
@RequestMapping("/wxmp/user/{appid}")
public class WxMpUserController {

    @Resource
    private SysWxMpUserService sysWxMpUserService;

    @ApiOperation("全量下载粉丝数据")
    @PostMapping("/downloadAllUsers")
    public void downloadAllUsers(@PathVariable String appid) {
        sysWxMpUserService.downloadAllUsers(appid);
    }

}
