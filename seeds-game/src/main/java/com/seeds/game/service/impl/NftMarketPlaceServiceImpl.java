package com.seeds.game.service.impl;
import com.seeds.game.dto.request.NftBuySuccessReq;
import com.seeds.game.dto.response.NftMarketPlaceDetailResp;
import com.seeds.game.service.NftMarketPlaceService;
import org.springframework.stereotype.Service;

@Service
public class NftMarketPlaceServiceImpl implements NftMarketPlaceService {

    @Override
    public NftMarketPlaceDetailResp detail(Long id) {
        return null;
    }

    @Override
    public void buySuccess(NftBuySuccessReq req) {

    }
}
