package com.seeds.gateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seeds.common.web.HttpHeaders;
import com.seeds.common.web.context.UserContext;
import com.seeds.gateway.service.AuthService;
import com.seeds.uc.dto.redis.LoginUserDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

    @Autowired
    private AuthService authService;

    public OpenAuthGatewayFilterFactory(ObjectMapper objectMapper) {
        super(Config.class);
        this.objectMapper = objectMapper;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String ucToken = exchange.getRequest().getHeaders().getFirst(HttpHeaders.USER_TOKEN);
            if (StringUtils.isNotBlank(ucToken)) {
                LoginUserDTO ucUser = authService.verify(ucToken);
                if (ucUser != null) {
                    UserContext.setCurrentUserId(ucUser.getUserId());
                }
            }
            return success(chain, exchange);
        };
    }

    private Mono<Void> success(GatewayFilterChain chain, ServerWebExchange exchange) {
        ServerHttpRequest.Builder builder = exchange.getRequest().mutate();
        builder.header(HttpHeaders.OPEN_AUTH, HttpHeaders.OPEN_AUTH);
        return chain.filter(exchange.mutate().request(builder.build()).build());
    }

    public static class Config {

    }
}
