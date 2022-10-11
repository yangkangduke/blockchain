package com.seeds.uc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seeds.admin.dto.request.UcSwitchReq;
import com.seeds.admin.dto.request.UpOrDownReq;
import com.seeds.admin.enums.AdminErrorCodeEnum;
import com.seeds.admin.enums.WhetherEnum;
import com.seeds.admin.feign.RemoteNftService;
import com.seeds.uc.dto.request.NFTBuyCallbackReq;
import com.seeds.uc.dto.request.NFTBuyReq;
import com.seeds.uc.dto.request.NFTForwardAuctionReq;
import com.seeds.uc.dto.request.NFTMakeOfferReq;
import com.seeds.uc.enums.*;
import com.seeds.uc.exceptions.GenericException;
import com.seeds.uc.model.*;
import com.seeds.uc.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
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
    private IUcUserAccountService ucUserAccountService;
    @Autowired
    private IUcNftOfferService ucNftOffersService;
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
        // 如果是admin端mint的nft，在uc端不用记账该账户到uc_user_account，都是uc端的用户则需要记账
        if (null != buyReq.getOwnerType() && buyReq.getOwnerType() == 1) { // 卖家为UC端用户
            //  卖家balance增加
            LambdaQueryWrapper<UcUserAccount> wrapper = new LambdaQueryWrapper<UcUserAccount>()
                    .eq(UcUserAccount::getUserId, buyReq.getFromUserId())
                    .eq(UcUserAccount::getCurrency, CurrencyEnum.USDC);
            UcUserAccount account = ucUserAccountService.getOne(wrapper);

            ucUserAccountService.update(UcUserAccount.builder()
                    .balance(account.getBalance().add(amount))
                    .build(), new LambdaQueryWrapper<UcUserAccount>()
                    .eq(UcUserAccount::getUserId, buyReq.getFromUserId())
                    .eq(UcUserAccount::getCurrency, CurrencyEnum.USDC));

        }
        // 买家freeze减少
        LambdaQueryWrapper<UcUserAccount> wrapper = new LambdaQueryWrapper<UcUserAccount>()
                .eq(UcUserAccount::getUserId, buyReq.getToUserId())
                .eq(UcUserAccount::getCurrency, CurrencyEnum.USDC);
        UcUserAccount account = ucUserAccountService.getOne(wrapper);

        ucUserAccountService.update(UcUserAccount.builder()
                .freeze(account.getFreeze().subtract(amount))
                .build(), new LambdaQueryWrapper<UcUserAccount>()
                .eq(UcUserAccount::getUserId, buyReq.getToUserId())
                .eq(UcUserAccount::getCurrency, CurrencyEnum.USDC));

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
    public void buyNFT(NFTBuyReq req) {
        ucUserAccountService.buyNFT(req);
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
        UcSwitchReq switchReq = new UcSwitchReq();
        switchReq.setUcUserId(req.getUserId());
        List<UpOrDownReq> reqs = new ArrayList<>();
        UpOrDownReq upOrDownReq = new UpOrDownReq();
        upOrDownReq.setId(req.getNftId());
        upOrDownReq.setStatus(WhetherEnum.YES.value());
        upOrDownReq.setPrice(req.getMaxPrice());
        upOrDownReq.setUnit(req.getCurrency().name());
        reqs.add(upOrDownReq);
        switchReq.setReqs(reqs);
        if (!remoteNftService.ucUpOrDown(switchReq).isSuccess()) {
            throw new GenericException(UcErrorCodeEnum.ERR_500_SYSTEM_BUSY.getDescEn());
        }
        // 保存正向拍卖记录
        forwardAuction = UcNftForwardAuction.builder().build();
        BeanUtil.copyProperties(req, forwardAuction);
        ucNftForwardAuctionService.save(forwardAuction);
    }

    @Override
    public void bids(NFTMakeOfferReq req) {
        ucNftOffersService.makeOffer(req);
    }
}
