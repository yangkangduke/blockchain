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
public class PinataConfig {

    @Value("${ipfs.pinata.apiKey:}")
    private String apiKey;
    @Value("${ipfs.pinata.secret:}")
    private String apiSecret;

    @PostConstruct
    public void init() {
        log.info("ipfs config: {}", this);
    }
}
