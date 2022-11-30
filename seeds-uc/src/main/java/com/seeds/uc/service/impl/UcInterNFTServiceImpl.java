package com.seeds.uc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.seeds.account.model.NftForwardAuction;
import com.seeds.account.model.NftReverseAuction;
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
import com.seeds.uc.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private RemoteNftService remoteNftService;

    @Override
    public void buyNFT(NFTBuyReq req, SysNftDetailResp sysNftDetailResp) {
//        ucUserAccountService.buyNFT(req, sysNftDetailResp);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void forwardAuction(NFTForwardAuctionReq req) {
        // 判断是否已存在正向拍卖
        NftForwardAuction forwardAuction = ucNftForwardAuctionService.queryByUserIdAndNftId(req.getUserId(), req.getNftId());
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
        forwardAuction = NftForwardAuction.builder().build();
        BeanUtil.copyProperties(req, forwardAuction);
        ucNftForwardAuctionService.save(forwardAuction);
    }

    @Override
    public void reverseAuction(NFTReverseAuctionReq req) {
        // 判断是否已存在反向拍卖
        NftReverseAuction reverseAuction = ucNftReverseAuctionService.queryByUserIdAndNftId(req.getUserId(), req.getNftId());
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
        reverseAuction = NftReverseAuction.builder().build();
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
        NftReverseAuction reverseAuction = ucNftReverseAuctionService.queryByUserIdAndNftId(sysNftDetailResp.getOwnerId(), req.getNftId());
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
        NftReverseAuction reverseAuction = ucNftReverseAuctionService.queryByUserIdAndNftId(sysNftDetailResp.getOwnerId(), req.getNftId());
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
        NftForwardAuction forwardAuction = ucNftForwardAuctionService.queryByUserIdAndNftId(userId, id);
        NftReverseAuction reverseAuction = ucNftReverseAuctionService.queryByUserIdAndNftId(userId, id);
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

    @Override
    public void deductGasFee(NFTDeductGasFeeReq req) {
//        ucUserAccountService.deductGasFee(req);
    }
}
