package com.seeds.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seeds.admin.constant.WhiteListPath;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.HttpHeaders;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;

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
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            URI uri = request.getURI();
            log.debug("get into open auth gateway filter, URL信息: {}", uri);
            // 不需要鉴权的
            if (uri.toString().contains(WhiteListPath.DOC_API)) {
                return success(chain, exchange, null, null);
            }
            String accessKey = request.getHeaders().getFirst(HttpHeaders.ACCESS_KEY);
            Map<String, String> paramsMap = request.getQueryParams().toSingleValueMap();
            if (StringUtils.isEmpty(accessKey)) {
                accessKey = paramsMap.get(HttpHeaders.ACCESS_KEY);
            }
            String signature = request.getHeaders().getFirst(HttpHeaders.SIGNATURE);
            if (StringUtils.isEmpty(signature)) {
                signature = paramsMap.get(HttpHeaders.SIGNATURE);
            }
            if (StringUtils.isEmpty(accessKey) || StringUtils.isEmpty(signature)) {
                return failure(exchange);
            } else {
                log.debug("Verify success, accessKey: {}, signature: {}", accessKey, signature);
                return success(chain, exchange, accessKey, signature);
            }
        };
    }

    private Mono<Void> success(GatewayFilterChain chain, ServerWebExchange exchange, String accessKey, String signature) {
        ServerHttpRequest.Builder builder = exchange.getRequest().mutate();
        builder.header(HttpHeaders.ACCESS_KEY, String.valueOf(accessKey));
        builder.header(HttpHeaders.SIGNATURE, String.valueOf(signature));
        return chain.filter(exchange.mutate().request(builder.build()).build());
    }

    private Mono<Void> failure(ServerWebExchange exchange) {
        log.debug("verify failed");
        ServerHttpResponse serverHttpResponse = exchange.getResponse();
        serverHttpResponse.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        DataBuffer buffer = serverHttpResponse.bufferFactory().wrap(getErrorBytes());
        // set status
        ServerWebExchangeUtils.setResponseStatus(exchange, HttpStatus.OK);
        // set already routed
        ServerWebExchangeUtils.setAlreadyRouted(exchange);
        return serverHttpResponse.writeWith(Mono.just(buffer));
    }

    private byte[] getErrorBytes() {
        try {
            return objectMapper.writeValueAsBytes(GenericDto.failure("Invalid Signature", 401));
        } catch (JsonProcessingException e) {
            return "".getBytes();
        }
    }

    public static class Config {

    }
}
