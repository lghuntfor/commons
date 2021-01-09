package cn.lghuntfor.commons.trace.feign.interceptor;

import cn.lghuntfor.commons.trace.common.TraceConstant;
import cn.lghuntfor.commons.trace.common.TraceContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;

/**
 * feign 拦截器
 * @author lghuntfor
 * @date 2020/11/29
 */
public class TraceFeignInterceptor implements RequestInterceptor {

    @Value("${spring.application.name:}")
    private String springApplicationName;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String traceId = TraceContext.getTraceId();
        requestTemplate.header(TraceConstant.TID_KEY, traceId);
        requestTemplate.header(TraceConstant.PID_KEY, springApplicationName);
    }
}
