package cn.easy4j.framework.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * Spring Application 工具类
 *
 * @author yangzongmin
 * @since 2019-08-02
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationUtil {

    /** 全局的ApplicationContext */
    private static final ApplicationContext APPLICATION_CONTEXT = ApplicationContextRegister.getApplicationContext();

    public static ApplicationContext getApplicationContext() {
        return APPLICATION_CONTEXT;
    }

    public static <T> T getBean(String beanName, Class<T> requiredType) {
        if (containsBean(beanName)) {
            return APPLICATION_CONTEXT.getBean(beanName, requiredType);
        }
        return null;
    }

    public static <T> T getBean(Class<T> requiredType) {
        return APPLICATION_CONTEXT.getBean(requiredType);
    }

    public static <T> T getBean(String beanName) {
        if (containsBean(beanName)) {
            Class<T> type = getType(beanName);
            return APPLICATION_CONTEXT.getBean(beanName, type);
        }
        return null;
    }

    public static HttpServletRequest getRequest() {
        try {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            return Objects.requireNonNull(requestAttributes).getRequest();
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean containsBean(String name) {
        return APPLICATION_CONTEXT.containsBean(name);
    }

    public static boolean isSingleton(String name) {
        return APPLICATION_CONTEXT.isSingleton(name);
    }

    public static <T> Class<T> getType(String name) {
        return (Class<T>) APPLICATION_CONTEXT.getType(name);
    }

    public static String getActiveProfile() {
        String[] profiles = APPLICATION_CONTEXT.getEnvironment().getActiveProfiles();
        if (ArrayUtils.isNotEmpty(profiles)) {
            return profiles[0];
        }
        return "";
    }

}
