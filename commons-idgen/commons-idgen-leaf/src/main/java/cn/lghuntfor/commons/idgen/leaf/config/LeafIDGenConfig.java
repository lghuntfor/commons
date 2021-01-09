package cn.lghuntfor.commons.idgen.leaf.config;

import cn.lghuntfor.commons.idgen.api.IdGen;
import cn.lghuntfor.commons.idgen.leaf.service.impl.LeafIdGenImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * idgen自动配置类
 * @author liaogang
 * @date 2020/9/2 10:05
 */
@Configuration
public class LeafIDGenConfig {

    @Bean
    public IdGen idGen() {
        return new LeafIdGenImpl();
    }

}
