package com.seeds.account.feign;

import com.seeds.common.crypto.RequestConfiguration;
import com.seeds.common.crypto.Version;
import com.seeds.common.interceptor.ClientSignRequestInterceptor;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

/**
 * @author milo
 *
 * copied from @Ricky
 *
 */
public class AccountFeignConfig {

    @Value("${feign.account.access.access-key}")
    private String accessKey;

    @Value("${feign.account.access.secret-key}")
    private String secretKey;

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    @Bean
    @Primary
    @Scope("prototype")
    public Encoder multipartFormEncoder() {
        return new SpringFormEncoder(new SpringEncoder(messageConverters));
    }

    @Bean
    public ClientSignRequestInterceptor getRequestInterceptor() {
        return new ClientSignRequestInterceptor(accessKey, secretKey, new RequestConfiguration(Version.V2));
    }
}
