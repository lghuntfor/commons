package cn.lghuntfor.commons.trace.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 链路日志信息对象
 * @author liaogang
 * @date 2020/9/10 10:46
 */
@Data
public class TraceInfo implements Serializable {

    /** traceId简写, 一次请求链路的id, 贯穿整个链路 */
    private String tid;

    /** 调用的上一次的id, 通常取应用的spring.application.name */
    private String pid;

    /** 当前应用的id, 通常取当前应用的spring.application.name */
    private String cid;

    public TraceInfo() { }

    public TraceInfo(String tid) {
        this.tid = checkTid(tid);
    }

    public TraceInfo(String tid, String cid) {
        this.tid = checkTid(tid);
        this.cid = cid;
    }

    public TraceInfo(String tid, String pid, String cid) {
        this.tid = checkTid(tid);
        this.pid = pid;
        this.cid = cid;
    }

    private String checkTid(String tid) {
        if (tid != null) {
            tid = TraceContext.createTraceId();
        }
        return tid;
    }

    /**
     * 重写toString(), 方便日志打印的格式转换
     * @return
     */
    @Override
    public String toString() {
        return "{" +
                "tid=" + tid +
                ",pid=" + pid +
                ",cid=" + cid +
                "}";
    }
}
