package com.seeds.common.interceptor;

import com.seeds.common.crypto.RequestConfiguration;
import com.seeds.common.crypto.SignatureHandler;
import com.seeds.common.crypto.TimeUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

/**
 * @author rickierao
 * @email antilaw@yahoo.com
 * @date 2021/1/25
 */
@Slf4j
public class ClientSignRequestInterceptor implements RequestInterceptor {
    private final String accessKey;
    private final String secretKey;
    private final RequestConfiguration requestConfiguration;

    public ClientSignRequestInterceptor(String accessKey, String secretKey, RequestConfiguration requestConfiguration) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.requestConfiguration = requestConfiguration;
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        log.debug("apply request interceptor apply");
        String path = requestTemplate.path();
        String timestamp = TimeUtils.getCurrentTimestamp();
        String method = requestTemplate.method();
        byte[] content = requestTemplate.body();
        String signature = requestConfiguration.getVersion().isDataInSignature()
                ? SignatureHandler.generate(secretKey, method, timestamp, path, content)
                : SignatureHandler.generate(secretKey, method, timestamp, path);
        log.debug("request sig is: {}", signature);

        requestTemplate.header(requestConfiguration.getAccessKeyHttpHeader(), accessKey);
        requestTemplate.header(requestConfiguration.getTimestampHttpHeader(), timestamp);
        requestTemplate.header(requestConfiguration.getSignatureHttpHeader(), signature);
        requestTemplate.header(requestConfiguration.getVersionHttpHeader(), requestConfiguration.getVersion().getValue());
    }
}
