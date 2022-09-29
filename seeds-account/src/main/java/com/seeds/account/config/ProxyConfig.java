package com.seeds.account.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "proxy")
public class ProxyConfig {
    private boolean enable;
    private String proxyHost;
    private int proxyPort;
}
