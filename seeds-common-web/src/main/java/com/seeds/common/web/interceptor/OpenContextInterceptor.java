package com.seeds.common.web.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.dto.LoginDTO;
import com.seeds.common.constant.redis.RedisKeys;
import com.seeds.common.web.HttpHeaders;
import com.seeds.common.web.context.UserContext;
import com.seeds.common.web.util.HttpHelper;
import com.seeds.common.web.util.SignatureUtils;
import com.seeds.common.web.wrapper.BodyReaderHttpServletRequestWrapper;
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

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.Set;

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
    private static final String GET_METHOD = "GET";
    private static final String POST_METHOD = "POST";

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 网关调用校验
        String openAuth = request.getHeader(HttpHeaders.OPEN_AUTH);
        if (!HttpHeaders.OPEN_AUTH.equals(openAuth)) {
            writeFailureResponse(response, INVALID_SIGNATURE_RESPONSE);
            return false;
        }
        // 验签
        if (!signatureCheck(request)) {
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

    private boolean signatureCheck(HttpServletRequest request) throws IOException {
        // 判断请求方式
        String method = request.getMethod();
        if (POST_METHOD.equals(method)) {
            log.info("POST请求进入...");
            // 获取请求Body参数，需要使用 BodyReaderHttpServletRequestWrapper进行处理
            // 否则会出现异常：I/O error while reading input message； nested exception is java.io.IOException: Stream closed
            // 原因就是在拦截器已经读取了请求体中的内容，这时候Request请求的流中已经没有了数据
            // 解决流只能读取一次的问题：先读取流，然后在将流重新写进去就行了
            ServletRequest requestWrapper = new BodyReaderHttpServletRequestWrapper(request);
            String body = HttpHelper.getBodyString(requestWrapper);
            String bodyString = URLDecoder.decode(body, "utf-8");
            if (StringUtils.isEmpty(bodyString)) {
                return false;
            }
            // 解析参数转JSON格式
            JSONObject jsonObject = JSONObject.parseObject(bodyString);
            // 验签
            return SignatureUtils.validation(jsonObject);
        }
        if (GET_METHOD.equals(method)) {
            log.info("GET请求进入...");
            // 获取请求参数
            Map<String, String> allRequestParam = HttpHelper.getAllRequestParam(request);
            Set<Map.Entry<String, String>> entries = allRequestParam.entrySet();
            // 参数转JSON格式
            JSONObject jsonObject = new JSONObject();
            entries.forEach(key -> {
                jsonObject.put(key.getKey(), key.getValue());
            });
            // 验签
            return SignatureUtils.validation(jsonObject);
        }
        return false;
    }
}