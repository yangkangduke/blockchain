package com.seeds.game.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.game.entity.NftMarketOrderEntity;

public interface INftMarketOrderService extends IService<NftMarketOrderEntity> {

    /**
     * 通过tokenid获取对象
     */
    NftMarketOrderEntity  detailForTokenId(Long tokenId);
}
