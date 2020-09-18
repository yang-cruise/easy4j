package cn.easy4j.wxmp.modular.task;

import cn.easy4j.framework.util.ApplicationUtil;
import cn.easy4j.wxmp.modular.dto.TemplateMsgVO;
import cn.easy4j.wxmp.modular.service.impl.WeixinTemplateMsgService;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.TimerTask;

/**
 * 微信公众号模板消息发送任务
 * @author yangzongmin
 * @since 2019-04-21
 */
@Slf4j
public class WxMpTemplateMsgTask extends TimerTask {

   private final TemplateMsgVO templateMsgVO;

    private static WeixinTemplateMsgService service = ApplicationUtil.getBean(WeixinTemplateMsgService.class);

    public WxMpTemplateMsgTask(TemplateMsgVO templateMsgVO) {
        this.templateMsgVO = templateMsgVO;
    }

    @Override
    public void run() {
        try {
           if (Objects.isNull(service)) {
                return;
            }
            service.send(templateMsgVO);
        } catch (Exception e) {
            log.error("异步发送模板消息失败", e);
        }
    }
}
