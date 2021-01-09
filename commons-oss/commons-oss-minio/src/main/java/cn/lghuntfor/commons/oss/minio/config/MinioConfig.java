package cn.lghuntfor.commons.oss.minio.config;

import cn.lghuntfor.commons.oss.api.service.OssService;
import cn.lghuntfor.commons.oss.api.config.OssProperties;
import cn.lghuntfor.commons.oss.minio.service.MinioOssService;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * minio配置
 * @author lghuntfor
 * @date 2020/10/12
 */
@Configuration
@EnableConfigurationProperties(OssProperties.class)
public class MinioConfig {

    @Autowired
    private OssProperties ossProperties;

    @Bean("minioClient")
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(ossProperties.getEndpoint())
                .credentials(ossProperties.getAccessKey(), ossProperties.getAccessSecret())
                .build();

    }

    @Bean("minioOssService")
    public OssService ossService() {
        return new MinioOssService();
    }

}
