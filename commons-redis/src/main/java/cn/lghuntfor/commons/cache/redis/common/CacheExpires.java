package cn.lghuntfor.commons.cache.redis.common;

/**
 * cache缓存时效定义
 * @author liaogang
 * @since 2018/10/18
 */
public class CacheExpires {
    /**
     * 1秒
     */
    public static final String SECOND = "SECOND";
    /**
     * 10秒
     */
    public static final String TEN_SECOND = "TEN_SECOND";
    /**
     * 30秒
     */
    public static final String THIRTY_SECOND = "THIRTY_SECOND";
    /**
     * 1分钟=60s
     */
    public static final String MINUTE = "MINUTE";
    /**
     * 15分钟=900s
     */
    public static final String QUARTER = "QUARTER";
    /**
     * 30分钟=1800s
     */
    public static final String HALF_HOUR = "HALF_HOUR";
    /**
     * 1小时=3600s
     */
    public static final String HOUR = "HOUR";
    /**
     * 1天=86400s
     */
    public static final String DAY = "DAY";
    /**
     * 1周=604800s
     */
    public static final String WEEK = "WEEK";
    /**
     * 1个月=2592000s
     */
    public static final String MONTH = "MONTH";
}
