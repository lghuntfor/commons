package cn.lghuntfor.commons.cache.multi.listener;

import cn.lghuntfor.commons.cache.multi.common.LocalCacheEvent;
import cn.lghuntfor.commons.cache.multi.common.MultiCacheEvent;
import cn.lghuntfor.commons.cache.multi.common.RedisCacheEvent;
import cn.lghuntfor.commons.cache.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 缓存
 * @author liaogang
 * @date 2021/1/4 12:00
 */
public class CacheCleanEventListener {

    @Autowired
    private RedisService redisService;

    /**
     * local缓存清除, 事务提交成功后才执行
     * 没有事务时, 立即执行
     * @author liaogang
     * @date 2021/1/4 13:46
     * @param event
     * @return void
     */
    @TransactionalEventListener(value = LocalCacheEvent.class, phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void cleanLocalMessage(LocalCacheEvent event) {
        redisService.publishMessage(event.getChannel(), event.getCacheKey());
    }

    /**
     * redis缓存清除, 事务提交成功后才执行
     * 没有事务时, 立即执行
     * @author liaogang
     * @date 2021/1/4 13:47
     * @param event
     * @return void
     */
    @TransactionalEventListener(value = RedisCacheEvent.class, phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void cleanRedisMessage(RedisCacheEvent event) {
        redisService.del(event.getCacheKey());
    }

    /**
     * 多级缓存清除, 事务提交成功后才执行
     * 没有事务时, 立即执行
     * @author liaogang
     * @date 2021/1/4 13:46
     * @param event
     * @return void
     */
    @TransactionalEventListener(value = MultiCacheEvent.class, phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void cleanMultiMessage(MultiCacheEvent event) {
        redisService.del(event.getCacheKey());
        redisService.publishMessage(event.getChannel(), event.getCacheKey());
    }

}
