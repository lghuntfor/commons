package cn.lghuntfor.commons.plumelog.gateway.config;

import cn.lghuntfor.commons.plumelog.gateway.filter.PlumelogGatewayFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.DispatcherHandler;

/**
 * gateway网关过滤器配置
 * @author lghuntfor
 * @date 2020/11/28
 */
@Configuration
@ConditionalOnClass(value = {DispatcherHandler.class})
public class PlumelogGatewayConfig {

    @Bean
    public PlumelogGatewayFilter catGatewayFilter() {
        return new PlumelogGatewayFilter();
    }

}
