package cn.lghuntfor.commons.mq.kafka.service.impl;

import cn.lghuntfor.commons.mq.kafka.common.KafkaMsgEvent;
import cn.lghuntfor.commons.mq.kafka.service.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;

/**
 * kafka实现类
 * @author liaogang
 * @date 2021/1/6 9:17
 */
public class KafkaServiceImpl implements KafkaService {

    @Autowired
    private KafkaTemplate<Object, Object> kafkaTemplate;
    @Autowired
    private ApplicationContext context;


    @Override
    public void sendMsg(String topic, Object message) {
        kafkaTemplate.send(topic, UUID.randomUUID().toString(), message);
    }

    @Override
    public void sendMsgAfterCommit(String topic, Object message) {
        context.publishEvent(new KafkaMsgEvent(topic, UUID.randomUUID().toString(), message));
    }
}
