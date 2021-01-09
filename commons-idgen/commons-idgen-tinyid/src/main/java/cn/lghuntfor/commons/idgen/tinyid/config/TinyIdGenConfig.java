package cn.lghuntfor.commons.idgen.tinyid.config;

import cn.lghuntfor.commons.idgen.tinyid.service.impl.TinyIdGenImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 *
 * @author lghuntfor
 * @date 2020/11/28
 */
@Configuration
public class TinyIdGenConfig {

    @Bean
    public TinyIdGenImpl segmentTinyIdGen() {
        return new TinyIdGenImpl();
    }

}
