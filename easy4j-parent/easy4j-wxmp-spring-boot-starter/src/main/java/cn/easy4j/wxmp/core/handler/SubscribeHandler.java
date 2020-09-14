package cn.easy4j.wxmp.core.handler;

import cn.easy4j.wxmp.modular.entity.SysWxMpUser;
import cn.easy4j.wxmp.modular.service.Easy4jWxMpHandler;
import cn.easy4j.wxmp.modular.service.impl.SysWxMpUserService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author ChenYichen
 */
@Slf4j
@Component
public class SubscribeHandler implements WxMpMessageHandler {

    @Resource
    private SysWxMpUserService sysWxMpUserService;

    @Resource
    private Easy4jWxMpHandler easy4jWxMpHandler;

    @Resource
    private ScanHandler scanHandler;

    /**
     * 扫描带参数二维码事件，用户未关注时，进行关注后的事件推送
     */
    private static final String EVENT_KEY_PREFIX_QRSCENE = "qrscene_";

    private static final Integer UN_AUTH_CODE = 48001;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) {

        log.info("新关注用户 OPENID: " + wxMessage.getFromUser());
        WxMpXmlOutMessage responseResult = null;
        try {
            WxMpUser userWxInfo = weixinService.getUserService().userInfo(wxMessage.getFromUser(), null);
            if (userWxInfo == null) {
                return null;
            }
            String appId = weixinService.getWxMpConfigStorage().getAppId();
            SysWxMpUser sysWxMpUser = sysWxMpUserService.saveOrUpdateByWxUserInfo(userWxInfo, appId);
            responseResult = easy4jWxMpHandler.handleSubscribe(wxMessage, context, weixinService, sessionManager, sysWxMpUser);

        } catch (WxErrorException e) {
            if (e.getError().getErrorCode() == UN_AUTH_CODE) {
                log.info("该公众号没有获取用户信息权限！");
            }
        }
        WxMpXmlOutMessage specialRes = null;
        try {
            specialRes = this.handleSpecial(wxMessage, weixinService);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null == specialRes ? responseResult : specialRes;
    }

    /**
     * 处理特殊请求，比如如果是扫码进来的，可以做相应处理
     */
    private WxMpXmlOutMessage handleSpecial(WxMpXmlMessage wxMessage, WxMpService wxMpService) throws WxErrorException {
        String eventKey = this.getEventKey(wxMessage);
        return scanHandler.routeByEventKey(eventKey, wxMpService, wxMessage);
    }

    private String getEventKey(WxMpXmlMessage wxMpXmlMessage) {
        String eventKey = wxMpXmlMessage.getEventKey();
        if (StringUtils.isBlank(eventKey)) {
            return null;
        }

        if (!eventKey.startsWith(EVENT_KEY_PREFIX_QRSCENE)) {
            log.warn("未知的扫码关注事件：{}", eventKey);
            return null;
        }

        // 去掉微信添加的qrscene_前缀，给scanHandler处理
        eventKey = eventKey.substring(EVENT_KEY_PREFIX_QRSCENE.length());
        return eventKey;
    }
}
