package cn.lghuntfor.commons.oss.api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 配置信息
 * @author lghuntfor
 * @date 2020/10/12
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.oss")
public class OssProperties {

    /** 访问的key */
    private String accessKey;

    /** 访问的密钥 */
    private String accessSecret;

    /** 终端节点, 即服务访问地址 */
    private String endpoint;

    /** 桶名称 */
    private String bucketName;

    /** 对外提供访问的域名地址 */
    private String cdn;

    /** 上传图片时的水印地址(可选远程/文件) */
    private String watermark;
}
