package com.seeds.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.game.dto.response.NftMarketPlaceDetailResp;
import com.seeds.game.entity.NftMarketPlaceEntity;


public interface INftMarketPlaceService extends IService<NftMarketPlaceEntity> {

    /**
     * 获取商场nft详情
     * @param id nft的id
     * @return 商场nft详情
     */
    NftMarketPlaceDetailResp detail(Long id);

}
