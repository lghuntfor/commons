package cn.lghuntfor.commons.webmvc.cors;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 服务http接口跨域访问的处理
 * 默认关闭, 需要配置才可开启
 * @author liaogang
 * @date 2020/10/20 15:28
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnProperty(value = "spring.mvc.cors.all.enable", havingValue = "true")
public class CorsWebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedMethods("*")
            .allowedOrigins("*")
            .allowedHeaders("*")
            .exposedHeaders(HttpHeaders.SET_COOKIE).maxAge(3600L)
            .allowCredentials(true);
    }
}
