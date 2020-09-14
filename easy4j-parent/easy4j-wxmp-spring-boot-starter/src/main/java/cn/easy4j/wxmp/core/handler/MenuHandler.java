package cn.easy4j.wxmp.core.handler;

import cn.easy4j.wxmp.modular.service.Easy4jWxMpHandler;
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
public class MenuHandler implements WxMpMessageHandler {

    @Resource
    private Easy4jWxMpHandler easy4jWxMpHandler;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService wxMpService,
                                    WxSessionManager sessionManager) {
        String msg = String.format("type:%s, event:%s, key:%s",
                wxMessage.getMsgType(), wxMessage.getEvent(),
                wxMessage.getEventKey());
        log.debug("msg: 【{}】", msg);
        return easy4jWxMpHandler.handleMenu(wxMessage, context, wxMpService, sessionManager);
    }

}
