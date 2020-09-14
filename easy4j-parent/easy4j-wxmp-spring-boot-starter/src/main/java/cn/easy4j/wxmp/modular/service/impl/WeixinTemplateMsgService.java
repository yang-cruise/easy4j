package cn.easy4j.wxmp.modular.service.impl;

import cn.easy4j.wxmp.modular.dto.TemplateMsgVO;
import cn.easy4j.wxmp.modular.entity.SysWxMpUser;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author ChenYichen
 * @date 2020/4/19
 */
@Slf4j
@Service
public class WeixinTemplateMsgService {

    @Resource
    private WeixinMpService weixinMpService;

    @Resource
    private SysWxMpUserService sysWxMpUserService;

    @Resource
    private SysWxMpService sysWxMpService;

    public void send(TemplateMsgVO templateMsgVO) {

        Assert.notNull(templateMsgVO, "参数错误");
        Assert.notNull(templateMsgVO.getWxMpUserId(), "接收用户ID不能为空");
        Assert.notNull(templateMsgVO.getTemplateId(), "模板类型不能为空");
        Assert.notNull(templateMsgVO.getData(), "模板参数不能为空");

        SysWxMpUser wxMpUser = sysWxMpUserService.getById(templateMsgVO.getWxMpUserId());
        Assert.notNull(wxMpUser, "参数错误");
        String appId = sysWxMpService.getById(wxMpUser.getWxMpId()).getAppId();

        WxMpService wxMpService = weixinMpService.getWxMpService(appId);


        WxMpTemplateMessage message = createWxMpTemplateMessage(wxMpUser, templateMsgVO);
        if (Objects.isNull(message)) {
            return;
        }

        try {
            wxMpService.getTemplateMsgService().sendTemplateMsg(message);
            log.info("发送模板消息成功，templateMsgVO={}", message.toJson());
        } catch (WxErrorException e) {
            log.error("发送模板消息异常，templateMsgVO={}，errorMsg={}", message.toJson(), e.getError().getErrorMsg());
        }
    }

    private WxMpTemplateMessage createWxMpTemplateMessage(SysWxMpUser sysWxMpUser, TemplateMsgVO templateMsgVO) {
        return WxMpTemplateMessage.builder().templateId(templateMsgVO.getTemplateId()).toUser(sysWxMpUser.getOpenId())
                .data(templateMsgVO.getData()).build();
    }

}
