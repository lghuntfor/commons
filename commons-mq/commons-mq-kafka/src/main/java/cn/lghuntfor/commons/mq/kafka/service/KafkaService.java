package cn.lghuntfor.commons.mq.kafka.service;

/**
 * kafka接口
 * @author liaogang
 * @date 2021/1/6 9:13
 */
public interface KafkaService {

    /**
     * 发送消息
     * @author liaogang
     * @date 2021/1/6 9:19
     * @param topic
     * @param message
     * @return void
     */
    void sendMsg(String topic, Object message);

    /**
     * 事务提交后, 才发送消息
     * @author liaogang
     * @date 2021/1/6 9:19
     * @param topic
     * @param message
     * @return void
     */
    void sendMsgAfterCommit(String topic, Object message);

}
