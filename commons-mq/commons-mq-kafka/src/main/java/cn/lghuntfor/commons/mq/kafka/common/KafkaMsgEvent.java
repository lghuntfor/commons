package cn.lghuntfor.commons.mq.kafka.common;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 添加描述信息
 *
 * @author liaogang
 * @date 2021/1/6 9:23
 */
@Getter
public class KafkaMsgEvent extends ApplicationEvent {

    private String topic;

    private String key;

    public KafkaMsgEvent(String topic, String key, Object message) {
        super(message);
        this.key = key;
        this.topic = topic;
    }
}
