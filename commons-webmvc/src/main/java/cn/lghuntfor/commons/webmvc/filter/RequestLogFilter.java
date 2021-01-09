package cn.lghuntfor.commons.webmvc.filter;

import cn.hutool.core.util.StrUtil;
import cn.lghuntfor.commons.webmvc.request.CustomHttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;

/**
 * webmvc 请求日志打印
 * @author liaogang
 * @date 2020/9/10
 */
@Slf4j
public class RequestLogFilter implements Filter {

    private static final String MULTIPART = "multipart";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            String contentType = request.getContentType();
            if (StrUtil.contains(contentType, MULTIPART)) {
                /** 文件上传的请求, 不打印body内容, 因此不需要可重复读的流 */
                handleRequestLogInfo((HttpServletRequest) request, false);
            } else {
                request = new CustomHttpServletRequestWrapper((HttpServletRequest) request);
                handleRequestLogInfo((HttpServletRequest) request, true);
            }
        }
        chain.doFilter(request, response);
        if(request instanceof HttpServletRequest){
            StringBuffer requestURL =((HttpServletRequest) request).getRequestURL();
            log.info("====== 请求链接: {} {}, end ======", ((HttpServletRequest) request).getMethod(), requestURL.toString());
        }
    }

    /**
     * 打印web请求信息, 包括以下内容:
     * 1. headers
     * 2. body
     * 3. parameters
     * @author liaogang
     * @date 2020/9/11
     * @param request
     * @param addBody
     * @return void
     */
    private void handleRequestLogInfo(HttpServletRequest request, boolean addBody) {
        StringBuffer requestURL = request.getRequestURL();
        StringBuilder requestInfo = new StringBuilder();
        requestInfo.append("====== 请求链接: ").append(request.getMethod()).append(" ")
                .append(requestURL.toString()).append(", start ======   ");
        this.getHeaders(request, requestInfo);
        if (addBody) {
            this.getBody(request, requestInfo);
        }
        this.getParameters(request, requestInfo);
        log.info(requestInfo.toString());
    }

    /**
     * 获取所有的header
     * @author liaogang
     * @date 2020/9/11
     * @param request
     * @param requestInfo
     * @return void
     */
    private void getHeaders(HttpServletRequest request, StringBuilder requestInfo) {
        Enumeration enumeration = request.getHeaderNames();
        if (enumeration != null) {
            requestInfo.append("[header ==> ");
            while (enumeration.hasMoreElements()) {
                String headerName = (String) enumeration.nextElement();
                requestInfo.append(headerName).append(":");
                requestInfo.append(request.getHeader(headerName));
                requestInfo.append(",");
            }
            requestInfo.append("]").append("   ");
        }
    }

    /**
     * 获取body的内容
     * @author liaogang
     * @date 2020/9/11
     * @param request
     * @param requestInfo
     * @return void
     */
    private void getBody(HttpServletRequest request, StringBuilder requestInfo) {
        if (request.getContentLength() <= 1024*1024) {
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
                requestInfo.append("[body ==> ");
                char[] charBuffer = new char[1024];
                int bytesRead;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    requestInfo.append(charBuffer, 0, bytesRead);
                }
                requestInfo.append("]").append("   ");
            } catch (IOException e) {
                log.error("Fail to read input stream", e);
            }
        }
    }

    /**
     * 获取所有parameter信息
     * @author liaogang
     * @date 2020/9/11
     * @param request
     * @param requestInfo
     * @return void
     */
    private void getParameters(HttpServletRequest request, StringBuilder requestInfo) {
        Enumeration parameterNames = request.getParameterNames();
        if (parameterNames != null) {
            requestInfo.append("[parameter ==> ");
            while (parameterNames.hasMoreElements()) {
                String headerName = (String) parameterNames.nextElement();
                requestInfo.append(headerName).append(":");
                requestInfo.append(request.getParameter(headerName));
                requestInfo.append(",");
            }
            requestInfo.append("]");
        }
    }

}
