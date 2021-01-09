package cn.lghuntfor.commons.cache.redis.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * redis message的适配
 * @author liaogang
 * @date 2020/9/7 10:22
 */
@Slf4j
public class RedisListenerAdaptor implements MessageListener {

    private StringRedisTemplate redisTemplate;

    /** 消息监听的bean包装器 */
    private ListenerBeanWrapper beanWrapper;

    public RedisListenerAdaptor(StringRedisTemplate redisTemplate, ListenerBeanWrapper beanWrapper) {
        this.redisTemplate = redisTemplate;
        this.beanWrapper = beanWrapper;
    }

    @Override
    public void onMessage(Message messageObj, byte[] pattern) {
        try {
            String channel = (String) redisTemplate.getKeySerializer().deserialize(messageObj.getChannel());
            String pn = new String(pattern);
            String message = (String) redisTemplate.getValueSerializer().deserialize(messageObj.getBody());

            RedisListenerContext.invokeBeanWrapperMethod(beanWrapper, channel, message);
            if (log.isDebugEnabled()) {
                log.debug("channel={}, pattern={}, message={}", channel, pn, message);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
