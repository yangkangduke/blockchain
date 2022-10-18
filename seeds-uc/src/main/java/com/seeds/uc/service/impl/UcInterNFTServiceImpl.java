package com.seeds.uc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seeds.admin.dto.request.SysNftShelvesReq;
import com.seeds.admin.dto.request.SysNftSoldOutReq;
import com.seeds.admin.dto.response.SysNftDetailResp;
import com.seeds.admin.enums.AdminErrorCodeEnum;
import com.seeds.admin.feign.RemoteNftService;
import com.seeds.uc.dto.request.*;
import com.seeds.uc.dto.response.NFTAuctionResp;
import com.seeds.uc.dto.response.NFTOfferResp;
import com.seeds.uc.enums.*;
import com.seeds.uc.exceptions.GenericException;
import com.seeds.uc.model.*;
import com.seeds.uc.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;


/**
 * <p>
 * 内部NFT 服务类
 * </p>
 *
 * @author hang.yu
 * @since 2022-09-05
 */
@Service
@Slf4j
@Transactional
public class UcInterNFTServiceImpl implements UcInterNFTService {

    @Autowired
    private IUcUserAccountActionHistoryService ucUserAccountActionHistoryService;
    @Autowired
    private IUcNftForwardAuctionService ucNftForwardAuctionService;
    @Autowired
    private IUcNftReverseAuctionService ucNftReverseAuctionService;
    @Autowired
    private IUcUserAccountService ucUserAccountService;
    @Autowired
    private IUcNftOfferService ucNftOfferService;
    @Autowired
    private RemoteNftService remoteNftService;

    /**
     * 购买回调
     * @param buyReq
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void buyNFTCallback(NFTBuyCallbackReq buyReq) {
        BigDecimal amount = buyReq.getAmount();
        // 成功
        if (AccountActionStatusEnum.SUCCESS == buyReq.getActionStatusEnum()) {
            // 如果是admin端mint的nft，在uc端不用记账该账户到uc_user_account，都是uc端的用户则需要记账，卖家为UC端用户
            if (null != buyReq.getOwnerType() && buyReq.getOwnerType() == 1) {
                //  卖家balance增加
                LambdaQueryWrapper<UcUserAccount> wrapper = new LambdaQueryWrapper<UcUserAccount>()
                        .eq(UcUserAccount::getUserId, buyReq.getFromUserId())
                        .eq(UcUserAccount::getCurrency, CurrencyEnum.USDT);
                UcUserAccount sellerAccount = ucUserAccountService.getOne(wrapper);
                sellerAccount.setBalance(sellerAccount.getBalance().add(amount));
                ucUserAccountService.updateById(sellerAccount);
            }

            // 买家freeze减少
            LambdaQueryWrapper<UcUserAccount> wrapper = new LambdaQueryWrapper<UcUserAccount>()
                    .eq(UcUserAccount::getUserId, buyReq.getToUserId())
                    .eq(UcUserAccount::getCurrency, CurrencyEnum.USDT);
            UcUserAccount buyerAccount = ucUserAccountService.getOne(wrapper);
            buyerAccount.setFreeze(buyerAccount.getFreeze().subtract(amount));
            ucUserAccountService.updateById(buyerAccount);
        } else {
            // 失败
            // 买家解冻金额，增加余额
            LambdaQueryWrapper<UcUserAccount> wrapper = new LambdaQueryWrapper<UcUserAccount>()
                    .eq(UcUserAccount::getUserId, buyReq.getToUserId())
                    .eq(UcUserAccount::getCurrency, CurrencyEnum.USDT);
            UcUserAccount buyerAccount = ucUserAccountService.getOne(wrapper);
            buyerAccount.setBalance(buyerAccount.getBalance().add(amount));
            buyerAccount.setFreeze(buyerAccount.getFreeze().subtract(amount));
            ucUserAccountService.updateById(buyerAccount);
        }

        // 改变交易记录的状态及其他信息
        ucUserAccountActionHistoryService.updateById(UcUserAccountActionHistory.builder()
                .status(buyReq.getActionStatusEnum())
                        .fromAddress(buyReq.getFromAddress())
                        .toAddress(buyReq.getToAddress())
                        .amount(buyReq.getAmount())
                        .chain(buyReq.getChain())
                        .txHash(buyReq.getTxHash())
                        .blockNumber(buyReq.getBlockNumber())
                        .blockHash(buyReq.getBlockHash())
                        .id(buyReq.getActionHistoryId())
                .build());

        // 改变offer状态
        if (buyReq.getOfferId() != null) {
            ucNftOfferService.updateById(UcNftOffer.builder()
                    .status(buyReq.getOfferStatusEnum())
                    .id(buyReq.getOfferId())
                    .build());

            // offer接受成功，拒绝其他相关的offer
            if (NFTOfferStatusEnum.ACCEPTED == buyReq.getOfferStatusEnum()) {
                List<UcNftOffer> nftOffers = ucNftOfferService.queryBiddingByNftId(buyReq.getNftId());
                if (!CollectionUtils.isEmpty(nftOffers)) {
                    nftOffers.forEach(p -> {
                        // 排除即将接受的offer
                        if (!p.getId().equals(buyReq.getOfferId())) {
                            // 拒绝其他相关的offer
                            ucNftOfferService.offerReject(p.getId());
                        }
                    });
                }
            }
        }

    }

    @Override
    public void buyNFT(NFTBuyReq req, SysNftDetailResp sysNftDetailResp) {
        ucUserAccountService.buyNFT(req, sysNftDetailResp);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void forwardAuction(NFTForwardAuctionReq req) {
        // 判断是否已存在正向拍卖
        UcNftForwardAuction forwardAuction = ucNftForwardAuctionService.queryByUserIdAndNftId(req.getUserId(), req.getNftId());
        if (forwardAuction != null) {
            throw new GenericException(AdminErrorCodeEnum.ERR_40013_THIS_NFT_AUCTION_IS_IN_PROGRESS.getDescEn());
        }
        // 上架NFT
        SysNftShelvesReq shelvesReq = new SysNftShelvesReq();
        shelvesReq.setUserId(req.getUserId());
        shelvesReq.setNftId(req.getNftId());
        shelvesReq.setPrice(req.getPrice());
        shelvesReq.setUnit(req.getCurrency().getCode());
        if (!remoteNftService.shelves(shelvesReq).isSuccess()) {
            throw new GenericException(UcErrorCodeEnum.ERR_500_SYSTEM_BUSY.getDescEn());
        }
        // 保存正向拍卖记录
        forwardAuction = UcNftForwardAuction.builder().build();
        BeanUtil.copyProperties(req, forwardAuction);
        ucNftForwardAuctionService.save(forwardAuction);
    }

    @Override
    public void reverseAuction(NFTReverseAuctionReq req) {
        // 判断是否已存在反向拍卖
        UcNftReverseAuction reverseAuction = ucNftReverseAuctionService.queryByUserIdAndNftId(req.getUserId(), req.getNftId());
        if (reverseAuction != null) {
            throw new GenericException(AdminErrorCodeEnum.ERR_40013_THIS_NFT_AUCTION_IS_IN_PROGRESS.getDescEn());
        }
        // 上架NFT
        SysNftShelvesReq shelvesReq = new SysNftShelvesReq();
        shelvesReq.setNftId(req.getNftId());
        shelvesReq.setUserId(req.getUserId());
        shelvesReq.setPrice(req.getPrice());
        shelvesReq.setUnit(req.getCurrency().getCode());
        if (!remoteNftService.shelves(shelvesReq).isSuccess()) {
            throw new GenericException(UcErrorCodeEnum.ERR_500_SYSTEM_BUSY.getDescEn());
        }
        // 保存反向拍卖记录
        reverseAuction = UcNftReverseAuction.builder().build();
        BeanUtil.copyProperties(req, reverseAuction);
        ucNftReverseAuctionService.save(reverseAuction);
    }

    @Override
    public void forwardBids(NFTMakeOfferReq req) {
        SysNftDetailResp sysNftDetailResp;
        try {
            sysNftDetailResp = remoteNftService.ucDetail(req.getNftId()).getData();
        } catch (Exception e) {
            throw new GenericException(UcErrorCodeEnum.ERR_18009_FAILED_TO_BID_NFT);
        }
        // 判断是否已存在正向拍卖
        UcNftReverseAuction reverseAuction = ucNftReverseAuctionService.queryByUserIdAndNftId(sysNftDetailResp.getOwnerId(), req.getNftId());
        if (reverseAuction == null) {
            throw new GenericException(AdminErrorCodeEnum.ERR_40015_THIS_NFT_IS_NOT_IN_THE_AUCTION.getDescEn());
        }
        ucNftOfferService.makeOffer(req, sysNftDetailResp);
    }

    @Override
    public void reverseBids(NFTBuyReq req) {
        SysNftDetailResp sysNftDetailResp;
        try {
            sysNftDetailResp = remoteNftService.ucDetail(req.getNftId()).getData();
        } catch (Exception e) {
            throw new GenericException(UcErrorCodeEnum.ERR_18005_ACCOUNT_BUY_FAIL);
        }
        // 判断是否已存在反向拍卖
        UcNftReverseAuction reverseAuction = ucNftReverseAuctionService.queryByUserIdAndNftId(sysNftDetailResp.getOwnerId(), req.getNftId());
        if (reverseAuction == null) {
            throw new GenericException(AdminErrorCodeEnum.ERR_40015_THIS_NFT_IS_NOT_IN_THE_AUCTION.getDescEn());
        }
        buyNFT(req, sysNftDetailResp);
    }

    @Override
    public List<NFTOfferResp> offerList(Long id) {
        return ucNftOfferService.offerList(id);
    }

    @Override
    public NFTAuctionResp actionInfo(Long id, Long userId) {
        NFTAuctionResp resp = new NFTAuctionResp();
        UcNftForwardAuction forwardAuction = ucNftForwardAuctionService.queryByUserIdAndNftId(userId, id);
        UcNftReverseAuction reverseAuction = ucNftReverseAuctionService.queryByUserIdAndNftId(userId, id);
        if (forwardAuction != null && reverseAuction != null) {
            resp.setAuctionFlag(NFTAuctionStatusEnum.BOTH.getCode());
        } else if (forwardAuction != null) {
            resp.setAuctionFlag(NFTAuctionStatusEnum.FORWARD.getCode());
        } else if (reverseAuction != null) {
            resp.setAuctionFlag(NFTAuctionStatusEnum.REVERSE.getCode());
        } else {
            resp.setAuctionFlag(NFTAuctionStatusEnum.NONE.getCode());
        }
        return resp;
    }
    @Override
    public void shelves(NFTShelvesReq req) {
        // NFT上架
        SysNftShelvesReq shelvesReq = new SysNftShelvesReq();
        BeanUtils.copyProperties(req, shelvesReq);
        remoteNftService.shelves(shelvesReq);
    }

    @Override
    public void soldOut(NFTSoldOutReq req) {
        // NFT下架
        SysNftSoldOutReq soldOutReq = new SysNftSoldOutReq();
        BeanUtils.copyProperties(req, soldOutReq);
        remoteNftService.soldOut(soldOutReq);
    }
}
