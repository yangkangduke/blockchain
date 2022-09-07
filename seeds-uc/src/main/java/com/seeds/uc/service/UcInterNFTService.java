package com.seeds.uc.service;

import com.seeds.uc.dto.request.NFTBuyCallbackReq;


/**
 * <p>
 * 内部NFT 服务类
 * </p>
 *
 * @author hang.yu
 * @since 2022-09-05
 */
public interface UcInterNFTService {

    /**
     * 购买回调
     * @param buyReq
     */
    void buyNFTCallback(NFTBuyCallbackReq buyReq);

}
