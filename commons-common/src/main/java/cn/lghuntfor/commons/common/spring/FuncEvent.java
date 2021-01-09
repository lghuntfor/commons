package cn.lghuntfor.commons.common.spring;

import org.springframework.context.ApplicationEvent;

import java.util.function.Consumer;

/**
 * 函数调用的事件
 * @author liaogang
 * @date 2020/12/23 9:05
 */
public class FuncEvent extends ApplicationEvent {

    /** 事件需要调用的表达式 */
    private Consumer consumer;

    public FuncEvent(Object source, Consumer consumer) {
        super(source);
        this.consumer = consumer;
    }

    public Consumer getConsumer() {
        return consumer;
    }

}
