package cn.lghuntfor.commons.plumelog.common;

import com.plumelog.core.TraceId;

import java.util.UUID;

/**
 * plume 工具类
 * @author lghuntfor
 * @date 2020/12/6
 */
public class PlumeUtils {

    /** 链路trace id key */
    public static final String TRACE_KEY = "plume_tid";

    /**
     * 获取traceId, 如果不存在, 则创建, 并且设置至TraceId
     * @return
     */
    public static String getTraceId() {
        String traceId = TraceId.logTraceID.get();
        if (traceId == null) {
            traceId = createTraceId();
            setTraceId(traceId);
        }
        return traceId;
    }

    /**
     * 创建traceId
     * @return
     */
    public static String createTraceId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 设置traceId
     * @param traceId
     */
    public static void setTraceId(String traceId) {
        TraceId.logTraceID.set(traceId);
    }

    /**
     * 清除traceId
     */
    public static void removeTraceId() {
        TraceId.logTraceID.remove();
    }
}
