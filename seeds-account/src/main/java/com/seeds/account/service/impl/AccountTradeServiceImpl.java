package com.seeds.account.service.impl;

import cn.hutool.json.JSONUtil;
import com.seeds.account.AccountConstants;
import com.seeds.account.dto.req.NftBuyReq;
import com.seeds.account.enums.AccountAction;
import com.seeds.account.enums.CommonActionStatus;
import com.seeds.account.model.UserAccountActionHis;
import com.seeds.account.service.AccountTradeService;
import com.seeds.account.service.IUserAccountActionHisService;
import com.seeds.account.service.IWalletAccountService;
import com.seeds.admin.dto.request.NftOwnerChangeReq;
import com.seeds.admin.dto.response.SysNftDetailResp;
import com.seeds.admin.enums.WhetherEnum;
import com.seeds.admin.feign.RemoteNftService;
import com.seeds.common.constant.mq.KafkaTopic;
import com.seeds.common.enums.TargetSource;
import com.seeds.common.web.context.UserContext;
import com.seeds.uc.enums.*;
import com.seeds.uc.exceptions.GenericException;
import com.seeds.uc.model.UcUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 账户交易服务
 *
 * @author hang.yu
 * @since 2022-11-28
 */
@Service
@Slf4j
public class AccountTradeServiceImpl implements AccountTradeService {

    @Autowired
    private IUserAccountActionHisService userAccountActionHisService;

    @Autowired
    private IWalletAccountService walletAccountService;

    @Autowired
    private RemoteNftService adminRemoteNftService;

    @Override
    public void validateAndInitBuyNft(NftBuyReq req) {
        SysNftDetailResp nftDetail;
        try {
            nftDetail = adminRemoteNftService.ucDetail(req.getNftId()).getData();
        } catch (Exception e) {
            throw new GenericException(UcErrorCodeEnum.ERR_18005_ACCOUNT_BUY_FAIL);
        }
        Long currentUserId = req.getUserId();
        if (currentUserId == null) {
            currentUserId = UserContext.getCurrentUserId();
        }
        BigDecimal price = nftDetail.getPrice();

        //  判断nft是否是上架状态、nft是否已经购买过了
        if (nftDetail.getStatus() != WhetherEnum.YES.value()) {
            throw new GenericException(UcErrorCodeEnum.ERR_18006_ACCOUNT_BUY_FAIL_INVALID_NFT_STATUS);
        }
        // 判断NFT是否已锁定
        if (WhetherEnum.YES.value() == nftDetail.getLockFlag()) {
            throw new GenericException(UcErrorCodeEnum.ERR_18007_ACCOUNT_BUY_FAIL_NFT_LOCKED);
        }
        // 买家是否是归属人
        if (Objects.equals(nftDetail.getOwnerId(), currentUserId)) {
            throw new GenericException(UcErrorCodeEnum.ERR_18008_YOU_ALREADY_OWN_THIS_NFT);
        }

        // 检查账户里面的金额是否足够支付
        if (!walletAccountService.checkBalance(currentUserId, price, CurrencyEnum.from(nftDetail.getUnit()).name())) {
            throw new GenericException(UcErrorCodeEnum.ERR_18004_ACCOUNT_BALANCE_INSUFFICIENT);
        }
        buyNftAndRecord(nftDetail, req.getSource(), currentUserId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void buyNftAndRecord(SysNftDetailResp nftDetail, TargetSource source, Long currentUserId) {
        Long nftId = nftDetail.getId();
        BigDecimal amount = nftDetail.getPrice();
        if (currentUserId == null) {
            currentUserId = UserContext.getCurrentUserId();
        }
        String currency = CurrencyEnum.from(nftDetail.getUnit()).name();
        // 冻结金额
        walletAccountService.freeze(currentUserId, currency, amount);

        // 添加记录信息
        UserAccountActionHis actionHistory = UserAccountActionHis.builder()
                .userId(currentUserId)
                .version((int) AccountConstants.DEFAULT_VERSION)
                .createTime(System.currentTimeMillis())
                .updateTime(System.currentTimeMillis())
                .action(AccountAction.BUY_NFT)
                .currency(currency)
                .status(CommonActionStatus.PROCESSING)
                .source("")
                .amount(amount)
                .build();
        userAccountActionHisService.save(actionHistory);
        Long actionHistoryId = actionHistory.getId();

        // 远程调用admin端归属人变更接口
        /*List<NftOwnerChangeReq> list = new ArrayList<>();
        NftOwnerChangeReq nftOwnerChangeReq = new NftOwnerChangeReq();
        UcUser buyer = ucUserService.getById(currentUserId);
        if (null != buyer) {
            // 买家名字、地址
            nftOwnerChangeReq.setOwnerName(buyer.getNickname());
            nftOwnerChangeReq.setToAddress(buyer.getPublicAddress());
        }
        nftOwnerChangeReq.setSource(source);
        nftOwnerChangeReq.setOwnerId(currentUserId);
        nftOwnerChangeReq.setId(nftId);
        nftOwnerChangeReq.setActionHistoryId(actionHistoryId);
        nftOwnerChangeReq.setOwnerType(nftDetail.getOwnerType());
        nftOwnerChangeReq.setAmount(amount);
        if (nftDetail.getOwnerType() == 1) {
            // 卖家地址
            UcUser saler = ucUserService.getById(nftDetail.getOwnerId());
            nftOwnerChangeReq.setFromAddress(saler.getPublicAddress());
        }
        list.add(nftOwnerChangeReq);
        kafkaProducer.send(KafkaTopic.UC_NFT_OWNER_CHANGE, JSONUtil.toJsonStr(list));*/
    }

}
