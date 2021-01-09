package cn.lghuntfor.commons.trace.gateway.config;

import cn.lghuntfor.commons.trace.gateway.filter.TraceGatewayFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.DispatcherHandler;

/**
 * trace gateway filter config
 * @author lghuntfor
 * @date 2020/11/29
 */
@Configuration
@ConditionalOnClass(value = {DispatcherHandler.class})
public class TraceGatewayConfig {

    @Bean
    public TraceGatewayFilter traceGatewayFilter() {
        return new TraceGatewayFilter();
    }

}
