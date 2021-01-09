package cn.lghuntfor.commons.plumelog.dubbo.filter;

import cn.lghuntfor.commons.plumelog.common.PlumeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.springframework.util.StringUtils;

/**
 *
 * @author lghuntfor
 * @date 2020/12/6
 */
@Slf4j
@Activate(group = CommonConstants.PROVIDER, order = -9999)
public class PlumelogProviderFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String traceId = invocation.getAttachment(PlumeUtils.TRACE_KEY);
        if (StringUtils.isEmpty(traceId)) {
            traceId = PlumeUtils.createTraceId();
        }
        try {
            PlumeUtils.setTraceId(traceId);
            String classMethodName = invoker.getInterface().getSimpleName()+"."+invocation.getMethodName();
            long startTime = System.currentTimeMillis();
            Result result = invoker.invoke(invocation);
//            log.info("{}, provider elapsed time = {}ms", classMethodName, (System.currentTimeMillis() - startTime));
            return result;
        } finally {
            PlumeUtils.removeTraceId();
        }
    }
}
