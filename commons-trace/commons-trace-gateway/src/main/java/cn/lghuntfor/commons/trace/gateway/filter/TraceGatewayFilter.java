package cn.lghuntfor.commons.trace.gateway.filter;

import com.alibaba.fastjson.JSON;
import cn.lghuntfor.commons.common.result.ReturnData;
import cn.lghuntfor.commons.trace.common.TraceConstant;
import cn.lghuntfor.commons.trace.common.TraceContext;
import cn.lghuntfor.commons.trace.common.TraceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * 链路日志处理过滤器
 * @author liaogang
 * @date 2020/9/10 11:28
 */
@Slf4j
public class TraceGatewayFilter implements GlobalFilter, Ordered {

    @Value("${spring.application.name:}")
    private String springApplicationName;

    @Override
    public int getOrder() {
        /** 用于排序, 值越小越靠前, log优先级最高 */
        return -Integer.MAX_VALUE;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        /** 入口处, 自行创建一个traceId */
        String tid = TraceContext.createTraceId();
        TraceInfo traceInfo = new TraceInfo(tid, springApplicationName);
        TraceContext.addTrace(traceInfo);
        request = request.mutate().header(TraceConstant.TID_KEY, traceInfo.getTid()).header(TraceConstant.PID_KEY, traceInfo.getCid()).build();

        handleRequestLogInfo(request);
        try {
            return chain.filter(exchange.mutate().request(request).build());
        } catch (Exception e) {
            return respMsg(exchange.getResponse(), HttpStatus.INTERNAL_SERVER_ERROR, "服务端异常");
        } finally {
            TraceContext.removeTrace();
        }
    }

    public static Mono<Void> respMsg(ServerHttpResponse resp, HttpStatus httpStatus, String msg) {
        resp.setStatusCode(httpStatus);
        resp.getHeaders().add("Content-Type","application/json;charset=UTF-8");
        ReturnData<String> returnData = new ReturnData<>(httpStatus.value(), msg, null);
        String returnStr = JSON.toJSONString(returnData);
        DataBuffer buffer = resp.bufferFactory().wrap(returnStr.getBytes(StandardCharsets.UTF_8));
        return resp.writeWith(Flux.just(buffer));
    }

    /**
     * 请求信息打印
     * @author liaogang
     * @date 2020/9/16
     * @param request
     * @return void
     */
    private void handleRequestLogInfo(ServerHttpRequest request) {
        String methodValue = request.getMethodValue();
        String uri = request.getURI().toString();
        StringBuilder requestInfo = new StringBuilder();
        requestInfo.append("====== 请求链接: ")
            .append(methodValue)
            .append(" ").append(uri)
            .append(", start ======   ");
        this.getHeaders(request, requestInfo);
        this.getParameters(request, requestInfo);
        log.info(requestInfo.toString());
    }

    /**
     * 获取所有header
     * @author liaogang
     * @date 2020/9/16
     * @param request
     * @param requestInfo
     * @return void
     */
    private void getHeaders(ServerHttpRequest request, StringBuilder requestInfo) {
        HttpHeaders headers = request.getHeaders();
        if (headers != null) {
            Map<String, String> headerMap = headers.toSingleValueMap();
            requestInfo.append("[header => ");
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                String headerName = entry.getKey();
                requestInfo.append(headerName).append(":");
                requestInfo.append(entry.getValue());
                requestInfo.append(",");
            }
            requestInfo.append("]").append("   ");
        }
    }

    /**
     * 获取所有参数
     * @author liaogang
     * @date 2020/9/16
     * @param request
     * @param requestInfo
     * @return void
     */
    private void getParameters(ServerHttpRequest request, StringBuilder requestInfo) {
        MultiValueMap<String, String> params = request.getQueryParams();
        if (params != null) {
            requestInfo.append("[parameter => ");
            for (Map.Entry<String, List<String>> param : params.entrySet()) {
                String headerName =  param.getKey();
                requestInfo.append(headerName).append(":");
                requestInfo.append(param.getValue());
                requestInfo.append(",");
            }
            requestInfo.append("]");
        }
    }
}
