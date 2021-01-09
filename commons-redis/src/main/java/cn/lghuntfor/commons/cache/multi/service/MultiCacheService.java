package cn.lghuntfor.commons.cache.multi.service;


import cn.lghuntfor.commons.cache.multi.common.FuncData;

/**
 * 多级缓存工具接口
 * @author liaogang
 * @date 2020/9/16 11:02
 */
public interface MultiCacheService {

    /**
     * 只从本地获取拿取数据, 没有则返回空
     * @author liaogang
     * @date 2020/9/16
     * @param key
     * @return T
     */
    <T> T loadByLocalCache(String key);

    /**
     * 从本地缓存中获取数据
     * 本地缓存中没有, 再去指定业务方法中去拿
     * @author liaogang
     * @date 2020/9/15
     * @param key
     * @param expireSeconds 单位秒
     * @param funcData
     * @return java.lang.String
     */
    <T> T loadByLocalCache(String key, long expireSeconds, FuncData<T> funcData);

    /**
     * 只从Redis中获取拿取数据, 没有则返回空
     * @author liaogang
     * @date 2020/9/16
     * @param key
     * @return T
     */
    <T> T loadByRedisCache(String key);

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
     * 只从多级缓存中拿取指定key的数据
     * @author liaogang
     * @date 2020/9/16
     * @param key
     * @return T
     */
    <T> T loadByMultiCache(String key);

    /**
     * 从多级缓存中拿取指定key的数据
     * 本地缓存 -> Redis缓存
     * 本地没有从Redis获取到后, 会加载到本地缓存中
     * @author liaogang
     * @date 2020/9/16
     * @param key
     * @param expireSeconds
     * @return T
     */
    <T> T loadByMultiCache(String key, long expireSeconds);

    /**
     * 多级缓存中获取数据
     * 本地缓存中没有, 再去Redis缓存中去拿
     * Redis缓存中没有, 再去指定业务方法中去拿
     * @author liaogang
     * @date 2020/9/15
     * @param key
     * @param expireSeconds 单位秒
     * @param funcData
     * @return java.lang.String
     */
    <T> T loadByMultiCache(String key, long expireSeconds, FuncData<T> funcData);

    /**
     * 多级缓存中获取数据
     * 本地缓存中没有, 再去Redis缓存中去拿
     * Redis缓存中没有, 再去指定业务方法中去拿
     * @author liaogang
     * @date 2020/9/15
     * @param key
     * @param expireSeconds 单位秒
     * @param funcData
     * @param mismatch 返回为null时, 是否存储
     * @return java.lang.String
     */
    <T> T loadByMultiCache(String key, long expireSeconds, FuncData<T> funcData ,boolean mismatch);

    /**
     * 清除指定key的所有应用内的本地缓存
     * @author liaogang
     * @date 2020/9/16
     * @param cacheKey
     */
    void clearLocalCache(String channel, String cacheKey);

    /**
     * 清除指定key的Redis缓存
     * @author liaogang
     * @date 2020/9/15
     * @param cacheKey
     */
    void clearRedisCache(String cacheKey);

    /**
     * 清除指定key的所有应用内的本地缓存以及redis缓存
     * @author liaogang
     * @date 2020/9/16
     * @param cacheKey
     */
    void clearMultiCache(String channel, String cacheKey);

}
