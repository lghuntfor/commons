package cn.lghuntfor.commons.mq.kafka.config;

import cn.lghuntfor.commons.mq.kafka.listener.KafkaEventListener;
import cn.lghuntfor.commons.mq.kafka.service.KafkaService;
import cn.lghuntfor.commons.mq.kafka.service.impl.KafkaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

/**
 * kafka相关配置
 * @author liaogang
 * @date 2021/1/6 9:01
 */
@Configuration
public class KafkaConfig {

    @Autowired
    private ConsumerFactory<Object, Object> consumerFactory;

    @Autowired
    private KafkaTemplate<Object, Object> kafkaTemplate;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Object, Object> containerFactory() {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        // 最大重试次数3次, 间隔2秒
        factory.setErrorHandler(new SeekToCurrentErrorHandler(new DeadLetterPublishingRecoverer((KafkaOperations) kafkaTemplate)
                , new FixedBackOff(2000, 3)));
        return factory;
    }

    @Bean
    public KafkaService kafkaService() {
        return new KafkaServiceImpl();
    }

    @Bean
    public KafkaEventListener kafkaEventListener() {
        return new KafkaEventListener();
    }

}
