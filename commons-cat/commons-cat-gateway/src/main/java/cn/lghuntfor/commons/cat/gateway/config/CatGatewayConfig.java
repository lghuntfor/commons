package cn.lghuntfor.commons.cat.gateway.config;

import cn.lghuntfor.commons.cat.gateway.filter.CatGatewayFilter;
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
public class CatGatewayConfig {

    @Bean
    public CatGatewayFilter catGatewayFilter() {
        return new CatGatewayFilter();
    }

}
