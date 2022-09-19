package com.seeds.wallet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.abi.DefaultFunctionEncoder;

/**
 * @author rickierao
 * @email antilaw@yahoo.com
 * @date 2021/1/8
 */
@Configuration
public class OnChainConfig {
    @Bean
    DefaultFunctionEncoder getDefaultFunctionEncoder() {
        return new DefaultFunctionEncoder();
    }
}
