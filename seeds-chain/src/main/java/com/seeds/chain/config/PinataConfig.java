package com.seeds.chain.config;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "ipfs.pinata")
public class PinataConfig {

    private String apiKey;
    private String apiSecret;

    @PostConstruct
    public void init() {
        log.info("ipfs config: {}", this);
    }
}
