package cn.lghuntfor.commons.trace.common;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 链路日志的上下文对象
 * 需要在每个链路入口与出口传道对应的trace信息
 * @author liaogang
 * @date 2020/9/10 10:48
 */
public class TraceContext {

    /**
     * 日志跟踪的线程共享变量
     */
    private static final InheritableThreadLocal<TraceInfo> TRACE_INFO = new InheritableThreadLocal<>();

    /**
     * 添加当前链路日志的trace信息
     * @author liaogang
     * @date 2020/9/10
     * @param traceInfo
     */
    public static void addTrace(TraceInfo traceInfo) {
        TRACE_INFO.set(traceInfo);
    }

    /**
     * 获取当前链路日志的trace信息
     * @author liaogang
     * @date 2020/9/10
     * @return TraceInfo
     */
    public static TraceInfo getTrace() {
        return TRACE_INFO.get();
    }

    /**
     * 获取当前链路日志的traceId
     * @author liaogang
     * @date 2020/9/10
     * @return java.lang.String
     */
    public static String getTraceId() {
        TraceInfo trace = TRACE_INFO.get();
        /** 如果当前没有trace信息, 则自己创建一个traceId, 比如内部定时任务的场景 */
        if (trace == null || StrUtil.isEmpty(trace.getTid())) {
            String tid = createTraceId();
            if (trace == null) {
                addTrace(new TraceInfo(tid));
            } else {
                trace.setTid(tid);
            }
            return tid;
        }
        return trace.getTid();
    }

    /**
     * 移除当前链路日志的trace信息
     * @author liaogang
     * @date 2020/9/10
     * @return void
     */
    public static void removeTrace() {
        TRACE_INFO.remove();
    }

    /**
     * 在此处定义统一的traceId生成方法, 方便统一修改
     * @author liaogang
     * @date 2020/9/24
     * @return java.lang.String
     */
    public static String createTraceId() {
        return IdUtil.fastSimpleUUID();
    }

}
