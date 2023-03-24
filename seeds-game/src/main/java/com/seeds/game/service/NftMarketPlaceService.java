package com.seeds.game.service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.game.dto.request.NftBuySuccessReq;
import com.seeds.common.dto.PageReq;
import com.seeds.game.dto.request.*;
import com.seeds.game.dto.response.*;


public interface NftMarketPlaceService {

    /**
     * 获取商场nft详情
     * @param id nft的id
     * @return 商场nft详情
     */
    NftMarketPlaceDetailResp detail(Long id);

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
    NftOfferResp offerPage(PageReq req);

    /**
     * 获取nft活动信息
     * @param req 分页入参
     * @return nft活动信息
     */
    NftActivityResp activityPage(PageReq req);

    /**
     * 接受nft出价
     * @param req 收据
     */
    void acceptOffer(NftAcceptOfferReq req);

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
     * @return 商场nft皮肤列表
     */
    IPage<NftMarketPlaceEqiupmentResp> equipQueryPage(NftMarketPlaceEquipPageReq equipQuery);

    /**
     * 浏览量
     * @param req
     * @return NFT浏览量
     */
    NftMarketPlaceDetailViewResp view(NftMarketPlaceDetailViewReq req);
}
