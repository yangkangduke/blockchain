package com.seeds.account.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.admin.dto.response.SysNftDetailResp;
import com.seeds.uc.dto.request.NFTMakeOfferReq;
import com.seeds.uc.dto.response.NFTOfferResp;
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
     * 出价
     * @param req NFT相关入参
     * @param sysNftDetail NFT详情
     */
    void makeOffer(NFTMakeOfferReq req, SysNftDetailResp sysNftDetail);

    /**
     * 出价列表
     * @param nftId NFT的id
     */
    List<NFTOfferResp> offerList(Long nftId);

    /**
     * NFT竞价拒绝
     * @param id NFT offer的id
     */
    void offerReject(Long id);

    /**
     * NFT竞价接受
     * @param id NFT offer的id
     */
    void offerAccept(Long id);

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
