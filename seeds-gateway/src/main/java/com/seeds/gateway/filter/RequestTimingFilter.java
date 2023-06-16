package com.seeds.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Component
public class RequestTimingFilter implements GlobalFilter, Ordered {

    public static final String REQ_UUID = "REQ-UUID";
    public static final String REQ_RECV_TIMESTAMP = "REQ-RECV-TIMESTAMP";
    public static final String CONTENT_LANGUAGE = "content-language";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        final String uuid = UUID.randomUUID().toString();
        final long nowTimestamp = Instant.now().toEpochMilli();

        String uuidValue = ServerWebExchangeUtils.expand(exchange, uuid);
        String nowTimestampValue = ServerWebExchangeUtils.expand(exchange, Long.toString(nowTimestamp));
        ServerHttpRequest request = exchange.getRequest().mutate()
                .headers(httpHeaders -> {
                    httpHeaders.set(REQ_UUID, uuidValue);
                    httpHeaders.set(REQ_RECV_TIMESTAMP, nowTimestampValue);
                })
                .build();

        return chain.filter(exchange.mutate().request(request).build())
                .then(Mono.fromRunnable(() ->
                        log.debug("Total time cost: {} ms, for URL {}， req ID: {}, and RECV-TIMESTAMP: {}",
                                Instant.now().toEpochMilli() - nowTimestamp,
                                request.getPath().toString(),
                                request.getHeaders().getFirst(REQ_UUID),
                                request.getHeaders().getFirst(REQ_RECV_TIMESTAMP))));
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}
