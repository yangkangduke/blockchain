package com.seeds.uc.service;

import com.seeds.uc.dto.request.*;


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
     * 出价
     * @param req 入参
     */
    void bids(NFTMakeOfferReq req);

}
