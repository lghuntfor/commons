package cn.lghuntfor.commons.plumelog.webmvc.filter;

import cn.lghuntfor.commons.plumelog.common.PlumeUtils;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * plume log web拦截器
 * @author lghuntfor
 * @since 2018/11/6
 */
public class PlumelogWebFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest)) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            HttpServletRequest req = (HttpServletRequest) request;
            String traceId = req.getHeader(PlumeUtils.TRACE_KEY);
            if (StringUtils.isEmpty(traceId)) {
                traceId = PlumeUtils.createTraceId();
            }
            PlumeUtils.setTraceId(traceId);
            filterChain.doFilter(request, response);
        } finally {
            PlumeUtils.removeTraceId();
        }
    }

}
