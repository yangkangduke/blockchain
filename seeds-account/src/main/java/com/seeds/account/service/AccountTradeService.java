package com.seeds.account.service;

import com.seeds.account.dto.NftGasFeesDto;
import com.seeds.account.dto.NftPriceHisDto;
import com.seeds.account.dto.req.*;
import com.seeds.account.dto.resp.NftOfferResp;
import com.seeds.admin.dto.response.SysNftDetailResp;
import com.seeds.common.enums.TargetSource;
import com.seeds.account.dto.resp.NftAuctionResp;

import java.util.List;


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
     * 出价
     * @param req NFT相关入参
     * @param sysNftDetail NFT详情
     */
    void nftMakeOffer(NftMakeOfferReq req, SysNftDetailResp sysNftDetail);

    /**
     * NFT竞价拒绝
     * @param id NFT offer的id
     */
    void nftOfferReject(Long id);

    /**
     * NFT竞价接受
     * @param id NFT offer的id
     */
    void nftOfferAccept(Long id);

    /**
     * NFT历史价格
     * @param req 入参
     * @return result
     */
    NftPriceHisDto nftPriceHis(NftPriceHisReq req);

    /**
     * 正向拍卖
     * @param req 入参
     */
    void forwardAuction(NftForwardAuctionReq req);

    /**
     * 反向拍卖
     * @param req 入参
     */
    void reverseAuction(NftReverseAuctionReq req);

    /**
     * 正向出价
     * @param req 入参
     */
    void forwardBids(NftMakeOfferReq req);

    /**
     * 反向出价
     * @param req 入参
     */
    void reverseBids(NftBuyReq req);


    /**
     * 出价列表
     * @param id 入参
     */
    List<NftOfferResp> offerList(Long id);

    /**
     * NFT拍卖信息
     * @param id NFT的id
     * @param userId 拥有该NFT的用户id
     * @return NFT拍卖信息
     */
    NftAuctionResp actionInfo(Long id, Long userId);

    /**
     * 手续费扣除
     * @param req 入参
     */
    void nftDeductGasFee(AccountOperateReq req);

    /**
     * 解冻金额
     * @param req 入参
     */
    void amountUnfreeze(AccountOperateReq req);

    /**
     * 余额变更
     * @param req 入参
     */
    void amountChangeBalance(AccountOperateReq req);

    /**
     * NFT的offer过期任务
     */
    void nftOfferExpired();

    /**
     * NFT的手续费
     * @return 手续费
     */
    List<NftGasFeesDto> nftGasFee();

}
