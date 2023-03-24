package com.seeds.game.service;

import com.seeds.game.dto.request.NftBuySuccessReq;
import com.seeds.game.dto.response.NftMarketPlaceDetailResp;


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

}
