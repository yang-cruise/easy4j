package cn.easy4j.framework.db;

import cn.easy4j.common.constant.OrderedConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;

import java.util.Map;

/**
 * 初始化数据库，运行SQL脚本
 *
 * @author yangzongmin
 * @since 2019-07-19
 */
@Slf4j
public class DbInitializerListener implements ApplicationListener<ApplicationReadyEvent>, Ordered {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        Map<String, BaseDbInitializer> beansOfType = event.getApplicationContext().getBeansOfType(BaseDbInitializer.class);

        for (Map.Entry<String, BaseDbInitializer> entry : beansOfType.entrySet()) {
            BaseDbInitializer value = entry.getValue();
            value.dbInit();
        }
        log.info("初始化MySQL脚本完成");
    }

    @Override
    public int getOrder() {
        return OrderedConstant.INIT_DB_ORDER;
    }

}
