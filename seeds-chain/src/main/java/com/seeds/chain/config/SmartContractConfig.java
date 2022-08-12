package com.seeds.chain.config;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Slf4j
@Data
@ToString
@Configuration
public class SmartContractConfig {
    @Value("${smart-contract.chain-id:97}")
    long chainId;
    @Value("${smart-contract.owner.address:0x00}")
    String ownerAddress;
    @Value("${smart-contract.owner.private-key:0x00}")
    String ownerPrivateKey;
    @Value("${smart-contract.rpc-provider.url:http://localhost}")
    String rpcProviderUrl;
    @Value("${smart-contract.game-address:0x00}")
    String gameItemsContractAddress;

    @PostConstruct
    public void init() {
        log.info("smart-contract config: {}", this);
    }

}
