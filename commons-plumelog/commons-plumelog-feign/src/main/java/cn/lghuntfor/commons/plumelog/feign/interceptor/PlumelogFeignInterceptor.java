package cn.lghuntfor.commons.plumelog.feign.interceptor;

import cn.lghuntfor.commons.plumelog.common.PlumeUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * feign 拦截器
 * @author lghuntfor
 * @date 2020/11/27
 */
public class PlumelogFeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(PlumeUtils.TRACE_KEY, PlumeUtils.getTraceId());
    }
}
