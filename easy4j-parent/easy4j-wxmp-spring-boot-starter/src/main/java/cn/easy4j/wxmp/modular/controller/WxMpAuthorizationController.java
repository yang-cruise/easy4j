package cn.easy4j.wxmp.modular.controller;

import cn.easy4j.common.enums.HttpStatusEnum;
import cn.easy4j.common.exception.BusinessException;
import cn.easy4j.wxmp.modular.entity.SysWxMpUser;
import cn.easy4j.wxmp.modular.service.Easy4jWxMpHandler;
import cn.easy4j.wxmp.modular.service.impl.SysWxMpUserService;
import cn.easy4j.wxmp.modular.service.impl.WeixinMpService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;

/**
 * @author ChenYichen
 * @date 2020/3/23
 */
@Api(tags = "微信公众号授权")
@Slf4j
@RestController
@RequestMapping("/wxmp/{appid}")
public class WxMpAuthorizationController {

    @Resource
    private WeixinMpService weixinMpService;

    @Resource
    private SysWxMpUserService sysWxMpUserService;

    @Resource
    private Easy4jWxMpHandler easy4jWxMpHandler;

    private static final String SNS_API_USER_INFO = "snsapi_userinfo";


    @ApiOperation("1、获取签名")
    @PostMapping("/createJsApiSignature")
    public WxJsapiSignature config(@RequestParam String url, @PathVariable(value = "appid") String appid) {
        try {
            WxMpService wxMpService = weixinMpService.getWxMpService(appid);
            return wxMpService.createJsapiSignature(url);
        } catch (WxErrorException e) {
            log.error("创建JS签名异常：", e);
            throw new BusinessException(e.getError().getErrorMsg());
        }
    }

    @ApiOperation("2、获取授权信息")
    @PostMapping("/oauth2getAccessToken")
    public Object oauth2getAccessToken(@RequestParam Map<String, Object> extParams,
                                       @RequestParam String state,
                                       @RequestParam String code,
                                       @PathVariable(value = "appid") String appid) {
        WxMpOAuth2AccessToken wxMpToken;
        WxMpService wxMpService = weixinMpService.getWxMpService(appid);
        Assert.isTrue(StringUtils.isNoneBlank(code, appid, state), "参数错误");
        try {
            wxMpToken = wxMpService.oauth2getAccessToken(code);

            WxMpUser wxMpUserInfo;
            SysWxMpUser sysWxMpUser;
            if (Objects.equals(SNS_API_USER_INFO, state)) {
                wxMpUserInfo = wxMpService.oauth2getUserInfo(wxMpToken, null);
                sysWxMpUser = sysWxMpUserService.saveOrUpdateByWxUserInfo(wxMpUserInfo, appid);
            } else {
                sysWxMpUser = sysWxMpUserService.saveOrUpdateByOpenId(wxMpToken.getOpenId(), appid);
            }

            Object res = easy4jWxMpHandler.handleAuthorization(wxMpToken, sysWxMpUser, extParams);
            return Objects.isNull(res) ? wxMpToken : res;
        } catch (WxErrorException e) {
            log.error("获取wxMpOAuth2AccessToken异常：", e);
            throw new BusinessException(HttpStatusEnum.PARSE_TOKEN_FAIL.code(), e.getError().getErrorMsg());
        }
    }
}
