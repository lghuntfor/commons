package cn.lghuntfor.commons.trace.dubbo;

import cn.hutool.core.util.StrUtil;
import cn.lghuntfor.commons.common.spring.SpringUtils;
import cn.lghuntfor.commons.trace.common.TraceConstant;
import cn.lghuntfor.commons.trace.common.TraceContext;
import cn.lghuntfor.commons.trace.common.TraceInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

/**
 * dubbo调用链路日志信息获取封装
 * 基于dubbo spi机制
 * @author liaogang
 * @date 2020/9/10 17:06
 */
@Slf4j
@Activate(group = {CommonConstants.PROVIDER, CommonConstants.CONSUMER},order = -9000)
public class TraceDubboFilter implements Filter {

    private String springApplicationName = null;

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        URL url = invoker.getUrl();
        String sideKey = url.getParameter(CommonConstants.SIDE_KEY);
        String cid = this.getSpringApplicationName();
        String classMethodName = invoker.getInterface().getSimpleName()+"."+invocation.getMethodName();
        if(CommonConstants.CONSUMER_SIDE.equals(sideKey)){
            addTraceInfo(cid);

            long startTime = System.currentTimeMillis();
            Result result = invoker.invoke(invocation);
            log.info("{}, consumer elapsed time = {}ms", classMethodName, (System.currentTimeMillis() - startTime));
            return result;
        }

        try {
            getTraceInfo(cid);

            long startTime = System.currentTimeMillis();
            Result result = invoker.invoke(invocation);
            log.info("{}, provider elapsed time = {}ms", classMethodName, (System.currentTimeMillis() - startTime));
            return result;
        } finally {
            /** provider端, 调用完成后, 清除信息 */
            TraceContext.removeTrace();
        }
    }

    /**
     * 获取传递信息并设置到对应的ThreadLocal中
     * @author liaogang
     * @date 2020/9/18
     * @param cid
     * @return void
     */
    private void getTraceInfo(String cid) {
        try {
            /** 作为 provider 端, 获取链路信息并放入TraceContext, 若tid不存在, 则手动创建一个 */
            String tid = RpcContext.getContext().getAttachment(TraceConstant.TID_KEY);
            tid = (StrUtil.isBlank(tid) ? TraceContext.createTraceId() : tid);

            String pid = RpcContext.getContext().getAttachment(TraceConstant.PID_KEY);
            TraceContext.addTrace(new TraceInfo(tid, pid, cid));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 获取信息并传递到dubbo attachment中
     * @author liaogang
     * @date 2020/9/18
     * @param cid
     * @return void
     */
    private void addTraceInfo(String cid) {
        try {
            /** 作为 consumer 端, 传递链路信息 */
            String tid = TraceContext.getTraceId();
            RpcContext.getContext().setAttachment(TraceConstant.TID_KEY, tid);
            RpcContext.getContext().setAttachment(TraceConstant.PID_KEY, cid);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 获取当前应用名
     */
    private String getSpringApplicationName() {
        if (StrUtil.isNotBlank(springApplicationName)) {
            return springApplicationName;
        }
        springApplicationName = SpringUtils.getPropertyValue("spring.application.name");
        return springApplicationName;
    }
}
