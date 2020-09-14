package cn.easy4j.wxmp.core.handler;

import cn.easy4j.wxmp.modular.service.Easy4jWxMpHandler;
import cn.easy4j.wxmp.modular.service.impl.SysWxMpUserService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author ChenYichen
 */
@Component
@Slf4j
public class UnsubscribeHandler implements WxMpMessageHandler {

    @Resource
    private Easy4jWxMpHandler easy4jWxMpHandler;

    @Resource
    private SysWxMpUserService sysWxMpUserService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService wxMpService,
                                    WxSessionManager sessionManager) {
        String openId = wxMessage.getFromUser();
        sysWxMpUserService.updateUnsubscribeStatusByOpenId(openId);
        easy4jWxMpHandler.handleUnSubscribe(wxMessage, context, wxMpService, sessionManager, openId);
        return null;
    }

}
