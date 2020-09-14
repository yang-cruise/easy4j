package cn.easy4j.wxmp.config;

import cn.easy4j.wxmp.core.handler.*;
import cn.easy4j.wxmp.modular.service.Easy4jWxMpHandler;
import cn.easy4j.wxmp.modular.service.impl.Easy4jWxMpHandlerDefaultImpl;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static me.chanjar.weixin.common.api.WxConsts.EventType.SUBSCRIBE;
import static me.chanjar.weixin.common.api.WxConsts.EventType.UNSUBSCRIBE;
import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType.EVENT;
import static me.chanjar.weixin.mp.constant.WxMpEventConstants.CustomerService.*;
import static me.chanjar.weixin.mp.constant.WxMpEventConstants.POI_CHECK_NOTIFY;

/**
 * @author ChenYichen
 */
@Slf4j
@Configuration
@ComponentScan("cn.easy4j.wxmp")
public class WxMpAutoConfiguration {
    @Resource
    private LogHandler logHandler;
    @Resource
    private NullHandler nullHandler;
    @Resource
    private KfSessionHandler kfSessionHandler;
    @Resource
    private StoreCheckNotifyHandler storeCheckNotifyHandler;
    @Resource
    private LocationHandler locationHandler;
    @Resource
    private MenuHandler menuHandler;
    @Resource
    private MsgHandler msgHandler;
    @Resource
    private UnsubscribeHandler unsubscribeHandler;
    @Resource
    private SubscribeHandler subscribeHandler;
    @Resource
    private ScanHandler scanHandler;

    private static final Map<String, WxMpMessageRouter> CACHE = new HashMap<>(16);

    @Bean
    @ConditionalOnMissingBean
    public Easy4jWxMpHandler easy4jWxMpHandler() {
        return new Easy4jWxMpHandlerDefaultImpl();
    }

    public WxMpMessageRouter getMessageRouter(String appId, WxMpService wxMpService) {

        WxMpMessageRouter wxMpMessageRouter = CACHE.get(appId);
        if (Objects.nonNull(wxMpMessageRouter)) {
            return wxMpMessageRouter;
        }

        final WxMpMessageRouter newRouter = new WxMpMessageRouter(wxMpService);

        // 记录所有事件的日志 （异步执行）
        newRouter.rule().handler(this.logHandler).next();

        // 接收客服会话管理事件
        newRouter.rule().async(false).msgType(EVENT).event(KF_CREATE_SESSION)
                .handler(this.kfSessionHandler).end();
        newRouter.rule().async(false).msgType(EVENT).event(KF_CLOSE_SESSION)
                .handler(this.kfSessionHandler).end();
        newRouter.rule().async(false).msgType(EVENT).event(KF_SWITCH_SESSION)
                .handler(this.kfSessionHandler).end();

        // 门店审核事件
        newRouter.rule().async(false).msgType(EVENT).event(POI_CHECK_NOTIFY).handler(this.storeCheckNotifyHandler).end();

        // 自定义菜单事件
        newRouter.rule().async(false).msgType(EVENT).event(WxConsts.EventType.CLICK).handler(this.menuHandler).end();

        // 点击菜单连接事件
        newRouter.rule().async(false).msgType(EVENT).event(WxConsts.EventType.VIEW).handler(this.nullHandler).end();

        // 关注事件
        newRouter.rule().async(false).msgType(EVENT).event(SUBSCRIBE).handler(this.subscribeHandler).end();

        // 取消关注事件
        newRouter.rule().async(false).msgType(EVENT).event(UNSUBSCRIBE).handler(this.unsubscribeHandler).end();

        // 上报地理位置事件
        newRouter.rule().async(false).msgType(EVENT).event(WxConsts.EventType.LOCATION).handler(this.locationHandler).end();

        // 接收地理位置消息
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.LOCATION).handler(this.locationHandler).end();

        // 扫码事件
        newRouter.rule().async(false).msgType(EVENT).event(WxConsts.EventType.SCAN).handler(this.scanHandler).end();

        // 默认
        newRouter.rule().async(false).handler(this.msgHandler).end();

        CACHE.put(appId, newRouter);

        return newRouter;
    }

}
