package com.seeds.account.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "account.zookeeper")
public class ZookeeperConfig {
    private int baseSleepTimeMs;
    private int maxRetries;
    private String connectString;
    private int sessionTimeoutMs;
    private int connectionTimeoutMs;
}
