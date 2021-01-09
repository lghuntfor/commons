package cn.lghuntfor.commons.cache.redis.common;

/**
 * 常量
 * @author liaogang
 * @date 2020/5/14
 */
public interface RedisConstant {

    Long UNLOCK_OK = 1L;
    String UNLOCK_SCRIPT = "if redis.call('GET', KEYS[1]) == ARGV[1] then redis.call('DEL', KEYS[1]) return true else return false end";


    String REDIS_KEY_CLEAR_LOCAL_CACHE = "common:clear:local:cache";
}
