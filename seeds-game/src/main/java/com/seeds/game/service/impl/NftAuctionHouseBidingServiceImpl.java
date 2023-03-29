package com.seeds.game.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.game.dto.request.NftOfferPageReq;
import com.seeds.game.dto.response.NftOfferResp;
import com.seeds.game.entity.NftAuctionHouseBiding;
import com.seeds.game.mapper.NftAuctionHouseBidingMapper;
import com.seeds.game.service.INftAuctionHouseBidingService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * nft拍卖出价
 * </p>
 *
 * @author hang.yu
 * @since 2023-03-28
 */
@Service
public class NftAuctionHouseBidingServiceImpl extends ServiceImpl<NftAuctionHouseBidingMapper, NftAuctionHouseBiding> implements INftAuctionHouseBidingService {

    @Override
    public NftOfferResp queryPage(NftOfferPageReq req) {

        return null;
    }
}
