package cn.lghuntfor.commons.cache.redis.annotation;

import java.lang.annotation.*;

/**
 * redis订阅消息的标识类
 * @author liaogang
 * @date 2020/9/7 15:27
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisListener {

    /**
     * 需要监听订阅的channel名称, 支持模糊匹配(*)
     * @author liaogang
     * @date 2020/9/7
     * @return java.lang.String
     */
    String[] channels() default {};

}
