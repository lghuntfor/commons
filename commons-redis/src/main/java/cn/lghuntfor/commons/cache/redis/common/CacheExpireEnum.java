package cn.lghuntfor.commons.cache.redis.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * cache缓存时效定义
 * @author liaogang
 * @since 2018/10/18
 */
@AllArgsConstructor
@Getter
public enum CacheExpireEnum {

    SECOND("SECOND", 1)
    ,TEN_SECOND("TEN_SECOND", 10)
    ,THIRTY_SECOND("THIRTY_SECOND", 30)
    ,MINUTE("MINUTE", 60)
    ,QUARTER("QUARTER", 900)
    ,HALF_HOUR("HALF_HOUR", 1800)
    ,HOUR("HOUR", 3600)
    ,DAY("DAY", 86400)
    ,WEEK("WEEK", 604800)
    ,MONTH("MONTH", 2592000)
    ;

    private String cacheName;

    private long ttl;

    public static final CacheExpireEnum[] VALUES = CacheExpireEnum.values();

}
