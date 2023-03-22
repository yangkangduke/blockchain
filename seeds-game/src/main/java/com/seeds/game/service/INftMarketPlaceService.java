package com.seeds.game.service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.game.dto.request.NftMarketPlaceEquipPageReq;
import com.seeds.game.dto.request.NftMarketPlaceSkinPageReq;
import com.seeds.game.dto.response.NftMarketPlaceEqiupmentResp;
import com.seeds.game.dto.response.NftMarketPlaceSkinResp;
import com.seeds.game.entity.NftMarketPlaceEntity;

public interface INftMarketPlaceService extends IService<NftMarketPlaceEntity> {


    IPage<NftMarketPlaceSkinResp> skinQueryPage(NftMarketPlaceSkinPageReq skinQuery);

    IPage<NftMarketPlaceEqiupmentResp> equipQueryPage(NftMarketPlaceEquipPageReq equipQuery);
}
