package com.seeds.common.web.interceptor;

import com.seeds.common.dto.GenericDto;
import com.seeds.common.dto.LoginDTO;
import com.seeds.common.constant.redis.RedisKeys;
import com.seeds.common.web.HttpHeaders;
import com.seeds.common.web.context.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class OpenContextInterceptor implements HandlerInterceptor {

    @Autowired
    private MappingJackson2HttpMessageConverter converter;

    @Autowired
    private RedissonClient redissonClient;

    private static final GenericDto<String> INVALID_TOKEN_RESPONSE = GenericDto.failure("Invalid token", 401);
    private static final GenericDto<String> INVALID_SIGNATURE_RESPONSE = GenericDto.failure("Invalid signature", 401);
    private static final String NO_LOGIN_REQUIRED_PATH = "/open-api/";

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 外部调用签名校验
        String accessKey = request.getHeader(HttpHeaders.ACCESS_KEY);
        String signature = request.getHeader(HttpHeaders.SIGNATURE);
        if (!accessKey.equals(signature)) {
            writeFailureResponse(response, INVALID_SIGNATURE_RESPONSE);
            return false;
        }
        // 不需要进行登录校验
        if (request.getRequestURI().contains(NO_LOGIN_REQUIRED_PATH)) {
            return true;
        }
        // 需要进行登录校验
        String token = request.getHeader(HttpHeaders.USER_TOKEN);
        if (StringUtils.isEmpty(token)) {
            writeFailureResponse(response, INVALID_TOKEN_RESPONSE);
            return false;
        }
        LoginDTO user = redissonClient.<LoginDTO>getBucket(RedisKeys.getUcTokenKey(token)).get();
        if (user == null || user.getUserId() == null) {
            writeFailureResponse(response, INVALID_TOKEN_RESPONSE);
            return false;
        }
        try {
            UserContext.setCurrentUserId(user.getUserId());
            return true;
        } catch (Exception e) {
            log.error("Got invalid user id from header: {}", user.getUserId());
            writeFailureResponse(response, INVALID_TOKEN_RESPONSE);
            return false;
        }
    }

    private void writeFailureResponse(HttpServletResponse response, GenericDto<String> message) {
        try {
            converter.write(message, MediaType.APPLICATION_JSON, new ServletServerHttpResponse(response));
        } catch (IOException e) {
            log.error("response io exception: ", e);
        }
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        log.debug("remove user context");
        UserContext.removeCurrentUserId();
    }

    private boolean signatureCheck() {
        return true;
    }
}
