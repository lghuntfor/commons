package cn.lghuntfor.commons.cache.multi.common;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * redis cache的事件类
 * @author liaogang
 * @date 2021/1/4 11:57
 */
@Getter
public class RedisCacheEvent extends ApplicationEvent {

    private String cacheKey;

    public RedisCacheEvent(String cacheKey) {
        super(cacheKey);
        this.cacheKey = cacheKey;
    }
}
