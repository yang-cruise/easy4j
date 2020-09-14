package cn.easy4j.admin.modular.service;

import cn.easy4j.admin.core.constant.SysConfigConstant;
import cn.easy4j.admin.modular.dto.PostSysConfigDTO;
import cn.easy4j.admin.modular.dto.PutSysConfigDTO;
import cn.easy4j.admin.modular.entity.SysConfig;
import cn.easy4j.admin.modular.mapper.SysConfigMapper;
import cn.easy4j.admin.modular.vo.AccountConfigVO;
import cn.easy4j.admin.modular.vo.LoginConfigVO;
import cn.easy4j.admin.modular.vo.SiteConfigVO;
import cn.easy4j.admin.modular.vo.WechatConfigVO;
import cn.easy4j.framework.util.JacksonUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * 系统配置 服务类
 *
 * @author ChenYichen
 * @since 2020-02-11
 */
@Service
public class SysConfigService extends ServiceImpl<SysConfigMapper, SysConfig> {

    @Resource
    private SysConfigCacheService sysConfigCacheService;

    public SysConfig selectByConfigKey(final String configKey) {
        return this.lambdaQuery().eq(SysConfig::getConfigKey, configKey).one();
    }

    public Boolean insertSysConfig(@NonNull final PostSysConfigDTO dto) {
        synchronized (this) {
            SysConfig sysConfig = this.selectByConfigKey(dto.getConfigKey());
            Assert.isNull(sysConfig, String.format("关键字[%s]已经存在", dto.getConfigKey()));

            SysConfig sysConfigForInsert = new SysConfig();
            BeanUtils.copyProperties(dto, sysConfigForInsert);
            return save(sysConfigForInsert);
        }
    }

    public Boolean updateSysConfigByConfigKey(@NonNull final PutSysConfigDTO dto) {
        sysConfigCacheService.clearCacheByConfigKey(dto.getConfigKey());
        return this.lambdaUpdate().set(SysConfig::getConfigContent, dto.getConfigContent())
                .eq(SysConfig::getConfigKey, dto.getConfigKey()).update();
    }

    public AccountConfigVO getAccountLoginConfig() {
        AccountConfigVO config = new AccountConfigVO();
        String configContent = sysConfigCacheService.getConfigContentByConfigKey(SysConfigConstant.Key.LOGIN_CONFIG);
        JsonNode node = JacksonUtil.readValue(configContent);
        if (node.isArray()) {
            for (JsonNode next : node) {
                if (StringUtils.equals(SysConfigConstant.LoginType.ACCOUNT, next.path("type").asText())) {
                    JsonNode params = next.path("params");
                    config.setLockAccountCount(params.path("lockAccountCount").asInt());
                    config.setShowCaptchaCount(params.path("showCaptchaCount").asInt());
                    return config;
                }
            }
        }
        return config;
    }

    public WechatConfigVO getWechatLoginConfig() {
        WechatConfigVO config = new WechatConfigVO();
        String configContent = sysConfigCacheService.getConfigContentByConfigKey(SysConfigConstant.Key.LOGIN_CONFIG);
        JsonNode node = JacksonUtil.readValue(configContent);
        if (node.isArray()) {
            for (JsonNode next : node) {
                if (StringUtils.equals(SysConfigConstant.LoginType.WECHAT, next.path("type").asText())) {
                    JsonNode params = next.path("params");
                    config.setAppId(params.path("appId").asText());
                    config.setAppSecret(params.path("appSecret").asText());
                    config.setRedirectUri(params.path("redirectUri").asText());
                    return config;
                }
            }
        }
        return config;
    }

    public SiteConfigVO getSiteConfig() {
        String configContent = sysConfigCacheService.getConfigContentByConfigKey(SysConfigConstant.Key.SITE_CONFIG);
        return JacksonUtil.parse(configContent, SiteConfigVO.class);
    }

    public List<LoginConfigVO> getLoginConfig() {
        String configContent = sysConfigCacheService.getConfigContentByConfigKey(SysConfigConstant.Key.LOGIN_CONFIG);
        List<LoginConfigVO> loginConfigList = Arrays.asList(JacksonUtil.parse(configContent, LoginConfigVO[].class));
        loginConfigList.sort(Comparator.comparing(LoginConfigVO::getSort));
        return loginConfigList;
    }

}
