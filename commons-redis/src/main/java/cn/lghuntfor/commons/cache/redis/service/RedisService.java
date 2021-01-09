package cn.lghuntfor.commons.cache.redis.service;

import cn.lghuntfor.commons.cache.multi.common.FuncData;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis api操作接口
 * @author liaogang
 * @date 2020/8/28 14:23
 */
public interface RedisService {

    /** 基础操作 */

    /**
     * 指定缓存失效时间
     * @author liaogang
     * @date 2020/9/1
     * @param key
     * @param expire
     * @return void
     */
    void expire(String key, long expire);

    /**
     * 获取key列表(正则)
     * @author liaogang
     * @date 2020/9/1
     * @param regex
     * @return java.util.Set<java.lang.String>
     */
    Set<String> keys(String regex);

    /**
     * 删除缓存
     * @author liaogang
     * @date 2020/9/1
     * @param key
     * @return void
     */
    void del(String key);

    /**
     * 批量删除缓存
     * @author liaogang
     * @date 2020/9/1
     * @param keys
     * @return void
     */
    void del(Set<String> keys);

    /**
     * 自增
     * @author liaogang
     * @date 2020/9/1
     * @param key
     * @param delta
     * @return long
     */
    long increment(String key, long delta);

    /**
     * 判断key是否存在
     * @author liaogang
     * @date 2020/9/1
     * @param key
     * @return java.lang.Boolean
     */
    Boolean exists(String key);

    /**
     * 获取指定key的剩余过期时长(单位秒)
     * 返回-1表示key不过期
     * 返回-2表示key不存在
     * @author liaogang
     * @date 2020/9/1
     * @param key
     * @return java.lang.Long
     */
    Long ttl(String key);


    /** String 类型的操作 */

    /**
     * 设置缓存信息
     * @param key
     * @param value
     */
    void set(String key, String value);

    /**
     * 设置缓存信息, 同时设置过期时间
     * @param key
     * @param value
     * @param expire 过期时间, 单位秒(s)
     * @date 2020/8/28 14:23
     */
    void set(String key, String value, long expire);

    /**
     * 当key不存在时, 才设置缓存信息, 同时设置过期时间
     * @author liaogang
     * @date 2020/8/31
     * @param key
     * @param value
     * @param expire
     * @return boolean
     */
    boolean setnx(String key, String value, long expire);

    /**
     * 当key存在时, 才设置缓存信息, 同时设置过期时间
     * @author liaogang
     * @date 2020/8/31
     * @param key
     * @param value
     * @param expire
     * @return boolean
     */
    boolean setex(String key, String value, long expire);

    /**
     * 获取缓存信息
     * @author liaogang
     * @date 2020/9/1
     * @param key
     * @return java.lang.String
     */
    String get(String key);

    /**
     * 设置对象型缓存信息
     * @author liaogang
     * @date 2020/9/1
     * @param key
     * @param value
     * @return void
     */
    void setObject(String key, Object value);

    /**
     * 并设置缓存时间
     * @author liaogang
     * @date 2020/9/1
     * @param key
     * @param value
     * @param expire
     * @return void
     */
    void setObject(String key, Object value, long expire);

    /**
     * 获取缓存信息
     * @author liaogang
     * @date 2020/9/1
     * @param key
     * @param clazz
     * @return
     */
    <T> T getObject(String key);

    /**
     * 获取缓存信息
     * @author liaogang
     * @date 2020/9/1
     * @param key
     * @param clazz
     * @return
     */
    <T> T getObject(String key, Class<T> clazz);

    /**
     * 并设置缓存时间
     * @author liaogang
     * @date 2020/9/15
     * @param key
     * @param value
     * @param expire
     * @return void
     */
    void setObjectByByte(String key, Object value, long expire);


    /**
     * 获取缓存信息
     * @author liaogang
     * @date 2020/9/15
     * @param key
     * @return
     */
    <T> T getObjectByByte(String key);


    /** Set 类型的操作 */

    /**
     * 向set类型中添加元素
     * @author liaogang
     * @date 2020/8/31
     * @param key
     * @param values
     * @return void
     */
    void sadd(String key, String ... values);

    /**
     * 删除Set类型指定key中指定的值
     * @author liaogang
     * @date 2020/8/31
     * @param key
     * @param value
     * @return void
     */
    void sremove(String key, String ... value);

    /**
     * 获取Set类型指定key中的所有元素
     * @author liaogang
     * @date 2020/8/31
     * @param key
     * @return java.util.Set<java.lang.String>
     */
    Set<String> smembers(String key);

    /**
     * 获取Set类型指定key中元素的个数
     * @author liaogang
     * @date 2020/8/31
     * @param key
     * @return long
     */
    long slen(String key);

    /** ZSet 类型的操作 */

    /**
     * 向set类型中添加元素
     * @author liaogang
     * @date 2020/8/31
     * @param key
     * @param value
     * @param score
     * @return void
     */
    void zadd(String key, String value, double score);

    /**
     * 获取Set类型指定key中元素的个数
     * @author liaogang
     * @date 2020/8/31
     * @param key
     * @return long
     */
    long zlen(String key);

    /**
     * 对zset中指定元素的score进行自增操作
     * @author liaogang
     * @date 2020/8/31
     * @param key
     * @param value
     * @param delta 增量
     * @return java.lang.Double
     */
    Double incrementScore(String key, String value, long delta);

    /** list 类型的操作 */

    /**
     * list类型: 左进
     * @author liaogang
     * @date 2020/8/31
     * @param key
     * @param values
     * @return void
     */
    void lpush(String key, String ... values);

    /**
     * 移除左边第一个元素
     * @author liaogang
     * @date 2020/8/31
     * @param key
     * @return java.lang.String
     */
    String lpop(String key);

    /**
     * 移除左边第一个元素, 如果不存在, 则等待至有元素或超时
     * @author liaogang
     * @date 2020/8/31
     * @param key
     * @param timeout
     * @return java.lang.String
     */
    String blpop(String key, long timeout);

    /**
     * list类型: 右进
     * @author liaogang
     * @date 2020/8/31
     * @param key
     * @param values
     * @return void
     */
    void rpush(String key, String ... values);

    /**
     * 移除右边第一个元素
     * @author liaogang
     * @date 2020/8/31
     * @param key
     * @return java.lang.String
     */
    String rpop(String key);

    /**
     * 移除右边第一个元素, 如果不存在, 则等待至有元素或超时
     * @author liaogang
     * @date 2020/8/31
     * @param key
     * @param timeout
     * @return java.lang.String
     */
    String brpop(String key, long timeout);

    /**
     * 获取list的长度
     * @author liaogang
     * @date 2020/8/31
     * @param key
     * @return long
     */
    long llen(String key);

    /**
     * 获取list中指定索引的元素值
     * @author liaogang
     * @date 2020/8/31
     * @param key
     * @param index
     * @return java.lang.String
     */
    String lindex(String key, int index);

    /**
     * 获取list指定范围的值
     * @author liaogang
     * @date 2020/8/31
     * @param key
     * @param start
     * @param end
     * @return java.util.List<java.lang.String>
     */
    List<String> lrange(String key, int start, int end);

    /**
     * 移除list指定索引的值
     * @author liaogang
     * @date 2020/8/31
     * @param key
     * @param index
     * @return void
     */
    void lremove(String key, int index);

    /** hash 类型的操作 */

    /**
     * 设置hash value
     * @author liaogang
     * @date 2020/8/31
     * @param key
     * @param field
     * @param value
     * @return void
     */
    void hset(String key, String field, String value);

    /**
     * 批量设置hash数据
     * @author liaogang
     * @date 2020/8/31
     * @param key
     * @param values
     * @return void
     */
    void hsetAll(String key, Map<String, String> values);

    /**
     * 获取hash中的数据
     * @author liaogang
     * @date 2020/8/31
     * @param key
     * @param field
     * @return java.lang.String
     */
    String hget(String key, String field);

    /**
     * 批量删除hash中的数据
     * @author liaogang
     * @date 2020/8/31
     * @param key
     * @param fields
     * @return void
     */
    void hdel(String key, String ... fields);

    /**
     * 判断hash是否存在key field
     * @author liaogang
     * @date 2020/8/31
     * @param key
     * @param field
     * @return java.lang.Boolean
     */
    Boolean hexists(String key, String field);

    /**
     * 获取hash中的元素的个数
     * @author liaogang
     * @date 2020/8/31
     * @param key
     * @return long
     */
    long hlen(String key);

    /**
     * 获取hash中所有field
     * @author liaogang
     * @date 2020/8/31
     * @param key
     * @return java.util.Set<java.lang.String>
     */
    Set<String> hkeys(String key);

    /**
     * 获取hash中所有的value
     * @author liaogang
     * @date 2020/8/31
     * @param key
     * @return java.util.List<java.lang.String>
     */
    List<String> hvals(String key);

    /**
     * 分布式锁的redis实现
     * 立即返回结果
     * @author liaogang
     * @date 2020/8/31
     * @param key
     * @param value
     * @param expire
     * @return java.lang.Boolean
     */
    Boolean lock(String key, String value, long expire);

    /**
     * 分布式锁的redis实现, 未获取到锁时, 等待超时
     * @author liaogang
     * @date 2020/8/31
     * @param key
     * @param lockId
     * @param expire 过期时间
     * @param timeout 等待超时时间
     * @return java.lang.Boolean
     */
    Boolean tryLock(String key, String lockId, long expire, long timeout, TimeUnit timeUnit);

    /**
     * 释放分布式锁
     * @author liaogang
     * @date 2020/8/31
     * @param key
     * @param lockId
     * @return java.lang.Boolean
     */
    Boolean unlock(String key, String lockId);

    /**
     * redis的消息发布
     * 要订阅发布的消息, 只要在bean的方法上添加@RedisListener注解即可以实现
     * @author liaogang
     * @date 2020/9/7
     * @param channel
     * @param message
     * @return void
     */
    void publishMessage(String channel, String message);

    /**
     * 从Redis缓存中获取数据
     * Redis缓存中没有, 再去指定业务方法中去拿
     * @author liaogang
     * @date 2020/9/15
     * @param key
     * @param expireSeconds 单位秒
     * @param funcData
     * @return java.lang.String
     */
    <T> T loadByRedisCache(String key, long expireSeconds, FuncData<T> funcData);

    /**
     * 从Redis缓存中获取数据
     * Redis缓存中没有, 再去指定业务方法中去拿
     * @author liaogang
     * @date 2020/9/15
     * @param key
     * @param expireSeconds 单位秒
     * @param funcData
     * @return java.lang.String
     */
    <T> T loadByRedisCache(String key, long expireSeconds, FuncData<T> funcData, boolean mismatch);

    /**
     * 清除指定key的Redis缓存
     * @author liaogang
     * @date 2020/9/15
     * @param cacheKey
     */
    void clearRedisCache(String cacheKey);
}
