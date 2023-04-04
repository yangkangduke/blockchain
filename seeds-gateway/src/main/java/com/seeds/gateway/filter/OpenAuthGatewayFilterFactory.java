package com.seeds.gateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seeds.common.web.HttpHeaders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Slf4j
@Component
public class OpenAuthGatewayFilterFactory extends AbstractGatewayFilterFactory<OpenAuthGatewayFilterFactory.Config> {

    @Autowired
    private ObjectMapper objectMapper;

    public OpenAuthGatewayFilterFactory(ObjectMapper objectMapper) {
        super(Config.class);
        this.objectMapper = objectMapper;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> success(chain, exchange);
    }

    private Mono<Void> success(GatewayFilterChain chain, ServerWebExchange exchange) {
        ServerHttpRequest.Builder builder = exchange.getRequest().mutate();
        builder.header(HttpHeaders.OPEN_AUTH, HttpHeaders.OPEN_AUTH);
        return chain.filter(exchange.mutate().request(builder.build()).build());
    }

    public static class Config {

    }
}
