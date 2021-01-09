package cn.lghuntfor.commons.plumelog.gateway.filter;

import cn.lghuntfor.commons.plumelog.common.PlumeUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * cat gateway过滤器
 * @author lghuntfor
 * @date 2020/11/28
 */
public class PlumelogGatewayFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        List<String> traceIds = request.getHeaders().get(PlumeUtils.TRACE_KEY);
        String traceId = null;
        if (!CollectionUtils.isEmpty(traceIds)) {
            traceId = traceIds.get(0);
        } else {
            traceId = PlumeUtils.getTraceId();
        }
        PlumeUtils.setTraceId(traceId);
        ServerHttpRequest req = request.mutate()
            .header(PlumeUtils.TRACE_KEY, traceId)
            .build();

        return chain.filter(exchange.mutate().request(req).build());
    }

    @Override
    public int getOrder() {
        return -Integer.MAX_VALUE;
    }
}
