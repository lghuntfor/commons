package cn.lghuntfor.commons.cache.redis.listener;

import cn.hutool.core.util.StrUtil;
import cn.lghuntfor.commons.cache.redis.annotation.RedisListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 所有方法上带有@RedisListener的bean,
 * 都添加到messageListenerContainer中去
 * @author liaogang
 * @date 2020/9/7 16:17
 */
@Slf4j
public class RedisListenerBeanPostProcessor implements BeanPostProcessor {

    @Autowired
    private RedisMessageListenerContainer messageListenerContainer;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        /** 有可能会被代理, 需要特殊处理 */
        Method[] methods = ReflectionUtils.getDeclaredMethods(AopUtils.getTargetClass(bean));
        for (Method method : methods) {
            addMessageListener(bean, method);
        }
        return bean;
    }

    private void addMessageListener(Object bean, Method method) {
        RedisListener annotation = AnnotationUtils.getAnnotation(method, RedisListener.class);
        if (annotation != null && annotation.channels().length > 0) {
            List<Topic> topics = new ArrayList<>(annotation.channels().length);
            RedisListenerAdaptor listenerAdaptor = null;
            ListenerBeanWrapper beanWrapper = null;
            for (String c : annotation.channels()) {
                String channel = StrUtil.trim(c);
                if (StrUtil.isBlank(channel)) {
                    continue;
                }
                topics.add(getTopic(channel));
                if (beanWrapper == null) {
                    beanWrapper = RedisListenerContext.getBeanWrapper(bean, method);
                    if (beanWrapper == null) {
                        log.warn("beanWrapper is null, channel={}, method={}", annotation.channels(), method.getName());
                        continue;
                    }
                }
                if (listenerAdaptor == null) {
                    listenerAdaptor = new RedisListenerAdaptor(redisTemplate, beanWrapper);
                }
            }
            if (listenerAdaptor != null) {
                messageListenerContainer.addMessageListener(listenerAdaptor, topics);
            }
        }
    }

    /** 获取topic */
    private Topic getTopic(String channel) {
        if (channel.contains(RedisListenerContext.FUZZY_STR)) {
            return PatternTopic.of(channel);
        } else {
            return ChannelTopic.of(channel);
        }
    }
}
