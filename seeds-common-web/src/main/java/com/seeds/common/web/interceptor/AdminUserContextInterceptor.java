package com.seeds.common.web.interceptor;

import com.seeds.common.dto.GenericDto;
import com.seeds.common.redis.constant.RedisKeys;
import com.seeds.common.web.HttpHeaders;
import com.seeds.common.web.context.UserContext;
import com.seeds.common.web.inner.Inner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class AdminUserContextInterceptor implements HandlerInterceptor {

    @Autowired
    private MappingJackson2HttpMessageConverter converter;

    @Autowired
    private RedissonClient redissonClient;

    private static final GenericDto<String> INVALID_TOKEN_RESPONSE = GenericDto.failure("Invalid token", 401);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (getInnerAnnotation(handler) != null && Boolean.parseBoolean(request.getHeader(HttpHeaders.INNER_REQUEST))) {
            return true;
        }
        String token = request.getHeader(HttpHeaders.ADMIN_USER_TOKEN);
        if (StringUtils.isEmpty(token)) {
            log.info("Token is empty");
            writeFailureResponse(response);
            return false;
        }
        String userIdStr = request.getHeader(HttpHeaders.ADMIN_USER_ID);
        if (StringUtils.isEmpty(userIdStr)) {
            log.info("UserId is empty");
            writeFailureResponse(response);
            return false;
        }
        String redisToken = redissonClient.<String>getBucket(RedisKeys.getAdminUserIdKey(Long.valueOf(userIdStr))).get();
        if (!token.equals(redisToken)) {
            log.info("Token is not right, redisToken={}, token={}", redisToken, token);
            writeFailureResponse(response);
            return false;
        }
        try {
            UserContext.setCurrentAdminUserId(Long.valueOf(userIdStr));
            return true;
        } catch (Exception e) {
            log.error("Got admin user id from header: {}", userIdStr);
            writeFailureResponse(response);
            return false;
        }
    }

    private void writeFailureResponse(HttpServletResponse response) {
        try {
            converter.write(INVALID_TOKEN_RESPONSE, MediaType.APPLICATION_JSON, new ServletServerHttpResponse(response));
        } catch (IOException e) {
            log.error("response io exception: ", e);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        log.debug("remove user context");
        UserContext.removeCurrentAdminUserId();
    }

    private Inner getInnerAnnotation(Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Inner inner = handlerMethod.getMethodAnnotation(Inner.class);
        if (inner == null) {
            inner = handlerMethod.getMethod().getDeclaringClass().getAnnotation(Inner.class);
        }
        return inner;
    }

}
