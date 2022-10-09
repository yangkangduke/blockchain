//package com.seeds.account.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang.StringUtils;
//import org.redisson.Redisson;
//import org.redisson.api.RedissonClient;
//import org.redisson.config.Config;
//import org.redisson.config.SingleServerConfig;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @author raowentong
// * @email antilaw@yahoo.com
// * @date 2020/7/30
// */
//@Configuration
//@Slf4j
//public class RedisCacheConfig {
//
//    @Value("${redisson.address:redis://127.0.0.1:6379}")
//    String address;
//
//    @Value("${redisson.timeout:5000}")
//    int timeout;
//
//    @Value("${redisson.max.poolsize:50}")
//    int maxPoolSize;
//
//    @Value("${redisson.min.idlesize:50}")
//    int minIdleSize;
//
//    @Value("${redisson.password:}")
//    String password;
//
//    @Value("${redisson.database:0}")
//    int database;
//
//    @Value("${redis.password}")
//    String redisPassword;
//
//    @Value("${spring.datasource.hikari.password}")
//    String dbPassword;
//
//    @Bean("defaultRedisClient")
//    public RedissonClient defaultClient() {
//
//        Config config = new Config();
//        SingleServerConfig serverConfig = config.useSingleServer();
//        serverConfig.setAddress(address);
//        serverConfig.setTimeout(timeout);
//        serverConfig.setConnectTimeout(timeout);
//        serverConfig.setConnectionPoolSize(maxPoolSize);
//        serverConfig.setConnectionMinimumIdleSize(minIdleSize);
//        serverConfig.setDatabase(database);
//
//        if (!StringUtils.isBlank(password)) {
//            serverConfig.setPassword(password);
//        }
//
//        return Redisson.create(config);
//    }
//
//}
