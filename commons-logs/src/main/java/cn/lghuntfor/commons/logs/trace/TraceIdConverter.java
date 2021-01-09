package cn.lghuntfor.commons.logs.trace;

import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import cn.lghuntfor.commons.trace.common.TraceContext;

/**
 * logback的日志消息转换器
 * 在这里添加全局的链路日志信息 traceId
 * @author liaogang
 * @date 2020/9/10 10:34
 */
public class TraceIdConverter extends MessageConverter {

    @Override
    public String convert(ILoggingEvent event) {
        return TraceContext.getTraceId();
    }
}
