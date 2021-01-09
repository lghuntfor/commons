package cn.lghuntfor.commons.plumelog.feign.config;

import cn.lghuntfor.commons.plumelog.feign.interceptor.PlumelogFeignInterceptor;
import feign.Feign;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * feign 拦截器配置
 * @author lghuntfor
 * @date 2020/11/27
 */
@Configuration
@ConditionalOnClass(value = {Feign.class})
public class PlumelogFeignConfig {

    @Bean
    public PlumelogFeignInterceptor plumelogFeignInterceptor() {
        return new PlumelogFeignInterceptor();
    }

}
