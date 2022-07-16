package com.seeds.gateway.config;

import com.seeds.common.web.HttpHeaders;
import com.seeds.gateway.util.GatewayUtils;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

@Configuration
public class GatewayConfig {

    @Bean(name = "defaultResolver")
    public KeyResolver defaultResolver() {
        return exchange -> {
            String userId = exchange.getRequest().getHeaders().getFirst(HttpHeaders.INTERNAL_USER_ID);
            if (StringUtils.hasText(userId)) {
                return Mono.just(userId);
            }
            userId = exchange.getRequest().getHeaders().getFirst(HttpHeaders.ADMIN_USER_ID);
            if (StringUtils.hasText(userId)) {
                return Mono.just(userId);
            }
            return Mono.just(GatewayUtils.getClientIp(exchange));
        };
    }
}
