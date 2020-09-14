package cn.easy4j.admin.modular.controller;

import cn.easy4j.admin.modular.dto.GetTokenByAccountDTO;
import cn.easy4j.admin.modular.service.SysConfigService;
import cn.easy4j.admin.modular.service.SysLoginService;
import cn.easy4j.admin.modular.vo.AccountConfigVO;
import cn.easy4j.admin.modular.vo.CaptchaVO;
import cn.easy4j.common.exception.BusinessException;
import cn.easy4j.common.response.Result;
import com.wf.captcha.ArithmeticCaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cache.Cache;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.UUID;

/**
 * 登录
 *
 * @author ChenYichen
 */
@Api(tags = "0000.登录")
@RestController
@RequestMapping(value = "/sys_login")
public class SysLoginController {

    @Resource
    private SysLoginService sysLoginService;

    @Resource
    private SysConfigService sysConfigService;

    @Resource(name = "securityCache")
    private Cache securityCache;

    @Resource(name = "captchaCache")
    private Cache captchaCache;

    @ApiOperation(value = "密码登录")
    @GetMapping("/token_by_account")
    public Result<Object> getTokenByAccount(GetTokenByAccountDTO dto) {
        return sysLoginService.loginByAccount(dto);
    }

    @ApiOperation(value = "微信登录")
    @ApiImplicitParam(name = "code", value = "授权码", required = true)
    @GetMapping("/token_by_wechat")
    public Result<Object> getTokenByWechat(@RequestParam(required = false) String code) {
        Assert.hasText(code, "授权码不能为空");
        return sysLoginService.loginByWechat(code);
    }

    @ApiOperation(value = "短信登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", required = true),
            @ApiImplicitParam(name = "captcha", value = "验证码", required = true)
    })
    @GetMapping("/token_by_sms")
    public String getTokenBySms(@RequestParam(required = false) String mobile,
                                    @RequestParam(required = false) String captcha) {
        throw new BusinessException("暂不支持的登录方式");
    }

    @ApiOperation(value = "获取验证码")
    @GetMapping("/captcha")
    public CaptchaVO getCaptcha() {
        CaptchaVO captchaVO = new CaptchaVO().setShow(Boolean.FALSE);

        String securityVerifyCacheKey = sysLoginService.getSecurityVerifyCacheKey();
        Integer failCount = securityCache.get(securityVerifyCacheKey, Integer.class);
        AccountConfigVO accountLoginConfig = sysConfigService.getAccountLoginConfig();
        if (Objects.nonNull(failCount) && failCount >= accountLoginConfig.getShowCaptchaCount()) {
            captchaVO.setShow(Boolean.TRUE);
            ArithmeticCaptcha specCaptcha = new ArithmeticCaptcha(100, 32, 2);
            String captcha = specCaptcha.text().toLowerCase();
            String uuid = UUID.randomUUID().toString();
            captchaCache.put(uuid, captcha);
            captchaVO.setUuid(uuid).setImage(specCaptcha.toBase64());
        }

        return captchaVO;
    }

}
