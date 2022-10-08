package com.seeds.account.chain.feigh;

import feign.Client;
import feign.Contract;
import feign.Feign;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.okhttp.OkHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@Component
@Import(FeignClientsConfiguration.class)
@Slf4j
public class FeignClientFactory {
    private final Encoder encoder;
    private final Decoder decoder;
    private final Contract contract;

    @Value("${etherscan.api.url:https://api.etherscan.io/}")
    private String etherscanUrl;

    public FeignClientFactory(Encoder encoder,
                              Decoder decoder,
                              Contract contract) {
        this.encoder = encoder;
        this.decoder = decoder;
        this.contract = contract;
    }

    protected Client newClient() {
        return new OkHttpClient();
    }

    public ETHScanApiClient etherscanApiClient() {
        return Feign.builder()
                .client(newClient())
                .contract(contract)
                .encoder(encoder)
                .decoder(decoder)
                .target(ETHScanApiClient.class, etherscanUrl);
    }
}
