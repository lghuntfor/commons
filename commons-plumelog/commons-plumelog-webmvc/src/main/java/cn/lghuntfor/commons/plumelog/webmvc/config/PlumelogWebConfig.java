package cn.lghuntfor.commons.plumelog.webmvc.config;

import cn.lghuntfor.commons.plumelog.webmvc.filter.PlumelogWebFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 拦截器配置
 */
@Configuration
@ConditionalOnWebApplication
public class PlumelogWebConfig {

    @Bean
    public FilterRegistrationBean plumelogWebFilterRegister() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new PlumelogWebFilter());
        registration.addUrlPatterns("/*");
        registration.setName("plumelogWebFilter");
        registration.setOrder(1);
        return registration;
    }

}
