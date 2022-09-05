package com.seeds.uc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.uc.dto.request.NFTMakeOfferReq;
import com.seeds.uc.dto.response.NFTOfferResp;
import com.seeds.uc.model.UcNftOffer;

import java.util.List;


/**
 * <p>
 * NFT的Offer管理表 服务类
 * </p>
 *
 * @author hang.yu
 * @since 2022-09-05
 */
public interface IUcNftOfferService extends IService<UcNftOffer> {

    /**
     * 出价
     * @param req NFT相关入参
     */
    void makeOffer(NFTMakeOfferReq req);

    /**
     * 出价列表
     * @param id NFT的id
     */
    List<NFTOfferResp> offerList(Long id);

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

}
