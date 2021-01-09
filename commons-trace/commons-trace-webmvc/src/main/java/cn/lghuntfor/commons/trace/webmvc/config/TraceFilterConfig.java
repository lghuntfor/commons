package cn.lghuntfor.commons.trace.webmvc.config;

import cn.lghuntfor.commons.trace.webmvc.filter.TraceWebFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Trace Filter的配置类
 * @author liaogang
 * @date 2020/9/10
 */
@Configuration
@ConditionalOnWebApplication
public class TraceFilterConfig {

    @Value("${spring.application.name}")
    private String springApplicationName;

    @Bean
    public FilterRegistrationBean traceWebFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new TraceWebFilter(springApplicationName));
        registration.addUrlPatterns("/*");
        registration.setName("traceWebFilter");
        registration.setOrder(-100);
        return registration;
    }

}
