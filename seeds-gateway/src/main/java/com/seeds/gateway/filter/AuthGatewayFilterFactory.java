package com.seeds.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.HttpHeaders;
import com.seeds.uc.dto.redis.LoginUserDTO;
import com.seeds.gateway.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

@Slf4j
@Component
public class AuthGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthGatewayFilterFactory.Config> {

    public static String SWAGGER_URI = "/v2/api-docs";
    public static String AUTH = "/auth/";
    public static String PUBLIC = "/public/";
    @Autowired
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    public AuthGatewayFilterFactory(AuthService authService, ObjectMapper objectMapper) {
        super(Config.class);
        this.authService = authService;
        this.objectMapper = objectMapper;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // 通过网关的为外部调用
            exchange.getRequest().mutate().header(HttpHeaders.INNER_REQUEST, Boolean.FALSE.toString());
            log.debug("get into auth gateway filter");
            URI uri = exchange.getRequest().getURI();
            log.info("URL信息: {}", uri);
            // 不需要鉴权的
            if (uri.toString().contains(SWAGGER_URI) || uri.toString().contains(AUTH) || uri.toString().contains(PUBLIC)){
                return success(chain, exchange, null);
            }
            String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.USER_TOKEN);
            HttpCookie tokenCookie = exchange.getRequest().getCookies().getFirst(HttpHeaders.USER_TOKEN);
            if (StringUtils.hasText(token)) {
                log.debug("No token found from header, start getting from cookie");
                if (tokenCookie != null) {
                    log.debug("got token from cookie");
                    token = tokenCookie.getValue();
                } else {
                    log.debug("no token found from cookie");
                }
            }
            if (!StringUtils.hasText(token)) {
                log.debug("no token found from either cookie or header");
                return failure(exchange);
            }
            LoginUserDTO loginUser = authService.verify(token);
            if (loginUser == null) {
                return failure(exchange);
            } else {
                log.debug("Verify success, got user: {}", loginUser);
                return success(chain, exchange, loginUser.getUserId());
            }
        };
    }

    private Mono<Void> success(GatewayFilterChain chain, ServerWebExchange exchange, Long userId) {
        ServerHttpRequest.Builder builder = exchange.getRequest().mutate();
        builder.header(HttpHeaders.INTERNAL_USER_ID, String.valueOf(userId));
        return chain.filter(exchange.mutate().request(builder.build()).build());
    }

    private Mono<Void> failure(ServerWebExchange exchange) {
        //unauthorized request to be blocked
        log.debug("token verify failed");
        ServerHttpResponse serverHttpResponse = exchange.getResponse();
        serverHttpResponse.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        DataBuffer buffer = serverHttpResponse.bufferFactory().wrap(getErrorBytes("Invalid token"));
        HttpCookie httpCookie = exchange.getRequest().getCookies().getFirst(HttpHeaders.USER_TOKEN);
        if (httpCookie != null) {
            log.debug("remove cookie: {}", HttpHeaders.USER_TOKEN);
            ResponseCookie cookie = ResponseCookie.from(httpCookie.getName(), httpCookie.getValue())
                    .maxAge(0)
                    .build();
            serverHttpResponse.getCookies().set(HttpHeaders.USER_TOKEN, cookie);
        }
        // set status
        ServerWebExchangeUtils.setResponseStatus(exchange, HttpStatus.OK);
        // set already routed
        ServerWebExchangeUtils.setAlreadyRouted(exchange);
        return serverHttpResponse.writeWith(Mono.just(buffer));
    }

    private byte[] getErrorBytes(String errorMessage) {
        try {
            return objectMapper.writeValueAsBytes(GenericDto.failure(errorMessage, 401));
        } catch (JsonProcessingException e) {
            return "".getBytes();
        }
    }

    public static class Config {

    }
}
