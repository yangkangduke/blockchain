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

    @Value("${seeds.api.admin.skin.address:skifVG2pkEzbuu4AYeQZx4aKAtE4J1bjEn4UYYxMNiA}")
    private String adminSkinAddress;

    @Value("${seeds.api.solToUsd:https://quote-api.jup.ag/v5/quote}")
    private String solToUsdApi;

    @Value("${seeds.api.solToken:Es9vMFrzaCERmJfrF4H2FYD4KCoNkY11McCe8BenwNYB}")
    private String solToken;

    @Value("${seeds.api.solTokenAddress:So11111111111111111111111111111111111111112}")
    private String solTokenAddress;

    @Value("${seeds.api.baseDomain:http://192.168.6.100:8002}")
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

    @Value("${seeds.api.auctionCancel:/api/auction/cancelBid}")
    private String auctionCancelBid;

    @Value("${seeds.api.endAuction:/api/auction/endAuction}")
    private String endAuction;

    @Value("${seeds.api.saleSuccess:/api/auction/saleSuccess}")
    private String saleSuccess;

    @Value("${seeds.api.auctionCancel:/api/auction/cancelAuction}")
    private String auctionCancel;

    @Value("${seeds.api.withdrawNft:/api/equipment/withdrawNft}")
    private String withdrawNft;

    @Value("${seeds.api.depositNft:/api/equipment/depositNft}")
    private String depositNft;

    @Value("${seeds.api.mintSuccess:/api/equipment/mintSuccess}")
    private String mintSuccess;

    @Value("${seeds.api.mintSuccess:/api/equipment/mintEquip}")
    private String mintEquip;

    @Value("${seeds.api.compose:/api/equipment/compose}")
    private String compose;

    @Value("${seeds.api.compose:/api/chainOp/nonce}")
    private String nonce;

    @Value("${seeds.api.refundFee:/api/admin/refundFee}")
    private String refundFee;

    @Value("${seeds.api.listReceipt:/api/market/getListReceipt}")
    private String listReceipt;

    // 皮肤withdraw
    @Value("${seeds.api.listReceipt:/api/admin/withdrawNft}")
    private String skinWithdraw;

    @Value("${seeds.api.tokenAddress:/api/address/tokenAddress}")
    private String tokenAddress;

}
