package com.seeds.game.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.game.dto.request.NftBuySuccessReq;
import com.seeds.game.dto.request.NftMarketPlaceDetailViewReq;
import com.seeds.game.dto.request.NftMarketPlaceEquipPageReq;
import com.seeds.game.dto.request.NftMarketPlaceSkinPageReq;
import com.seeds.game.dto.response.NftMarketPlaceDetailResp;
import com.seeds.game.dto.response.NftMarketPlaceDetailViewResp;
import com.seeds.game.dto.response.NftMarketPlaceEqiupmentResp;
import com.seeds.game.dto.response.NftMarketPlaceSkinResp;


public interface NftMarketPlaceService {

    /**
     * 获取商场nft详情
     * @param id nft的id
     * @return 商场nft详情
     */
    NftMarketPlaceDetailResp detail(Long id);

    /**
     * 购买nft成功
     * @param req 收据
     */
    void buySuccess(NftBuySuccessReq req);

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
