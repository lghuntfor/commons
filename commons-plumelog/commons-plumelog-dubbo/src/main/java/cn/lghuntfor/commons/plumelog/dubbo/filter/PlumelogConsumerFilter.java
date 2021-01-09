package cn.lghuntfor.commons.plumelog.dubbo.filter;

import cn.lghuntfor.commons.plumelog.common.PlumeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

/**
 *
 * @author lghuntfor
 * @date 2020/12/6
 */
@Slf4j
@Activate(group = CommonConstants.CONSUMER, order = -9999)
public class PlumelogConsumerFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String traceId = PlumeUtils.getTraceId();
        String classMethodName = invoker.getInterface().getSimpleName()+"."+invocation.getMethodName();

        long startTime = System.currentTimeMillis();
        invocation.setAttachment(PlumeUtils.TRACE_KEY, traceId);
        Result result = invoker.invoke(invocation);
//        log.info("{}, consumer elapsed time = {}ms", classMethodName, (System.currentTimeMillis() - startTime));
        return result;
    }
}
