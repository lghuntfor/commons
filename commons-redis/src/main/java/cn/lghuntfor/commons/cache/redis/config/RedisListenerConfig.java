package cn.lghuntfor.commons.cache.redis.config;

import cn.hutool.core.thread.ThreadUtil;
import cn.lghuntfor.commons.cache.local.service.LocalCacheService;
import cn.lghuntfor.commons.cache.multi.listener.CacheCleanEventListener;
import cn.lghuntfor.commons.cache.multi.service.MultiCacheService;
import cn.lghuntfor.commons.cache.redis.listener.RedisListenerBeanPostProcessor;
import cn.lghuntfor.commons.cache.multi.service.impl.MultiCacheServiceImpl;
import cn.lghuntfor.commons.cache.local.service.impl.LocalCacheServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * redis message 监听器
 * @author liaogang
 * @date 2020/9/7 16:12
 */
@Configuration
@Slf4j
public class RedisListenerConfig {

    @Autowired
    private RedisConnectionFactory factory;

    @Bean
    public RedisListenerBeanPostProcessor redisMessageBeanPostProcessor() {
        return new RedisListenerBeanPostProcessor();
    }

    /**
     * 清除缓存的监听器
     * @return
     */
    @Bean
    public CacheCleanEventListener cacheCleanListener() {
        return new CacheCleanEventListener();
    }

    /**
     * 多级缓存服务
     */
    @Bean
    public MultiCacheService multiCacheService() {
        return new MultiCacheServiceImpl();
    }

    /**
     * 本地缓存服务
     */
    @Bean
    public LocalCacheService localCacheService() {
        return new LocalCacheServiceImpl();
    }

    /**
     * message listener容器
     */
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer() {
        log.info("===== redisMessageListenerContainer =====");
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(factory);
        redisMessageListenerContainer.setTaskExecutor(ThreadUtil.newExecutor(Runtime.getRuntime().availableProcessors() * 2));
        return redisMessageListenerContainer;
    }

}
