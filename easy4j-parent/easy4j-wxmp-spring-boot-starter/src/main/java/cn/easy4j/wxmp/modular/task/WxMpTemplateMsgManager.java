package cn.easy4j.wxmp.modular.task;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 微信公众号模板消息发送管理器
 * @author yangzongmin
 * @date 2019-04-21
 */
public class WxMpTemplateMsgManager {

    /** 发送模板消息操作延时 */
    private static final int OPERATE_DELAY_TIME = 10;

    /** 异步操作发送模板消息的线程池 */
    private final ScheduledThreadPoolExecutor executor;

    private WxMpTemplateMsgManager() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("WxMpTemplateMsg-pool-%d").build();
        executor = new ScheduledThreadPoolExecutor(10, threadFactory);
    }

    private static final WxMpTemplateMsgManager WX_MP_TEMPLATE_MSG_MANAGER = new WxMpTemplateMsgManager();

    public static WxMpTemplateMsgManager me() {
        return WX_MP_TEMPLATE_MSG_MANAGER;
    }

    public void send(TimerTask task) {
        executor.schedule(task, OPERATE_DELAY_TIME, TimeUnit.MILLISECONDS);
    }

}
