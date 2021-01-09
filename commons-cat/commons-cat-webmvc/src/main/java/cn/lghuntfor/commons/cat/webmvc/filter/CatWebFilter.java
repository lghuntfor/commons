package cn.lghuntfor.commons.cat.webmvc.filter;

import cn.lghuntfor.commons.cat.common.CatContext;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

/**
 * cat 拦截器
 * @author lghuntfor
 * @since 2018/11/6
 */
public class CatWebFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest)) {
            filterChain.doFilter(request, response);
            return;
        }
        HttpServletRequest req = (HttpServletRequest) request;

        logRemoteCallServer(req);

        Transaction t = Cat.newTransaction("URL", req.getRequestURI());
        try {
            Cat.logEvent("URL.Method", req.getMethod(), Message.SUCCESS, req.getRequestURI());
            Cat.logEvent("URL.Host", req.getMethod(), Message.SUCCESS, getRemoteAddr(req));
            Cat.logMetricForCount("ApiCount");
            filterChain.doFilter(request, response);
            t.setStatus(Transaction.SUCCESS);
        } catch (Exception e) {
            t.setStatus(e);
            Cat.logError(e);
            throw e;
        } finally {
            t.complete();
        }
    }

    private void logRemoteCallServer(HttpServletRequest req) {
        CatContext context = new CatContext();
        context.addProperty(CatContext.ROOT, Optional.ofNullable(req.getHeader(CatContext.ROOT)).orElse(Cat.createMessageId()));
        context.addProperty(CatContext.PARENT, Optional.ofNullable(req.getHeader(CatContext.PARENT)).orElse(Cat.createMessageId()));
        context.addProperty(CatContext.CHILD, Optional.ofNullable(req.getHeader(CatContext.CHILD)).orElse(Cat.createMessageId()));
        Cat.logRemoteCallServer(context);
    }

    /**
     * 获得用户远程地址
     */
    private String getRemoteAddr(HttpServletRequest request) {
        String remoteAddr = request.getHeader("X-Real-IP");
        if (StringUtils.isEmpty(remoteAddr)) {
            remoteAddr = request.getHeader("X-Forwarded-For");
        } else if (StringUtils.isEmpty(remoteAddr)) {
            remoteAddr = request.getHeader("Proxy-Client-IP");
        } else if (StringUtils.isEmpty(remoteAddr)) {
            remoteAddr = request.getHeader("WL-Proxy-Client-IP");
        }
        return remoteAddr != null ? remoteAddr : request.getRemoteAddr();
    }
}
