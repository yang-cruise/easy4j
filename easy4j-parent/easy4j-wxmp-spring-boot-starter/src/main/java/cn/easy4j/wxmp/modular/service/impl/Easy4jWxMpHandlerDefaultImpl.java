package cn.easy4j.wxmp.modular.service.impl;

import cn.easy4j.wxmp.modular.entity.SysWxMpUser;
import cn.easy4j.wxmp.modular.service.Easy4jWxMpHandler;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;

import java.util.Map;

/**
 * @author ChenYichen
 */
public class Easy4jWxMpHandlerDefaultImpl implements Easy4jWxMpHandler {

    @Override
    public Object handleAuthorization(WxMpOAuth2AccessToken wxMpToken, SysWxMpUser sysWxMpUser, Map<String, Object> extParams) {
        return null;
    }

    @Override
    public WxMpXmlOutMessage handleSubscribe(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager, SysWxMpUser sysWxMpUser) {
        return null;
    }

    @Override
    public WxMpXmlOutMessage handleUnSubscribe(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager, String openId) {
        return null;
    }

    @Override
    public WxMpXmlOutMessage handleScanQrCode(String eventKey, WxMpService wxMpService, WxMpXmlMessage wxMpXmlMessage, SysWxMpUser sysWxMpUser) {
        return null;
    }

    @Override
    public WxMpXmlOutMessage handleMenu(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) {
        return null;
    }

    @Override
    public WxMpXmlOutMessage handleMsg(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) {
        return null;
    }
}
