package cn.lghuntfor.commons.webmvc.filter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * filter的配置类
 * 默认开启, 可以手动配置关闭
 * @author liaogang
 * @date 2020/9/10
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnProperty(value = "spring.webmvc.request.log.enable", havingValue = "true", matchIfMissing = true)
public class LogFilterConfig {

    @Bean
    public FilterRegistrationBean requestLogFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new RequestLogFilter());
        registration.addUrlPatterns("/*");
        registration.setName("requestLogFilter");
        registration.setOrder(1);
        return registration;
    }

}
