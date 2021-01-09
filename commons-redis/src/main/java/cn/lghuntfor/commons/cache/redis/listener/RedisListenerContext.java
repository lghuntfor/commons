package cn.lghuntfor.commons.cache.redis.listener;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;


/**
 * Redis 消息订阅的工具类
 * @author liaogang
 * @date 2020/9/7 15:33
 */
@Slf4j
public class RedisListenerContext {

    /** 模糊匹配的模糊字符串 */
    public static final String FUZZY_STR = "*";

    /**
     * 获取wrapper对象
     * @author liaogang
     * @date 2020/9/7
     * @param bean
     * @param listenMethod
     * @return com.ndccloud.commons.cache.redis.subscribe.SubscribeBeanWrapper
     */
    public static ListenerBeanWrapper getBeanWrapper(Object bean, Method listenMethod) {
        Parameter[] parameters = listenMethod.getParameters();
        int length = parameters.length;
        if (length == 1 || length == 2) {
            return new ListenerBeanWrapper(bean, listenMethod, length);
        }
        return null;
    }

    /**
     * method的执行
     * method的参数只能是1个或2个
     * 1个参数时, 传入的是message内容
     * 2个参数时, 传入的分别是channel与message
     */
    public static void invokeBeanWrapperMethod(ListenerBeanWrapper wrapper, String channel, String message) throws InvocationTargetException, IllegalAccessException {
        if (wrapper.getArgsLength() == 1) {
            wrapper.getListenMethod().invoke(wrapper.getListenBean(), message);
        } else {
            wrapper.getListenMethod().invoke(wrapper.getListenBean(), channel, message);
        }
    }

}
