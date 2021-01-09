package cn.lghuntfor.commons.common.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;


/**
 * spring操作工具类
 * 1. 提供静态方法获取bean
 * 2. 提供静态方法获取properties内容
 * @author liaogang
 * @date 2020/9/10 17:50
 */
@Configuration
public class SpringUtils implements ApplicationContextAware, EnvironmentAware {

    private static ApplicationContext context = null;

    private static Environment env = null;

    public static <T> T getBean(String beanName, Class<T> clazz) {
        Assert.notNull(context, "ApplicationContext is null");
        return context.getBean(beanName, clazz);
    }

    public static <T> T getBeanByClass(Class<T> clazz) {
        Assert.notNull(context, "ApplicationContext is null");
        return context.getBean(clazz);
    }

    public static <T> T getBeanByName(String beanName) {
        Assert.notNull(context, "ApplicationContext is null");
        return (T) context.getBean(beanName);
    }

    public static String getPropertyValue(String key) {
        Assert.notNull(env, "Environment is null");
        return env.getProperty(key);
    }


    public static ApplicationContext getApplicationContext() {
        return context;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    @Override
    public void setEnvironment(Environment environment) {
        env = environment;
    }
}
