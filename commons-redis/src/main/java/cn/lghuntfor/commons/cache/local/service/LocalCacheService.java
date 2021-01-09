package cn.lghuntfor.commons.cache.local.service;

import cn.lghuntfor.commons.cache.multi.common.FuncData;

/**
 * 本地缓存工具接口
 * @author liaogang
 * @date 2020/9/16 11:00
 */
public interface LocalCacheService {

    /**
     * 添加本地缓存
     * @author liaogang
     * @date 2020/9/15
     * @param key
     * @param value
     * @param expireSeconds 单位秒
     */
    void put(String key, Object value, long expireSeconds);

    /**
     * 清除本地缓存
     * @author liaogang
     * @date 2020/9/15
     * @param key
     */
    void remove(String key);

    /**
     * 获取本地缓存
     * @author liaogang
     * @date 2020/9/15
     * @param key
     */
    <T> T get(String key);

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
     * 从本地缓存中获取数据
     * 本地缓存中没有, 再去指定业务方法中去拿
     * @author liaogang
     * @date 2020/9/15
     * @param key
     * @param expireSeconds 单位秒
     * @param funcData
     * @return java.lang.String
     */
    <T> T loadByLocalCache(String key, long expireSeconds, FuncData<T> funcData, boolean mismatch);

    /**
     * 清除指定key的所有应用内的本地缓存
     * @author liaogang
     * @date 2020/9/16
     * @param cacheKey
     */
    void clearLocalCache(String channel, String cacheKey);
}
