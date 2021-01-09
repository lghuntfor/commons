package cn.lghuntfor.commons.cache.local.service.impl;

import cn.hutool.cache.impl.LRUCache;
import cn.lghuntfor.commons.cache.local.service.LocalCacheService;
import cn.lghuntfor.commons.cache.multi.common.FuncData;
import cn.lghuntfor.commons.cache.multi.common.LocalCacheEvent;
import cn.lghuntfor.commons.cache.redis.common.CacheExpireEnum;
import cn.lghuntfor.commons.cache.redis.common.RedisConstant;
import cn.lghuntfor.commons.cache.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;

/**
 * 本地缓存工具实现类
 * 基于hutool
 * @author liaogang
 * @date 2020/9/15 16:59
 */
public class LocalCacheServiceImpl implements LocalCacheService {

    /**
     * 本地缓存的默认过期时间, 以分钟为单位
     * 默认120分钟(2小时)
     */
    @Value("${local.cache.expire.minute:120}")
    private Integer localCacheExpireMinute;

    /**
     * 本地缓存对象的容量
     * 默认5w
     */
    @Value("${local.cache.capacity:50000}")
    private Integer localCacheCapacity;

    @Autowired
    private RedisService redisService;

    private LRUCache<String, Object> CACHE = null;

    @Autowired
    private ApplicationContext context;

    @PostConstruct
    public void init() {
        CACHE = new LRUCache<>(localCacheCapacity, localCacheExpireMinute * 60000);
    }

    /**
     * 添加本地缓存
     * @author liaogang
     * @date 2020/9/15
     * @param key
     * @param value
     * @param expireSeconds 单位秒
     */
    @Override
    public void put(String key, Object value, long expireSeconds) {
        CACHE.put(key, value, expireSeconds * 1000);
    }

    /**
     * 清除本地缓存
     * @author liaogang
     * @date 2020/9/15
     * @param key
     */
    @Override
    public void remove(String key) {
        CACHE.remove(key);
    }

    /**
     * 获取本地缓存
     * @author liaogang
     * @date 2020/9/15
     * @param key
     */
    @Override
    public <T> T get(String key) {
        return (T) CACHE.get(key);
    }

    /**
     * 从本地缓存中获取数据
     * 本地缓存中没有, 再去指定业务方法中去拿
     * @author liaogang
     * @date 2020/9/15
     * @param cacheKey
     * @param expireSeconds 单位秒
     * @param funcData
     * @return T
     */
    @Override
    public <T> T loadByLocalCache(String cacheKey, long expireSeconds, FuncData<T> funcData) {
        return loadByLocalCache(cacheKey, expireSeconds, funcData, false);
    }

    /**
     * 从本地缓存中获取数据
     * 本地缓存中没有, 再去指定业务方法中去拿
     * @author liaogang
     * @date 2020/9/15
     * @param cacheKey
     * @param expireSeconds 单位秒
     * @param funcData
     * @return T
     */
    @Override
    public <T> T loadByLocalCache(String cacheKey, long expireSeconds, FuncData<T> funcData, boolean mismatch) {
        /** 先从本地缓存拿 */
        T result = get(cacheKey);
        if (result != null) {
            return result;
        }
        /** 本地缓存没有, 再去提供的业务方法中拿 */
        if (funcData == null) {
            return null;
        }
        synchronized (cacheKey) {
            /** 缓存击穿: 双重空判断, 避免高并发时重复查询数据库 */
            result = get(cacheKey);
            if (result != null) {
                return result;
            }
            result = funcData.loadData();
            if (result != null || mismatch) {
                put(cacheKey, result, expireSeconds);
            }
        }
        return result;
    }


    /**
     * 清除指定cacheKey的所有应用内的本地缓存
     * @author liaogang
     * @date 2020/9/16
     * @param channel
     * @param cacheKey
     */
    @Override
    public void clearLocalCache(String channel, String cacheKey) {
        redisService.publishMessage(channel, cacheKey);
        /** 有事务时, 事务提交后再删除一次, 到达延时双删的目的 */
        context.publishEvent(new LocalCacheEvent(channel, cacheKey));
    }
}
