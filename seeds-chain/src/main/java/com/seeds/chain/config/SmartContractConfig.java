package com.seeds.chain.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "smart-contract")
public class SmartContractConfig {
    private long chainId;
    private String ownerAddress;
    private String ownerPrivateKey;
    private String rpcProviderUrl;
    private String gameAddress;

    @PostConstruct
    public void init() {
        log.info("smart-contract config: {}", this);
    }

}
