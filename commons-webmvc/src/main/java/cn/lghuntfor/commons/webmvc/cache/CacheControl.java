package cn.lghuntfor.commons.webmvc.cache;

import cn.lghuntfor.commons.common.constants.TTL;

import java.lang.annotation.*;

/**
 * web http 的缓存设置注解
 * 添加此注释的mvc方法, 会自动设置浏览器缓存
 * @author liaogang
 * @date 2021/1/7 9:01
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD })
@Documented
public @interface CacheControl {

    /**
     * 最小缓存时间, 默认半小时
     */
    long minAge() default TTL.M30;

    /**
     * 最大缓存时间, 默认6小时
     */
    long maxAge() default TTL.H6;

}
