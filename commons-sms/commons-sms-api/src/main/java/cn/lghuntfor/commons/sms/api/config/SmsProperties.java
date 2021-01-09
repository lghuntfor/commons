package cn.lghuntfor.commons.sms.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 短信配置类
 * @author liaogang
 * @date 2020/11/2 09:32
 */
@Data
@ConfigurationProperties(prefix = "spring.sms")
public class SmsProperties {

    private String url;

    private String accessKey;

    private String accessSecret;

}
