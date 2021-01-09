package cn.lghuntfor.commons.cache.redis.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.lghuntfor.commons.cache.multi.common.RedisCacheEvent;
import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import cn.lghuntfor.commons.cache.multi.common.FuncData;
import cn.lghuntfor.commons.cache.redis.common.CacheExpireEnum;
import cn.lghuntfor.commons.cache.redis.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * Redis api操作接口实现类
 * @author liaogang
 * @date 2020/8/28 14:24
 */
public class RedisServiceImpl implements RedisService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /** 分布式锁key的后缀 */
    private final String REDIS_LOCK_KEY_SUFFIX = ":REDIS-LOCK";

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private GenericFastJsonRedisSerializer fastjsonSerializer;
    @Autowired
    private JdkSerializationRedisSerializer jdkSerializer;
    @Autowired
    private StringRedisSerializer stringSerializer;
    @Autowired
    private ApplicationContext context;

    @Override
    public void expire(String key, long expire) {
        redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    @Override
    public Set<String> keys(String regex) {
        return redisTemplate.keys(regex);
    }

    @Override
    public void del(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void del(Set<String> keys) {
        redisTemplate.delete(keys);
    }

    @Override
    public long increment(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    @Override
    public Boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public Long ttl(String key) {
        return redisTemplate.getExpire(key);
    }

    @Override
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void set(String key, String value, long expire) {
        redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
    }

    @Override
    public boolean setnx(String key, String value, long expire) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, expire, TimeUnit.SECONDS);
    }

    @Override
    public boolean setex(String key, String value, long expire) {
        return redisTemplate.opsForValue().setIfPresent(key, value, expire, TimeUnit.SECONDS);
    }

    @Override
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void setObject(String key, Object value) {
        redisTemplate.execute((RedisConnection connection) ->
                connection.set(stringSerializer.serialize(key), fastjsonSerializer.serialize(value)));
    }

    @Override
    public void setObject(String key, Object value, long expire) {
        redisTemplate.execute((RedisConnection connection) ->
                connection.set(stringSerializer.serialize(key), fastjsonSerializer.serialize(value), Expiration.seconds(expire), RedisStringCommands.SetOption.UPSERT));
    }

    @Override
    public <T> T getObject(String key) {
        T result = (T) redisTemplate.execute((RedisConnection connection) -> {
            byte[] bytes = connection.get(stringSerializer.serialize(key));
            return fastjsonSerializer.deserialize(bytes);
        });
        return result;
    }

    @Override
    public <T> T getObject(String key, Class<T> clazz) {
        T result = (T) redisTemplate.execute((RedisConnection connection) -> {
            byte[] bytes = connection.get(stringSerializer.serialize(key));
            return fastjsonSerializer.deserialize(bytes);
        });
        return result;
    }

    @Override
    public void setObjectByByte(String key, Object value, long expire) {
        redisTemplate.execute((RedisConnection connection) ->
            connection.set(stringSerializer.serialize(key), jdkSerializer.serialize(value), Expiration.seconds(expire), RedisStringCommands.SetOption.UPSERT));
    }

    @Override
    public <T> T getObjectByByte(String key) {
        return (T) redisTemplate.execute((RedisConnection connection) -> {
            byte[] bytes = connection.get(stringSerializer.serialize(key));
            return jdkSerializer.deserialize(bytes);
        });
    }

    @Override
    public void sadd(String key, String ... values) {
        redisTemplate.opsForSet().add(key, values);
    }

    @Override
    public void sremove(String key, String... value) {
        redisTemplate.opsForSet().remove(key, value);
    }

    @Override
    public Set<String> smembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    @Override
    public long slen(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    @Override
    public void zadd(String key, String value, double score) {
        redisTemplate.opsForZSet().add(key, value, score);
    }

    @Override
    public long zlen(String key) {
        return redisTemplate.opsForZSet().size(key);
    }

    @Override
    public Double incrementScore(String key, String value, long delta) {
        return redisTemplate.opsForZSet().incrementScore(key, value, delta);
    }

    @Override
    public void lpush(String key, String ... values) {
        redisTemplate.opsForList().leftPushAll(key, values);
    }

    @Override
    public String lpop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    @Override
    public String blpop(String key, long timeout) {
        return redisTemplate.opsForList().leftPop(key, timeout, TimeUnit.SECONDS);
    }

    @Override
    public void rpush(String key, String... values) {
        redisTemplate.opsForList().rightPushAll(key, values);
    }

    @Override
    public String rpop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    @Override
    public String brpop(String key, long timeout) {
        return redisTemplate.opsForList().rightPop(key, timeout, TimeUnit.SECONDS);
    }

    @Override
    public long llen(String key) {
        return redisTemplate.opsForList().size(key);
    }

    @Override
    public String lindex(String key, int index) {
        return redisTemplate.opsForList().index(key, index);
    }

    @Override
    public List<String> lrange(String key, int start, int end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    @Override
    public void lremove(String key, int index) {
        redisTemplate.opsForList().remove(key, 0, index);
    }

    @Override
    public void hset(String key, String field, String value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    @Override
    public void hsetAll(String key, Map<String, String> values) {
        redisTemplate.opsForHash().putAll(key, values);
    }

    @Override
    public String hget(String key, String field) {
        return (String) redisTemplate.opsForHash().get(key, field);
    }

    @Override
    public void hdel(String key, String... fields) {
        redisTemplate.opsForHash().delete(key, fields);
    }

    @Override
    public Boolean hexists(String key, String field) {
        return redisTemplate.opsForHash().hasKey(key, field);
    }

    @Override
    public long hlen(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    @Override
    public Set<String> hkeys(String key) {
        Set<Object> keys = redisTemplate.opsForHash().keys(key);
        if (CollUtil.isNotEmpty(keys)) {
            return keys.stream().map(Object::toString).collect(Collectors.toSet());
        }
        return null;
    }

    @Override
    public List<String> hvals(String key) {
        List<Object> values = redisTemplate.opsForHash().values(key);
        if (CollUtil.isNotEmpty(values)) {
            return values.stream().map(Object::toString).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public Boolean lock(String key, String value, long expire) {
        return this.setnx(key, value, expire);
    }

    @Override
    public Boolean tryLock(String key, String lockId, long expire, long timeout, TimeUnit timeUnit) {
        try {
            long startTime = System.currentTimeMillis();
            /** 超时的时间点 */
            long endTime = timeUnit.toMillis(timeout) + startTime;
            while (isTimeout(startTime, endTime)) {
                boolean success = this.setnx(key, lockId, expire);
                if (success) {
                    return true;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    private boolean isTimeout(long startTime, long endTime) {
        long currTime = System.currentTimeMillis();
        return (currTime < startTime || currTime >= endTime);
    }

    @Override
    public Boolean unlock(String key, String lockId) {
        DefaultRedisScript script = new DefaultRedisScript<Boolean>();
        script.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/unlock.lua")));
        script.setResultType(Boolean.class);
        Object result = redisTemplate.execute(script, Collections.singletonList(key), lockId);
        return Boolean.TRUE.equals(result);
    }

    @Override
    public void publishMessage(String channel, String message) {
        redisTemplate.convertAndSend(channel, message);
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
        return loadByRedisCache(cacheKey, expireSeconds, funcData, false);
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
    public <T> T loadByRedisCache(String cacheKey, long expireSeconds, FuncData<T> funcData, boolean mismatch) {
        /** 先从本地缓存拿 */
        T result = getObject(cacheKey);
        if (result != null) {
            return result;
        }
        /** 本地缓存没有, 再去提供的业务方法中拿 */
        if (funcData == null) {
            return null;
        }
        String lockId = IdUtil.fastSimpleUUID();
        String lockKey = cacheKey + REDIS_LOCK_KEY_SUFFIX;
        try {
            /** 缓存击穿: 添加分布式锁, 避免高并发时重复查询数据库 */
            boolean hasLock = tryLock(lockKey, lockId, 10, 2, TimeUnit.SECONDS);
            if (hasLock) {
                /** 缓存击穿: 双重空判断, 避免高并发时重复查询数据库 */
                result = getObject(cacheKey);
                if (result != null) {
                    return result;
                }
                // TODO: 2020/9/16 缓存穿透的处理, 布隆过滤器
                result = funcData.loadData();
                if (result != null || mismatch) {
                    setObject(cacheKey, result, expireSeconds);
                }
            }
        } finally {
            /** 释放锁资源 */
            unlock(lockKey, lockId);
        }
        return result;
    }

    @Override
    public void clearRedisCache(String cacheKey) {
        del(cacheKey);
        /** 有事务时, 事务提交后再删除一次, 到达延时双删的目的 */
        context.publishEvent(new RedisCacheEvent(cacheKey));
    }
}
