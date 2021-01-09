package cn.lghuntfor.commons.cache.redis.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;

/**
 * cache异常处理:
 * 1.get异常时只打印信息, 避免因cache对业务操作影响
 * 2.put/清除cache时将异常抛出, 避免因cache导致业务逻辑出错
 * @author liaogang
 * @date 2020/5/18
 */
@Slf4j
public class CustomCacheErrorHandler implements CacheErrorHandler {

    @Override
    public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
        log.error("===handleCacheGetError, key="+key.toString(), exception);
    }

    @Override
    public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
        log.error("===handleCachePutError, key="+key.toString(), exception);
        throw exception;
    }

    @Override
    public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
        log.error("===handleCacheEvictError, key="+key.toString(), exception);
        throw exception;
    }

    @Override
    public void handleCacheClearError(RuntimeException exception, Cache cache) {
        log.error("===handleCacheClearError", exception);
        throw exception;
    }
}
