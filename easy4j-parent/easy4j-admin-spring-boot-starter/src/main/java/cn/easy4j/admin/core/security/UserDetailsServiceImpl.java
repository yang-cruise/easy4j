package cn.easy4j.admin.core.security;

import cn.easy4j.admin.core.constant.AdminConstant;
import cn.easy4j.admin.core.constant.SysConfigConstant;
import cn.easy4j.admin.modular.entity.LoginUser;
import cn.easy4j.admin.modular.entity.SysUser;
import cn.easy4j.admin.modular.entity.SysWxOpenUser;
import cn.easy4j.admin.modular.enums.SysUserStatusEnum;
import cn.easy4j.admin.modular.service.SysConfigService;
import cn.easy4j.admin.modular.service.SysUserCacheService;
import cn.easy4j.admin.modular.service.SysUserService;
import cn.easy4j.admin.modular.service.SysWxOpenUserService;
import cn.easy4j.admin.modular.vo.WechatConfigVO;
import cn.easy4j.common.enums.HttpStatusEnum;
import cn.easy4j.framework.http.HttpsClientRequestFactory;
import cn.easy4j.framework.util.JacksonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author ChenYichen
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysWxOpenUserService sysWxOpenUserService;

    @Resource
    private SysUserCacheService sysUserCacheService;

    @Resource
    private SysConfigService sysConfigService;

    private static final String GET_USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token={1}&openid={2}";
    private static final String GET_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={1}&secret={2}&code={3}&grant_type=authorization_code";

    private static final RestTemplate REST_TEMPLATE = new RestTemplate(new HttpsClientRequestFactory());

    static {
        REST_TEMPLATE.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        LoginPrincipal loginPrincipal = JacksonUtil.parse(username, LoginPrincipal.class);
        if (Objects.isNull(loginPrincipal)) {
            throw new InternalAuthenticationServiceException("登录失败，请稍后重试");
        }

        switch (loginPrincipal.getType()) {
            case SysConfigConstant.LoginType.ACCOUNT:
                return getLoginUserByAccount(loginPrincipal.getPrincipal());
            case SysConfigConstant.LoginType.WECHAT:
                return getLoginUserByWechat(loginPrincipal.getPrincipal());
            default:
                throw new InternalAuthenticationServiceException("暂不支持此登录方式");
        }
    }

    private LoginUser getLoginUserByAccount(String account) {
        SysUser sysUserFromDb = sysUserService.selectByAccount(account);
        if (Objects.isNull(sysUserFromDb)) {
            throw new InternalAuthenticationServiceException("用户不存在或已被删除");
        }
        if (Objects.equals(sysUserFromDb.getStatus(), SysUserStatusEnum.LOCKED.getValue())) {
            throw new InternalAuthenticationServiceException("账户已被冻结");
        }
        return sysUserCacheService.refreshLoginUserByUserId(sysUserFromDb.getId());
    }

    private LoginUser getLoginUserByWechat(String code) {
        WechatConfigVO config = sysConfigService.getWechatLoginConfig();
        ResponseEntity<String> accessTokenResponse = REST_TEMPLATE.getForEntity(GET_ACCESS_TOKEN_URL, String.class, config.getAppId(), config.getAppSecret(), code);
        this.checkError(accessTokenResponse);
        JsonNode accessTokenNode = JacksonUtil.readValue(accessTokenResponse.getBody());

        String accessToken = accessTokenNode.path("access_token").asText();
        String openId = accessTokenNode.path("openid").asText();
        ResponseEntity<String> userInfoResponse = REST_TEMPLATE.getForEntity(GET_USER_INFO_URL, String.class, accessToken, openId);
        this.checkError(userInfoResponse);

        UserInfoResponse userInfo = JacksonUtil.parse(userInfoResponse.getBody(), UserInfoResponse.class);
        if (Objects.isNull(userInfo)) {
            throw new InternalAuthenticationServiceException("登录失败，请稍后重试");
        }

        SysWxOpenUser sysWxOpenUserFromDb = sysWxOpenUserService.selectByOpenId(userInfo.getOpenId());
        this.saveOrUpdateSysWxOpenUser(userInfo, sysWxOpenUserFromDb);
        if (Objects.isNull(sysWxOpenUserFromDb) || Objects.equals(sysWxOpenUserFromDb.getUserId(), 0L)) {
            throw new NotBindWechatException(HttpStatusEnum.NOT_BIND_ACCOUNT.msg(), userInfo);
        }

        LoginUser loginUser = sysUserCacheService.refreshLoginUserByUserId(sysWxOpenUserFromDb.getUserId());
        if (Objects.isNull(loginUser)) {
            throw new InternalAuthenticationServiceException("用户不存在或已被删除");
        }
        if (Objects.equals(loginUser.getStatus(), SysUserStatusEnum.LOCKED.getValue())) {
            throw new InternalAuthenticationServiceException("账户已被冻结");
        }
        // 为了通过DaoAuthenticationProvider的密码验证，微信登录时直接设置为默认密码
        loginUser.setPassword(new BCryptPasswordEncoder().encode(AdminConstant.DEFAULT_PASSWORD));
        return loginUser;
    }

    private void saveOrUpdateSysWxOpenUser(UserInfoResponse userInfo, SysWxOpenUser sysWxOpenUserFromDb) {
        SysWxOpenUser sysWxOpenUser = new SysWxOpenUser();
        BeanUtils.copyProperties(userInfo, sysWxOpenUser);
        if (Objects.isNull(sysWxOpenUserFromDb)) {
            sysWxOpenUser.setUserId(0L);
            sysWxOpenUserService.save(sysWxOpenUser);
        } else {
            sysWxOpenUser.setId(sysWxOpenUserFromDb.getId()).setUserId(sysWxOpenUserFromDb.getUserId());
            sysWxOpenUserService.updateById(sysWxOpenUser);
        }
    }

    private void checkError(ResponseEntity<String> entity) {
        if (entity.getStatusCode().value() != HttpStatus.OK.value() || StringUtils.isBlank(entity.getBody())) {
            throw new InternalAuthenticationServiceException("登录失败，请刷新重试");
        }

        String errMsg = JacksonUtil.readValue(entity.getBody()).path("errmsg").asText();
        if (StringUtils.isNotBlank(errMsg)) {
            throw new InternalAuthenticationServiceException(errMsg);
        }
    }

}
