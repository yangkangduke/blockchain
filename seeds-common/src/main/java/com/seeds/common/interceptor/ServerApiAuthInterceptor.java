//package com.seeds.common.interceptor;
//
//import com.seeds.common.crypto.RequestConfiguration;
//import com.seeds.common.crypto.SignatureHandler;
//import com.seeds.common.crypto.TimeUtils;
//import com.seeds.common.exception.BadSigException;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.io.IOUtils;
//import org.springframework.http.MediaType;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.http.server.ServletServerHttpResponse;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.time.Duration;
//import java.time.ZonedDateTime;
//import java.util.Map;
//
///**
// * @author rickierao
// * @email antilaw@yahoo.com
// * @date 2021/1/25
// */
//
//@Slf4j
//public class ServerApiAuthInterceptor implements HandlerInterceptor {
//    private Map<String, String> authMapping;
//    private RequestConfiguration requestConfiguration;
//    private Long allowedTimestampRange;
//
//    private static final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//
//    private static final SignResponse BAD_SIGN_RES =
//            SignResponse.builder().code(401).success(false).message("error checking signature").build();
//
//    public ServerApiAuthInterceptor(Map<String, String> authMapping,
//                                    RequestConfiguration requestConfiguration,
//                                    Long allowedTimestampRange) {
//        this.authMapping = authMapping;
//        this.requestConfiguration = requestConfiguration;
//        this.allowedTimestampRange = allowedTimestampRange;
//    }
//
//    @Override
//    public boolean preHandle(HttpServletRequest servletRequest, HttpServletResponse response, Object handler) {
//        String timestamp = servletRequest.getHeader(requestConfiguration.getTimestampHttpHeader());
//        try {
//            // check timestamp
//            if (!validateTimestamp(timestamp)) {
//                throw new BadSigException("Exceeded allowed range timestamp: " + timestamp);
//            }
//            String signature = servletRequest.getHeader(requestConfiguration.getSignatureHttpHeader());
//            String accessKey = servletRequest.getHeader(requestConfiguration.getAccessKeyHttpHeader());
//            String secretKey = authMapping.get(accessKey);
//            String method = servletRequest.getMethod();
//            String path = servletRequest.getRequestURI().substring(servletRequest.getContextPath().length());
//            byte[] content = safelyGetContent(servletRequest);
//            SignatureHandler.check(signature, secretKey, method, timestamp, path, content);
//            log.debug("request auth passed");
//            return true;
//        } catch (BadSigException bse) {
//            log.error("Wrong Signature", bse);
//            writeBadSign(response);
//            return false;
//        } catch (Exception e) {
//            log.error("Error while checking Signature", e);
//            return false;
//        }
//    }
//
//    private void writeBadSign(HttpServletResponse resp) {
//        try {
//            converter.write(BAD_SIGN_RES, MediaType.APPLICATION_JSON, new ServletServerHttpResponse(resp));
//        } catch (IOException e) {
//            log.error("response io exception: ", e);
//        }
//    }
//
//    private boolean validateTimestamp(String timestamp) {
//        ZonedDateTime requestTime = TimeUtils.parse(timestamp);
//        long difference = Math.abs(Duration.between(requestTime, TimeUtils.nowInUTC()).toMillis());
//        return difference <= allowedTimestampRange;
//    }
//
//    private byte[] safelyGetContent(HttpServletRequest request) {
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        try {
//            InputStream in = request.getInputStream();
//            IOUtils.copy(in, out);
//            return out.toByteArray();
//        } catch (IOException ex) {
//            throw new RuntimeException(ex);
//        }
//    }
//
//
//    @Data
//    @Builder
//    @AllArgsConstructor
//    @NoArgsConstructor
//    private static class SignResponse {
//        private int code;
//        private boolean success;
//        private String message;
//    }
//}
