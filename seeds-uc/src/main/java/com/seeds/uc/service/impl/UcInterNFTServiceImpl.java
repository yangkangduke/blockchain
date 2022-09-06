package com.seeds.uc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seeds.uc.dto.request.NFTBuyCallbackReq;
import com.seeds.uc.enums.*;
import com.seeds.uc.model.*;
import com.seeds.uc.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


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
    private IUcUserAccountService ucUserAccountService;
    @Autowired
    private IUcNftOfferService ucNftOfferService;

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
        }
    }
}
