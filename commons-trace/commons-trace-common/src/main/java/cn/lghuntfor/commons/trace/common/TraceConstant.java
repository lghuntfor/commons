package cn.lghuntfor.commons.trace.common;

/**
 * 链路日志的常量
 * @author liaogang
 * @date 2020/9/10 11:49
 */
public interface TraceConstant {

    /** traceId简写, 一次请求链路的id, 贯穿整个链路 */
    String TID_KEY = "tid";

    /** 调用的上一次的id, 通常取应用的spring.application.name */
    String PID_KEY = "pid";

    /** 当前应用的id, 通常取当前应用的spring.application.name */
    String CID_KEY = "cid";

}
