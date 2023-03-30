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

    @Value("${seeds.api.englishOrder:/api/auction/english}")
    private String englishOrderApi;

    @Value("${seeds.api.cancelOrder:/api/chainOp/cancelOrder}")
    private String cancelOrderApi;

    @Value("${seeds.api.auctionBid:/api/auction/bid}")
    private String auctionBid;

    @Value("${seeds.api.buySuccess:/api/chainOp/buySuccess}")
    private String buySuccess;

    @Value("${seeds.api.endAuction:/api/auction/endAuction}")
    private String endAuction;

    @Value("${seeds.api.auctionCancel:/api/auction/cancel}")
    private String auctionCancel;

    @Value("${seeds.api.withdrawNft:/api/equipment/withdrawNft}")
    private String withdrawNft;

    @Value("${seeds.api.depositNft:/api/equipment/depositNft}")
    private String depositNft;

}
