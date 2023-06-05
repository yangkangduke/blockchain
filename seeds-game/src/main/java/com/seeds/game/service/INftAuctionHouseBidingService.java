package com.seeds.game.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.game.dto.request.NftMyOfferPageReq;
import com.seeds.game.dto.request.NftOfferPageReq;
import com.seeds.game.dto.response.NftOfferResp;
import com.seeds.game.entity.NftAuctionHouseBiding;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * nft拍卖出价
 * </p>
 *
 * @author hang.yu
 * @since 2023-03-28
 */
public interface INftAuctionHouseBidingService extends IService<NftAuctionHouseBiding> {

    /**
     * 分页获取NFT拍卖出价信息
     * @param req 分页查询条件
     * @return NFT拍卖出价信息
     */
    IPage<NftOfferResp.NftOffer> queryPage(NftOfferPageReq req);

    /**
     * 分页获取我的NFT拍卖出价信息
     * @param req 分页查询条件
     * @return NFT拍卖出价信息
     */
    IPage<NftAuctionHouseBiding> queryMyPage(NftMyOfferPageReq req);

    /**
     * 获取NFT拍卖当前出价
     * @param auctionId 拍卖id
     * @return NFT拍卖当前出价
     */
    BigDecimal queryAuctionCurrentPrice(Long auctionId);

    /**
     * 根据地址和价格查询拍卖
     * @param publicAddress 用户地址
     * @param mintAddress NFT地址
     * @return 拍卖列表
     */
    List<NftAuctionHouseBiding> queryByAddressAndMintAddress(String publicAddress, String mintAddress);

}
