package cn.lghuntfor.commons.plumelog.spring.config;

import cn.lghuntfor.commons.plumelog.spring.aspect.PlumelogTraceAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 扫描TraceAspect
 * @author lghuntfor
 * @date 2020/12/3
 */
@Configuration
public class PlumelogTraceConfig {

    @Bean
    public PlumelogTraceAspect plumelogTraceAspect() {
        return new PlumelogTraceAspect();
    }

}
