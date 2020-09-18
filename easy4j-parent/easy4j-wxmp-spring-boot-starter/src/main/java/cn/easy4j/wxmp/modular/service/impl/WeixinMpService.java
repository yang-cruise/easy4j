package cn.easy4j.wxmp.modular.service.impl;

import cn.easy4j.wxmp.modular.entity.SysWxMp;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Objects;

/**
 * @author ChenYichen
 * @since 2020/3/23
 */
@AllArgsConstructor
@Service
public class WeixinMpService {

    private final SysWxMpService sysWxMpService;

    /**
     * k : appId
     * v : WxMpService
     */
    private static final HashMap<String, WxMpService> CACHE = new HashMap<>();

    public WxMpService getWxMpService(String appId) {
        WxMpService wxMpServiceByCache = CACHE.get(appId);
        if (Objects.isNull(wxMpServiceByCache)) {
            SysWxMp sysWxMpByAppId = sysWxMpService.selectByAppId(appId);
            Assert.notNull(sysWxMpByAppId, "未找到该appId对应配置");
            WxMpDefaultConfigImpl configStorage = new WxMpDefaultConfigImpl();
            configStorage.setAppId(sysWxMpByAppId.getAppId());
            configStorage.setSecret(sysWxMpByAppId.getAppSecret());
            configStorage.setToken(sysWxMpByAppId.getToken());
            configStorage.setAesKey(sysWxMpByAppId.getAesKey());
            WxMpServiceImpl wxMpService = new WxMpServiceImpl();
            wxMpService.setWxMpConfigStorage(configStorage);
            CACHE.put(sysWxMpByAppId.getAppId(), wxMpService);
            return wxMpService;
        }
        return wxMpServiceByCache;
    }
}
