package com.seeds.game.service;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.game.dto.request.NftBuySuccessReq;
import com.seeds.game.dto.request.*;
import com.seeds.game.dto.response.*;

import java.math.BigDecimal;


public interface NftMarketPlaceService {

    /**
     * 获取商场nft详情
     * @param nftId nft的id
     * @return 商场nft详情
     */
    NftMarketPlaceDetailResp detail(Long nftId);

    /**
     * nft固定价格上架
     * @param req 收据
     */
    void fixedPriceShelf(NftFixedPriceShelfReq req);

    /**
     * nft英式拍卖上架
     * @param req 收据
     */
    void britishAuctionShelf(NftBritishAuctionShelfReq req);

    /**
     * nft下架
     * @param req 收据
     */
    void shelved(NftShelvedReq req);

    /**
     * nft取消拍卖
     * @param req 收据
     */
    void cancelAuction(NftCancelAuctionReq req);

    /**
     * nft拍卖出价
     * @param req 收据
     */
    void makeOffer(NftMakeOfferReq req);

    /**
     * 购买nft成功
     * @param req 收据
     */
    void buySuccess(NftBuySuccessReq req);

    /**
     * 获取nft拍卖出价信息
     * @param req 分页入参
     * @return nft拍卖出价信息
     */
    NftOfferResp offerPage(NftOfferPageReq req);

    /**
     * 获取nft活动信息
     * @param req 分页入参
     * @return nft活动信息
     */
    IPage<NftActivityResp> activityPage(NftActivityPageReq req);

    /**
     * 接受nft出价
     * @param req 收据
     * @return 拍卖信息
     */
    NftOfferDetailResp acceptOffer(NftAcceptOfferReq req);

    /**
     * 拍卖达成交易
     * @param req 收据
     */
    void auctionSuccess(NftSaleSuccessReq req);

    /**
     * 取消nft出价
     * @param req 收据
     */
    void cancelOffer(NftCancelOfferReq req);

    /**
     * 获取nft 皮肤列表
     * @param skinQuery
     * @return 商场nft皮肤列表
     */
    IPage<NftMarketPlaceSkinResp> skinQueryPage(NftMarketPlaceSkinPageReq skinQuery);

    /**
     * 获取nft 装备列表
     * @param equipQuery
     * @return 商场nft装备列表
     */
    IPage<NftMarketPlaceEqiupmentResp> equipQueryPage(NftMarketPlaceEquipPageReq equipQuery);

    /**
     * 浏览量
     * @param req
     * @return NFT浏览量
     */
    void view(NftMarketPlaceDetailViewReq req);

    /**
     * 获取nft 道具列表
     * @param propsQuery
     * @return 商场nft道具列表
     */
    IPage<NftMarketPlacePropsResp> propsQueryPage(NftMarketPlacePropsPageReq propsQuery);

    /**
     * 获取美元汇率
     * @param currency 币种
     * @return 美元汇率
     */
    BigDecimal usdRate(String currency);

    /**
     * 获取交易顺序
     * @return 交易顺序
     */
    String chainNonce();

    /**
     * 托管费退回
     * @param req 订单信息
     */
    void refundFee(NftRefundFeeReq req);

    /**
     * 获取托管费
     * @param price 价格信息
     * @param duration 持续时间
     * @return  托管费
     */
    BigDecimal custodianFee(BigDecimal price, Long duration);

    /**
     * 获取订单收据
     * @param orderId 订单id
     * @return 订单收据
     */
    JSONObject listReceipt(Long orderId);

    /**
     * 托管费全额退回
     * @param req 订单信息
     */
    void refundAllFee(NftRefundAllFeeReq req);

}
