package com.seeds.account.service;

import com.seeds.account.dto.NftPriceHisDto;
import com.seeds.account.dto.req.NftBuyCallbackReq;
import com.seeds.account.dto.req.NftBuyReq;
import com.seeds.account.dto.req.NftPriceHisReq;
import com.seeds.admin.dto.response.SysNftDetailResp;
import com.seeds.common.enums.TargetSource;


/**
 * 账户交易服务
 *
 * @author hang.yu
 * @since 2022-11-28
 */
public interface AccountTradeService {

    /**
     * 校验和发起购买NFT
     * @param req 入参
     */
    void validateAndInitBuyNft(NftBuyReq req);

    /**
     * 购买NFT和记录信息
     * @param nftDetail nft信息
     * @param source 购买用户端
     * @param currentUserId 登录用户id
     */
    void buyNftAndRecord(SysNftDetailResp nftDetail, TargetSource source, Long currentUserId);

    /**
     * 购买NFT回调
     * @param buyReq 入参
     */
    void buyNftCallback(NftBuyCallbackReq buyReq);

    /**
     * NFT历史价格
     * @param req 入参
     * @return result
     */
    NftPriceHisDto nftPriceHis(NftPriceHisReq req);

}
