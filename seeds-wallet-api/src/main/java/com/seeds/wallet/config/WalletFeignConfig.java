package com.seeds.wallet.config;

import com.seeds.common.crypto.RequestConfiguration;
import com.seeds.common.interceptor.ClientSignRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * @author rickierao
 * @email antilaw@yahoo.com
 * @date 2021/1/25
 */
public class WalletFeignConfig {
    @Value("${feign.wallet.access.access-key}")
    private String walletAccessKey;
    @Value("${feign.wallet.access.secret-key}")
    private String walletSecretKey;

    @Bean
    public ClientSignRequestInterceptor getRequestInterceptor() {
        return new ClientSignRequestInterceptor(walletAccessKey, walletSecretKey, new RequestConfiguration());
    }
}
