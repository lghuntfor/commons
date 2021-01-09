package cn.lghuntfor.commons.idgen.leaf.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.lghuntfor.commons.idgen.api.IdGen;
import cn.lghuntfor.commons.idgen.common.Const;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static cn.lghuntfor.commons.idgen.common.Const.DEFAULT_KEY;

/**
 * idgen基于雪花算法的id获取实现类
 * @author liaogang
 * @date 2020/9/2 09:27
 */
@Slf4j
public class LeafIdGenImpl implements IdGen {

    /** leaf snowflake的固定接口地址 */
    private static final String SNOWFLAKE_SERVER_API = "/api/snowflake/get/";
    /** leaf segment的固定接口地址 */
    private static final String SEGMENT_SERVER_API = "/api/segment/get/";

    public static final Map<String, String> serverUrlMap = new ConcurrentHashMap<>();

    /**
     * 模式:
     * snowflake(默认)
     * segment
     */
    @Value("${leaf.server.mode:snowflake}")
    private String serverMode;

    /**
     * leaf snowflake的服务器器地址()
     * 例: http://localhost:8080
     */
    @Value("${leaf.server.address:}")
    private String serverAddress;
    /**
     * 获取id请求的超时时间
     * 默认3000ms
     */
    @Value("${leaf.server.timeout:3000}")
    private Integer serverTimeout;
    /**
     * 获取id请求异常时的重试次数
     * 默认10次
     */
    @Value("${leaf.server.retry:10}")
    private Integer serverRetry;


    @Override
    public Long nextId() {
        return nextId(DEFAULT_KEY);
    }

    @Override
    public Long nextId(String businessKey) {
        Assert.notBlank(businessKey, "businessKey can not be null ");
        String serverUrl = this.getServerUrl(businessKey);
        return getId(serverUrl);
    }

    /**
     * 从服务端获取id的处理
     * 异常重试
     * @param serverUrl
     * @return
     */
    private long getId(String serverUrl) {
        for (int i = 0; i < serverRetry; i++) {
            try {
                String idStr = HttpUtil.get(serverUrl, serverTimeout);
                return Long.parseLong(idStr);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        throw new RuntimeException("get id is error, please check leaf server");
    }

    /**
     * 获取服务地址, 添加缓存
     * @author liaogang
     * @date 2020/9/2
     * @param businessKey
     * @return java.lang.String
     */
    private String getServerUrl(String businessKey) {
        Assert.notBlank(serverAddress, "leaf.server.address can not be null ");
        if (Const.SEGMENT.equals(serverMode)) {
            return (serverAddress + SEGMENT_SERVER_API + businessKey);
        }
        /** 其他情况都默认为snowflake模式 */
        return (serverAddress + SNOWFLAKE_SERVER_API + businessKey);
    }

}
