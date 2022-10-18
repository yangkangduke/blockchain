package com.seeds.uc.service;

import com.seeds.uc.dto.request.*;
import com.seeds.uc.dto.response.NFTAuctionResp;
import com.seeds.uc.dto.response.NFTOfferResp;

import java.util.List;


/**
 * <p>
 * 内部NFT 服务类
 * </p>
 *
 * @author hang.yu
 * @since 2022-09-05
 */
public interface UcInterNFTService {

    /**
     * 购买回调
     * @param buyReq
     */
    void buyNFTCallback(NFTBuyCallbackReq buyReq);

    /**
     * 购买
     * @param req 入参
     */
    void buyNFT(NFTBuyReq req);

    /**
     * 正向拍卖
     * @param req 入参
     */
    void forwardAuction(NFTForwardAuctionReq req);

    /**
     * 反向拍卖
     * @param req 入参
     */
    void reverseAuction(NFTReverseAuctionReq req);

    /**
     * 正向出价
     * @param req 入参
     */
    void forwardBids(NFTMakeOfferReq req);

    /**
     * 反向出价
     * @param req 入参
     */
    void reverseBids(NFTBuyReq req);

    /**
     * 出价列表
     * @param id 入参
     */
    List<NFTOfferResp> offerList(Long id);

    /**
     * NFT拍卖信息
     * @param id NFT的id
     * @param userId 拥有该NFT的用户id
     * @return NFT拍卖信息
     */
    NFTAuctionResp actionInfo(Long id, Long userId);

    /**
     * 上架
     * @param req 入参
     */
    void shelves(NFTShelvesReq req);

    /**
     * 下架
     * @param req 入参
     */
    void soldOut(NFTSoldOutReq req);
}
