package cn.lghuntfor.commons.sms.qiniu.config;

import cn.lghuntfor.commons.sms.api.config.SmsProperties;
import cn.lghuntfor.commons.sms.api.service.SmsService;
import cn.lghuntfor.commons.sms.qiniu.service.impl.QiniuSmsServiceImpl;
import com.qiniu.sms.SmsManager;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 短信配置
 * @author liaogang
 * @date 2020/9/16 11:54
 */
@Configuration
@EnableConfigurationProperties(value = SmsProperties.class)
public class SmsConfig {

    @Autowired
    private SmsProperties smsProperties;

    @Bean
    public SmsManager smsManager() {
        Auth auth = Auth.create(smsProperties.getAccessKey(), smsProperties.getAccessSecret());
        return new SmsManager(auth);
    }

    @Bean
    public SmsService smsService() {
        return new QiniuSmsServiceImpl();
    }
}
