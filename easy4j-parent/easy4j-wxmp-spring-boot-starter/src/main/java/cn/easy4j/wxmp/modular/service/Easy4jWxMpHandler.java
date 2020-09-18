package cn.easy4j.wxmp.modular.service;

import cn.easy4j.wxmp.modular.entity.SysWxMpUser;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;

import java.util.Map;

/**
 * 微信授权扩展接口
 *
 * @author ChenYichen
 * @since 2020/3/24
 */
public interface Easy4jWxMpHandler {

    /**
     * 微信授权登陆 扩展方法
     *
     * @param wxMpToken   微信token
     * @param sysWxMpUser 粉丝信息
     * @param extParams   扩展字段
     * @return 指定类型
     */
    Object handleAuthorization(WxMpOAuth2AccessToken wxMpToken, SysWxMpUser sysWxMpUser, Map<String, Object> extParams);

    /**
     * 微信关注公众号扩展方法
     *
     * @param wxMpXmlMessage 关注的消息
     * @param context        上下文
     * @param wxMpService    微信服务API
     * @param sessionManager sessionManager
     * @param sysWxMpUser    关注的粉丝
     * @return 指定类型
     */
    WxMpXmlOutMessage handleSubscribe(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager, SysWxMpUser sysWxMpUser);

    /**
     * 取消关注扩展方法
     *
     * @param wxMpXmlMessage wxMpXmlMessage
     * @param context        context
     * @param wxMpService    wxMpService
     * @param sessionManager sessionManager
     * @param openId         openId
     * @return WxMpXmlOutMessage
     */
    WxMpXmlOutMessage handleUnSubscribe(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager, String openId);

    /**
     * 扫码扩展方法
     *
     * @param eventKey       eventKey
     * @param wxMpService    wxMpService
     * @param wxMpXmlMessage wxMpXmlMessage
     * @param sysWxMpUser    sysWxMpUser
     * @return WxMpXmlOutMessage
     * @throws WxErrorException 微信异常信息
     */
    WxMpXmlOutMessage handleScanQrCode(String eventKey, WxMpService wxMpService, WxMpXmlMessage wxMpXmlMessage, SysWxMpUser sysWxMpUser) throws WxErrorException;

    /**
     * 处理menu事件
     *
     * @param wxMessage      wxMessage
     * @param context        context
     * @param wxMpService    wxMpService
     * @param sessionManager sessionManager
     * @return WxMpXmlOutMessage
     */
    WxMpXmlOutMessage handleMenu(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager);

    /**
     * 处理关键字
     *
     * @param wxMessage      wxMessage
     * @param context        context
     * @param wxMpService    wxMpService
     * @param sessionManager sessionManager
     * @return WxMpXmlOutMessage
     */
    WxMpXmlOutMessage handleMsg(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager);
}
