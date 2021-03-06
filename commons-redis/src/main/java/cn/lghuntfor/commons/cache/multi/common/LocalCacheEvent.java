package cn.lghuntfor.commons.cache.multi.common;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * cache的事件类
 * @author liaogang
 * @date 2021/1/4 11:57
 */
@Getter
public class LocalCacheEvent extends ApplicationEvent {

    private String channel;

    private String cacheKey;

    public LocalCacheEvent(String channel, String cacheKey) {
        super(cacheKey);
        this.channel = channel;
        this.cacheKey = cacheKey;
    }
}
