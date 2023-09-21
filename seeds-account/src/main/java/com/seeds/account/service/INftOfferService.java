package com.seeds.account.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.account.dto.resp.NftOfferResp;
import com.seeds.account.model.NftOffer;

import java.util.List;


/**
 * <p>
 * NFT的Offer管理表 服务类
 * </p>
 *
 * @author hang.yu
 * @since 2022-09-05
 */
public interface INftOfferService extends IService<NftOffer> {

    /**
     * 出价列表
     * @param nftId NFT的id
     */
    List<NftOfferResp> offerList(Long nftId);

    /**
     * 查询过期的offers
     * @return 过期的offers
     */
    List<NftOffer> queryExpiredOffers();

    /**
     * 通过nft的id查询竞价中的offers
     * @param nftId NFT 的id
     * @return 竞价中的offers
     */
    List<NftOffer> queryBiddingByNftId(Long nftId);

}