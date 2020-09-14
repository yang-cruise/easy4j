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
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author ChenYichen
 */
@Component
@Slf4j
public class ScanHandler implements WxMpMessageHandler {

    @Resource
    private Easy4jWxMpHandler easy4jWxMpHandler;

    @Resource
    private SysWxMpUserService sysWxMpUserService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> map,
                                    WxMpService wxMpService, WxSessionManager wxSessionManager) throws WxErrorException {
        log.info("微信号：" + wxMpXmlMessage.getToUser() + "，扫描二维码事件，用户OPENID: " + wxMpXmlMessage.getFromUser());
        return this.routeByEventKey(wxMpXmlMessage.getEventKey(), wxMpService, wxMpXmlMessage);
    }


    public WxMpXmlOutMessage routeByEventKey(String eventKey, WxMpService wxMpService, WxMpXmlMessage wxMpXmlMessage) throws WxErrorException {

        log.debug("处理微信扫码事件，微信号：{}，OPENID：{}，事件码：{}", wxMpXmlMessage.getToUser(), wxMpXmlMessage.getFromUser(), eventKey);
        String openId =  wxMpXmlMessage.getFromUser();
        // save or update
        WxMpUser wxMpUser = wxMpService.getUserService().userInfo(openId);
        sysWxMpUserService.saveOrUpdateByWxUserInfo(wxMpUser, wxMpService.getWxMpConfigStorage().getAppId());
        SysWxMpUser sysWxMpUser = sysWxMpUserService.selectByOpenId(openId);
        return easy4jWxMpHandler.handleScanQrCode(eventKey, wxMpService, wxMpXmlMessage, sysWxMpUser);
    }
}
