package cn.lghuntfor.commons.cat.gateway.filter;

import cn.lghuntfor.commons.cat.common.CatContext;
import com.dianping.cat.Cat;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * cat gateway过滤器
 * @author lghuntfor
 * @date 2020/11/28
 */
public class CatGatewayFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        CatContext context = new CatContext();
        Cat.logRemoteCallClient(context);
        Cat.logMetricForCount("GatewayApi");
        Cat.logEvent("URL", request.getURI().getPath());

        ServerHttpRequest req = request.mutate()
            .header(CatContext.ROOT, context.getProperty(CatContext.ROOT))
            .header(CatContext.PARENT, context.getProperty(CatContext.PARENT))
            .header(CatContext.CHILD, context.getProperty(CatContext.CHILD))
            .build();

        return chain.filter(exchange.mutate().request(req).build());
    }

    @Override
    public int getOrder() {
        return -Integer.MAX_VALUE;
    }
}
