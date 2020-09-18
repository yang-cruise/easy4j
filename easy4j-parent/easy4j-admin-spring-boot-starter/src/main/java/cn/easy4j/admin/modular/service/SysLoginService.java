package cn.easy4j.admin.modular.service;

import cn.easy4j.admin.core.constant.AdminConstant;
import cn.easy4j.admin.core.constant.SysConfigConstant;
import cn.easy4j.admin.core.security.JwtUtil;
import cn.easy4j.admin.core.security.LoginPrincipal;
import cn.easy4j.admin.core.security.NotBindWechatException;
import cn.easy4j.admin.modular.dto.GetTokenByAccountDTO;
import cn.easy4j.admin.modular.entity.LoginUser;
import cn.easy4j.admin.modular.vo.AccountConfigVO;
import cn.easy4j.common.enums.HttpStatusEnum;
import cn.easy4j.common.exception.BusinessException;
import cn.easy4j.common.response.FailedResult;
import cn.easy4j.common.response.Result;
import cn.easy4j.common.response.SuccessResult;
import cn.easy4j.framework.util.ApplicationUtil;
import cn.easy4j.framework.util.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.Cache;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author ChenYichen
 * @since 2020/2/9
 */
@Service
@Slf4j
public class SysLoginService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private SysWxOpenUserService sysWxOpenUserService;

    @Resource
    private SysConfigService sysConfigService;

    @Resource(name = "captchaCache")
    private Cache captchaCache;

    @Resource(name = "securityCache")
    private Cache securityCache;

    @Resource(name = "accountCache")
    private Cache accountCache;

    public Result<Object> loginByAccount(@NotNull GetTokenByAccountDTO dto) {
        // 账户锁定验证
        int lockAccountCount = this.getLockAccountCount();
        Integer accountFailCount = accountCache.get(dto.getAccount(), Integer.class);
        if (Objects.nonNull(accountFailCount) && accountFailCount >= lockAccountCount) {
            throw new BusinessException(String.format("密码错误%s次，锁定10分钟", lockAccountCount));
        }

        // 安全验证
        String securityVerifyCacheKey = this.getSecurityVerifyCacheKey();
        Integer failCount = securityCache.get(securityVerifyCacheKey, Integer.class);
        AccountConfigVO accountLoginConfig = sysConfigService.getAccountLoginConfig();
        if (Objects.nonNull(failCount) && failCount >= accountLoginConfig.getShowCaptchaCount()) {
            if (StringUtils.isBlank(dto.getUuid()) || StringUtils.isBlank(dto.getCaptcha())) {
                return new FailedResult<>(HttpStatusEnum.BAD_CAPTCHA.code(), HttpStatusEnum.BAD_CAPTCHA.msg());
            }
            String captcha = captchaCache.get(dto.getUuid(), String.class);
            if (StringUtils.isBlank(captcha) || !StringUtils.equalsIgnoreCase(captcha, dto.getCaptcha())) {
                return new FailedResult<>(HttpStatusEnum.BAD_CAPTCHA.code(), HttpStatusEnum.BAD_CAPTCHA.msg());
            }
        }

        LoginPrincipal principal = new LoginPrincipal(SysConfigConstant.LoginType.ACCOUNT, dto.getAccount());
        LoginUser loginUser = getLoginUser(principal, dto.getPassword());
        if (StringUtils.isNotBlank(dto.getOpenId())) {
            sysWxOpenUserService.bindWechatByOpenId(loginUser.getId(), dto.getOpenId());
        }
        return new SuccessResult<>(JwtUtil.createToken(loginUser.getId(), loginUser.getUsername()));
    }

    public Result<Object> loginByWechat(@NonNull String code) {
        LoginPrincipal principal = new LoginPrincipal(SysConfigConstant.LoginType.WECHAT, code);
        LoginUser loginUser;
        try {
            // 为了通过DaoAuthenticationProvider的密码验证，微信登录时直接设置为默认密码
            loginUser = getLoginUser(principal, AdminConstant.DEFAULT_PASSWORD);
        } catch (NotBindWechatException e) {
            log.info("登录失败，class = [{}], msg = [{}], openId = [{}]", e.getClass().getSimpleName(), e.getMessage(), e.getUserInfo());
            return new FailedResult<>(HttpStatusEnum.NOT_BIND_ACCOUNT.code(), HttpStatusEnum.NOT_BIND_ACCOUNT.msg(), e.getUserInfo());
        }
        return new SuccessResult<>(JwtUtil.createToken(loginUser.getId(), loginUser.getUsername()));
    }

    public String getSecurityVerifyCacheKey() {
        HttpServletRequest request = ApplicationUtil.getRequest();
        String str = IpUtil.getIp(request) + (Objects.isNull(request) ? "" : request.getHeader(HttpHeaders.USER_AGENT));
        return DigestUtils.md5DigestAsHex(str.getBytes(StandardCharsets.UTF_8));
    }

    private LoginUser getLoginUser(LoginPrincipal principal, String credentials) throws NotBindWechatException {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(principal, credentials);
        String securityVerifyCacheKey = this.getSecurityVerifyCacheKey();
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            addBadCredentialsCountBySecurity(securityVerifyCacheKey);
            throw new BusinessException(getPasswordErrorMsg(principal));
        } catch (NotBindWechatException e) {
            throw e;
        } catch (Exception e) {
            addBadCredentialsCountBySecurity(securityVerifyCacheKey);
            log.info("登录失败，principal = [{}], msg = [{}]", principal.getPrincipal(), e.getMessage());
            throw new BusinessException(e.getMessage());
        }

        // 清除缓存
        securityCache.evict(securityVerifyCacheKey);
        accountCache.evict(principal.getPrincipal());

        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return loginUser;
    }

    private String getPasswordErrorMsg(LoginPrincipal principal) {
        Integer failCount = accountCache.get(principal.getPrincipal(), Integer.class);
        failCount = Objects.isNull(failCount) ? 1 : failCount + 1;
        accountCache.put(principal.getPrincipal(), failCount);
        int lockAccountCount = getLockAccountCount();
        int surplusCount = lockAccountCount - failCount;
        return surplusCount > 0 ? String.format("密码错误，还剩%s次机会", surplusCount) : String.format("密码错误%s次，锁定10分钟", lockAccountCount);
    }

    private void addBadCredentialsCountBySecurity(String key) {
        Integer failCount = securityCache.get(key, Integer.class);
        failCount = Objects.isNull(failCount) ? 1 : failCount + 1;
        securityCache.put(key, failCount);
    }

    private int getLockAccountCount() {
        AccountConfigVO accountLoginConfig = sysConfigService.getAccountLoginConfig();
        Integer lockAccountCount = accountLoginConfig.getLockAccountCount();
        return Objects.isNull(lockAccountCount) ? Integer.valueOf(5) : lockAccountCount;
    }
}
