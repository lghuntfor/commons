package cn.lghuntfor.commons.cat.webmvc.config;

import cn.lghuntfor.commons.cat.webmvc.filter.CatWebFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 拦截器配置
 */
@Configuration
@ConditionalOnWebApplication
public class CatWebConfig {

    @Bean
    public FilterRegistrationBean catWebFilterRegister() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new CatWebFilter());
        registration.addUrlPatterns("/*");
        registration.setName("catWebFilter");
        registration.setOrder(1);
        return registration;
    }

}
