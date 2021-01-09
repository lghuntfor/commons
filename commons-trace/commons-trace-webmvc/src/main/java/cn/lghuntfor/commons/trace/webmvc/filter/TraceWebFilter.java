package cn.lghuntfor.commons.trace.webmvc.filter;

import cn.hutool.core.util.StrUtil;
import cn.lghuntfor.commons.trace.common.TraceConstant;
import cn.lghuntfor.commons.trace.common.TraceContext;
import cn.lghuntfor.commons.trace.common.TraceInfo;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * web链路日志信息获取封装
 * @author liaogang
 * @date 2020/9/10
 */
@Slf4j
public class TraceWebFilter implements Filter {

    private String springApplicationName;

    public TraceWebFilter(String springApplicationName) {
        this.springApplicationName = springApplicationName;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest)) {
            chain.doFilter(request, response);
            return;
        }
        try {
            this.addTransferInfo((HttpServletRequest) request);
            chain.doFilter(request, response);
        } finally {
            /** 调用结束, 清除链路信息 */
            TraceContext.removeTrace();
        }
    }

    private void addTransferInfo(HttpServletRequest request) {
        try {
            String tid = request.getHeader(TraceConstant.TID_KEY);
            tid = (StrUtil.isBlank(tid) ? TraceContext.createTraceId() : tid);

            String pid = request.getHeader(TraceConstant.PID_KEY);
            TraceContext.addTrace(new TraceInfo(tid, pid, springApplicationName));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }


}
