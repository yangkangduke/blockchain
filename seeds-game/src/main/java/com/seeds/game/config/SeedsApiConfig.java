package com.seeds.game.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author hang.yu
 * @date 2023/3/28
 */
@Data
@Configuration
public class SeedsApiConfig {

    @Value("${seeds.api.baseDomain:https://seed-api.llyc.fun}")
    private String baseDomain;

    @Value("${seeds.api.placeOrder:/api/chainOp/placeOrder}")
    private String placeOrderApi;

    @Value("${redisson.max.englishOrder:/api/auction/english}")
    private String englishOrderApi;

    @Value("${redisson.max.cancelOrder:/api/chainOp/cancelOrder}")
    private String cancelOrderApi;

}
