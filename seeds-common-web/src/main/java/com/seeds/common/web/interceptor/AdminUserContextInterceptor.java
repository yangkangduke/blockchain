package com.seeds.common.web.interceptor;

import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.HttpHeaders;
import com.seeds.common.web.context.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
public class AdminUserContextInterceptor implements HandlerInterceptor {

    @Autowired
    private MappingJackson2HttpMessageConverter converter;

    private static final GenericDto<String> INVALID_TOKEN_RESPONSE = GenericDto.failure("Invalid token", 401);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userIdStr = request.getHeader(HttpHeaders.ADMIN_USER_ID);
        if (StringUtils.isEmpty(userIdStr)) {
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

}