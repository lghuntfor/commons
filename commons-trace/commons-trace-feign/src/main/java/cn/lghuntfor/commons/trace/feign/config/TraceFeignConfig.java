package cn.lghuntfor.commons.trace.feign.config;

import cn.lghuntfor.commons.trace.feign.interceptor.TraceFeignInterceptor;
import feign.Feign;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * trace feign config
 * @author lghuntfor
 * @date 2020/11/29
 */
@Configuration
@ConditionalOnClass(value = {Feign.class})
public class TraceFeignConfig {

    @Bean
    public TraceFeignInterceptor traceFeignInterceptor() {
        return new TraceFeignInterceptor();
    }

}
