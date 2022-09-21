package com.seeds.wallet.config;

import com.seeds.common.crypto.RequestConfiguration;
import com.seeds.common.filter.ReplaceStreamFilter;
import com.seeds.common.interceptor.ServerApiAuthInterceptor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author rickierao
 * @email antilaw@yahoo.com
 * @date 2021/1/25
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "wallet.access")
public class ServerApiAuthConfig {
    private List<KeyPair> keyList;

    private long allowedTimestampRange;

    @Bean
    public ServerApiAuthInterceptor getAuthInterceptor() {
        Map<String, String> keyMapping =
                Optional.ofNullable(keyList).orElse(Collections.emptyList())
                        .stream().collect(Collectors.toMap(KeyPair::getKey, KeyPair::getSecret, (v1, v2) -> v2));
        return new ServerApiAuthInterceptor(keyMapping, new RequestConfiguration(), allowedTimestampRange);
    }

    @Bean
    public FilterRegistrationBean<Filter> replaceStreamFilterRegistration() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(replaceStreamFilter());
        registration.addUrlPatterns("/*");
        registration.setName("streamFilter");
        return registration;
    }

    @Bean(name = "replaceStreamFilter")
    public Filter replaceStreamFilter() {
        return new ReplaceStreamFilter();
    }

    @Data
    private static class KeyPair {
        private String key;
        private String secret;
    }
}
