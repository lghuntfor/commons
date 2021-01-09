package cn.lghuntfor.commons.webmvc.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * wet http 缓存拦截器配置类
 * 默认开启, 可通过配置关闭
 * @author liaogang
 * @date 2021/1/7 9:24
 */
@Configuration
@ConditionalOnProperty(value = "spring.mvc.cache.control.enable", havingValue = "true", matchIfMissing = true)
public class CacheControlConfig implements WebMvcConfigurer {

    /** 是否开启 */
    @Value("${spring.mvc.cache.control.enable:true}")
    private boolean enable;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CacheControlInterceptor(enable))
            .addPathPatterns("/**");
    }
}
