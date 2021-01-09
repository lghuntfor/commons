package cn.lghuntfor.commons.oss.api.common;

import lombok.Data;

/**
 * Oss操作结果信息封装
 * @author lghuntfor
 * @date 2020/10/12
 */
@Data
public class OssResult {

    /** 存储路径 */
    private String path;

    /** 访问地址 */
    private String url;

    public OssResult(String path, String url) {
        this.path = path;
        this.url = url;
    }
}
