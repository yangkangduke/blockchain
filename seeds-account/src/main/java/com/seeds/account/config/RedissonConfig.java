package com.seeds.account.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 *
 * @author yk
 *
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "redisson")
public class RedissonConfig {

    String address;
    String password;
    int timeout;
    int maxPoolSize;
    int minIdleSize;

    @Bean
    public RedissonClient redisson() throws IOException {
        Config config = new Config();
        SingleServerConfig serverConfig = config.useSingleServer();
        serverConfig.setAddress(address);
        serverConfig.setTimeout(timeout);
        serverConfig.setConnectTimeout(timeout);
        serverConfig.setConnectionPoolSize(maxPoolSize);
        serverConfig.setConnectionMinimumIdleSize(minIdleSize);
        if (password != null && password.length() > 0) {
            serverConfig.setPassword(password);
        }
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }

}
