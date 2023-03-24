package com.seeds.game.service.impl;
import com.seeds.common.dto.PageReq;
import com.seeds.game.dto.request.*;
import com.seeds.game.dto.response.NftActivityResp;
import com.seeds.game.dto.response.NftMarketPlaceDetailResp;
import com.seeds.game.dto.response.NftOfferResp;
import com.seeds.game.service.NftMarketPlaceService;
import org.springframework.stereotype.Service;

@Service
public class NftMarketPlaceServiceImpl implements NftMarketPlaceService {

    @Override
    public NftMarketPlaceDetailResp detail(Long id) {
        return null;
    }

    @Override
    public void fixedPriceShelf(NftFixedPriceShelfReq req) {

    }

    @Override
    public void britishAuctionShelf(NftBritishAuctionShelfReq req) {

    }

    @Override
    public void shelved(NftShelvedReq req) {

    }

    @Override
    public void makeOffer(NftMakeOfferReq req) {

    }

    @Override
    public void buySuccess(NftBuySuccessReq req) {

    }

    @Override
    public NftOfferResp offerPage(PageReq req) {
        return null;
    }

    @Override
    public NftActivityResp activityPage(PageReq req) {
        return null;
    }

    @Override
    public void acceptOffer(NftAcceptOfferReq req) {

    }

    @Override
    public void cancelOffer(NftCancelOfferReq req) {

    }

}
