package top.sanjeev.card.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring 工具类 方便在非Spring 管理环境中获取bean
 */
@Component
public final class SpringUtils implements BeanFactoryPostProcessor, ApplicationContextAware {
    /**
     * Spring 应用上下文环境
     */
    private static ConfigurableListableBeanFactory configurableListableBeanFactory;

    private static ApplicationContext applicationContext;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        SpringUtils.configurableListableBeanFactory = configurableListableBeanFactory;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtils.applicationContext = applicationContext;
    }

    @SuppressWarnings("all")
    public static <T> T getBean(String name) throws BeansException {
        return (T)configurableListableBeanFactory.getBean(name);
    }

    @SuppressWarnings("all")
    public static <T> T getBean(Class<T> clz) throws BeansException {
        return (T)configurableListableBeanFactory.getBean(clz);
    }

    public static String getRequiredProperty(String key) {
        return applicationContext.getEnvironment().getRequiredProperty(key);
    }
}
