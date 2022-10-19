package com.seeds.account.config;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CuratorConfig {

    @Bean
    public CuratorFramework curatorFramework(ZookeeperConfig zookeeperConfig) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(zookeeperConfig.getBaseSleepTimeMs(),
                zookeeperConfig.getMaxRetries());

        CuratorFramework curatorFramework =
                CuratorFrameworkFactory.builder()
                        .connectString(zookeeperConfig.getConnectString())
                        .sessionTimeoutMs(zookeeperConfig.getSessionTimeoutMs())
                        .connectionTimeoutMs(zookeeperConfig.getConnectionTimeoutMs())
                        .retryPolicy(retryPolicy)
                        .build();

        curatorFramework.start();
        return curatorFramework;
    }
}
