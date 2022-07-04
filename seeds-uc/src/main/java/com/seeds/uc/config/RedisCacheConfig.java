package com.seeds.uc.config;

import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/7/30
 */
@Configuration
public class RedisCacheConfig {

    @Value("${redisson.address:redis://127.0.0.1:6379}")
    String address;

    @Value("${redisson.timeout:5000}")
    int timeout;

    @Value("${redisson.max.poolsize:50}")
    int maxPoolSize;

    @Value("${redisson.min.idlesize:50}")
    int minIdleSize;

    @Value("${redisson.password:}")
    String password;

    @Value("${redisson.database:0}")
    int database;

    @Bean("defaultRedisClient")
    public RedissonClient defaultClient() {
        Config config = new Config();
        SingleServerConfig serverConfig = config.useSingleServer();
        serverConfig.setAddress(address);
        serverConfig.setTimeout(timeout);
        serverConfig.setConnectTimeout(timeout);
        serverConfig.setConnectionPoolSize(maxPoolSize);
        serverConfig.setConnectionMinimumIdleSize(minIdleSize);
        serverConfig.setDatabase(database);

        if (!StringUtils.isBlank(password)) {
            serverConfig.setPassword(password);
        }
        return Redisson.create(config);
    }
}
