package cn.lghuntfor.commons.mq.kafka.listener;

import cn.lghuntfor.commons.mq.kafka.common.KafkaMsgEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * kafka消息事件监听器
 * @author liaogang
 * @date 2021/1/6 9:24
 */
public class KafkaEventListener {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @TransactionalEventListener(value = KafkaMsgEvent.class, phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void message(KafkaMsgEvent event) {
        kafkaTemplate.send(event.getTopic(), event.getKey(), event.getSource());
    }

}
