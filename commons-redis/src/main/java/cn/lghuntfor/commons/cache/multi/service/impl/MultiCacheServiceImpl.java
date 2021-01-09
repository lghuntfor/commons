package cn.lghuntfor.commons.cache.multi.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.lghuntfor.commons.cache.local.service.LocalCacheService;
import cn.lghuntfor.commons.cache.multi.common.FuncData;
import cn.lghuntfor.commons.cache.multi.common.MultiCacheEvent;
import cn.lghuntfor.commons.cache.multi.service.MultiCacheService;
import cn.lghuntfor.commons.cache.redis.common.CacheExpireEnum;
import cn.lghuntfor.commons.cache.redis.common.RedisConstant;
import cn.lghuntfor.commons.cache.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.TimeUnit;


/**
 * 多级缓存服务
 * @author lghuntfor
 * @date 2020/9/15
 */
public class MultiCacheServiceImpl implements MultiCacheService {

    /** 分布式锁key的后缀 */
    private final String LOCK_KEY_SUFFIX = ":MULTI-LOCK";

    @Autowired
    private LocalCacheService localCacheService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ApplicationContext context;


    /**
     * 只从本地获取拿取数据, 没有则返回空
     * @author liaogang
     * @date 2020/9/16
     * @param cacheKey
     * @return T
     */
    @Override
    public <T> T loadByLocalCache(String cacheKey) {
        return loadByLocalCache(cacheKey, CacheExpireEnum.HALF_HOUR.getTtl(), null);
    }

    /**
     * 从本地缓存中获取数据, 可用于非db类的操作
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
        return localCacheService.loadByLocalCache(cacheKey, expireSeconds, funcData);
    }

    /**
     * 只从Redis中获取拿取数据, 没有则返回空
     * @author liaogang
     * @date 2020/9/16
     * @param cacheKey
     * @return T
     */
    @Override
    public <T> T loadByRedisCache(String cacheKey) {
        return loadByRedisCache(cacheKey, CacheExpireEnum.HALF_HOUR.getTtl(), null);
    }

    /**
     * 从Redis缓存中获取数据
     * Redis缓存中没有, 再去指定业务方法中去拿
     * @author liaogang
     * @date 2020/9/15
     * @param cacheKey
     * @param expireSeconds 单位秒
     * @param funcData
     * @return T
     */
    @Override
    public <T> T loadByRedisCache(String cacheKey, long expireSeconds, FuncData<T> funcData) {
        return redisService.loadByRedisCache(cacheKey, expireSeconds, funcData);
    }

    /**
     * 只从多级缓存中拿取指定cacheKey的数据
     * @author liaogang
     * @date 2020/9/16
     * @param cacheKey
     * @return T
     */
    @Override
    public <T> T loadByMultiCache(String cacheKey) {
        return loadByMultiCache(cacheKey, CacheExpireEnum.HALF_HOUR.getTtl());
    }

    /**
     * 从多级缓存中拿取指定cacheKey的数据
     * 本地缓存 -> Redis缓存
     * 本地没有从Redis获取到后, 会加载到本地缓存中
     * @author liaogang
     * @date 2020/9/16
     * @param cacheKey
     * @param expireSeconds
     * @return T
     */
    @Override
    public <T> T loadByMultiCache(String cacheKey, long expireSeconds) {
        return loadByMultiCache(cacheKey, expireSeconds, null);
    }

    /**
     * 多级缓存中获取数据
     * 本地缓存中没有, 再去Redis缓存中去拿
     * Redis缓存中没有, 再去指定业务方法中去拿
     * @author liaogang
     * @date 2020/9/15
     * @param cacheKey
     * @param expireSeconds 单位秒
     * @param funcData
     * @return T
     */
    @Override
    public <T> T loadByMultiCache(String cacheKey, long expireSeconds, FuncData<T> funcData) {
        return loadByMultiCache(cacheKey, expireSeconds, funcData, false);
    }

    /**
     * 多级缓存中获取数据
     * 本地缓存中没有, 再去Redis缓存中去拿
     * Redis缓存中没有, 再去指定业务方法中去拿
     * @author liaogang
     * @date 2020/9/15
     * @param cacheKey
     * @param expireSeconds 单位秒
     * @param funcData
     * @return T
     */
    @Override
    public <T> T loadByMultiCache(String cacheKey, long expireSeconds, FuncData<T> funcData, boolean mismatch) {
        T result = loadFromCache(cacheKey, expireSeconds);
        if (result != null) {
            return result;
        }
        /** 缓存中都没有, 再去提供的业务方法中拿 */
        if (funcData == null) {
            return null;
        }
        return loadFromData(cacheKey, expireSeconds, funcData, mismatch);
    }

    /**
     * 从提供的data方法中获取数据
     */
    private <T> T loadFromData(String cacheKey, long expireSeconds, FuncData<T> funcData, boolean mismatch) {
        T result = null;
        String lockId = IdUtil.fastSimpleUUID();
        String lockKey = cacheKey + LOCK_KEY_SUFFIX;
        try {
            /** 缓存击穿: 添加分布式锁, 避免高并发时重复查询数据库 */
            boolean hasLock = redisService.tryLock(lockKey, lockId, 10, 2, TimeUnit.SECONDS);
            if (hasLock) {
                /** 缓存击穿: 双重空判断, 避免高并发时重复查询数据库 */
                result = loadFromCache(cacheKey, expireSeconds);
                if (result != null) {
                    return result;
                }
                // TODO: 2020/9/16 缓存穿透的处理, 布隆过滤器
                result = funcData.loadData();
                if (result != null || mismatch) {
                    localCacheService.put(cacheKey, result, expireSeconds);
                    redisService.setObject(cacheKey, result, expireSeconds);
                }
            }
        } finally {
            /** 释放锁资源 */
            redisService.unlock(lockKey, lockId);
        }
        return result;
    }

    /**
     * 从本地与redis中获取数据
     */
    private <T> T loadFromCache(String cacheKey, long expireSeconds) {
        /** 先从本地缓存拿 */
        T result = localCacheService.get(cacheKey);
        if (result != null) {
            return result;
        }
        /** 本地缓存没有, 再去redis中拿 */
        result = redisService.getObject(cacheKey);
        if (result != null) {
            localCacheService.put(cacheKey, result, expireSeconds);
            return result;
        }
        return null;
    }

    @Override
    public void clearLocalCache(String channel, String cacheKey) {
        localCacheService.clearLocalCache(channel, cacheKey);
    }

    /**
     * 清除指定cacheKey的Redis缓存
     * @author liaogang
     * @date 2020/9/15
     * @param cacheKey
     */
    @Override
    public void clearRedisCache(String cacheKey) {
        redisService.clearRedisCache(cacheKey);
    }

    /**
     * 清除指定cacheKey的所有应用内的本地缓存以及redis缓存
     * @author liaogang
     * @date 2020/9/16
     * @param cacheKey
     */
    @Override
    public void clearMultiCache(String channel, String cacheKey) {
        redisService.del(cacheKey);
        redisService.publishMessage(channel, cacheKey);
        /** 有事务时, 事务提交后再删除一次, 到达延时双删的目的 */
        context.publishEvent(new MultiCacheEvent(channel, cacheKey));
    }

}
