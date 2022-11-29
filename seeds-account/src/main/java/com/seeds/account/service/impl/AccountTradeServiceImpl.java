package com.seeds.account.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.seeds.account.AccountConstants;
import com.seeds.account.dto.NftPriceHisDto;
import com.seeds.account.dto.req.NftBuyCallbackReq;
import com.seeds.account.dto.req.NftBuyReq;
import com.seeds.account.dto.req.NftPriceHisReq;
import com.seeds.account.enums.AccountAction;
import com.seeds.account.enums.CommonActionStatus;
import com.seeds.account.model.UserAccountActionHis;
import com.seeds.account.mq.producer.KafkaProducer;
import com.seeds.account.service.AccountTradeService;
import com.seeds.account.service.IUserAccountActionHisService;
import com.seeds.account.service.IWalletAccountService;
import com.seeds.admin.dto.request.NftOwnerChangeReq;
import com.seeds.admin.dto.response.SysNftDetailResp;
import com.seeds.admin.enums.SysOwnerTypeEnum;
import com.seeds.admin.enums.WhetherEnum;
import com.seeds.admin.feign.RemoteNftService;
import com.seeds.common.constant.mq.KafkaTopic;
import com.seeds.common.enums.TargetSource;
import com.seeds.common.web.context.UserContext;
import com.seeds.uc.enums.*;
import com.seeds.uc.exceptions.GenericException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private KafkaProducer kafkaProducer;

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
                .action(AccountAction.NFT_TRADE)
                .currency(currency)
                .status(CommonActionStatus.PROCESSING)
                .source(nftId.toString())
                .amount(amount)
                .build();
        userAccountActionHisService.save(actionHistory);
        Long actionHistoryId = actionHistory.getId();

        // 远程调用admin端归属人变更接口
        List<NftOwnerChangeReq> list = new ArrayList<>();
        NftOwnerChangeReq nftOwnerChangeReq = new NftOwnerChangeReq();
        nftOwnerChangeReq.setSource(source);
        nftOwnerChangeReq.setOwnerId(currentUserId);
        nftOwnerChangeReq.setCurrency(currency);
        nftOwnerChangeReq.setId(nftId);
        nftOwnerChangeReq.setActionHistoryId(actionHistoryId);
        nftOwnerChangeReq.setOwnerType(SysOwnerTypeEnum.UC_USER.getCode());
        nftOwnerChangeReq.setAmount(amount);
        list.add(nftOwnerChangeReq);
        kafkaProducer.send(KafkaTopic.UC_NFT_OWNER_CHANGE, JSONUtil.toJsonStr(list));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void buyNftCallback(NftBuyCallbackReq buyReq) {
        BigDecimal amount = buyReq.getAmount();
        // 成功
        if (CommonActionStatus.SUCCESS == buyReq.getActionStatusEnum()) {
            // 如果是admin端mint的nft，在uc端不用记账该账户到uc_user_account，都是uc端的用户则需要记账，卖家为UC端用户
            if (null != buyReq.getOwnerType() && SysOwnerTypeEnum.UC_USER.getCode() == buyReq.getOwnerType()) {
                //  卖家balance增加
                walletAccountService.updateAvailable(buyReq.getFromUserId(), buyReq.getCurrency(), amount, false);
            }

            // 买家freeze减少
            walletAccountService.unfreeze(buyReq.getToUserId(), buyReq.getCurrency(), amount);
        } else {
            // 失败
            // 买家解冻金额，增加余额
            walletAccountService.updateFreezeAndAvailable(buyReq.getToUserId(), buyReq.getCurrency(), amount, buyReq.getCurrency(), amount);
        }

        // 改变交易记录的状态
        userAccountActionHisService.updateById(UserAccountActionHis.builder()
                .toUserId(buyReq.getToUserId())
                .status(buyReq.getActionStatusEnum())
                .amount(buyReq.getAmount())
                .id(buyReq.getActionHistoryId())
                .build());
    }

    @Override
    public NftPriceHisDto nftPriceHis(NftPriceHisReq req) {
        List<UserAccountActionHis> actionHisList = userAccountActionHisService.querySuccessByActionAndSourceAndTime(AccountAction.NFT_TRADE, req.getNftId(), req.getStartTime(), req.getEndTime());
        if (CollectionUtils.isEmpty(actionHisList)) {
            return null;
        }
        Map<String, List<UserAccountActionHis>> actionHisMap = actionHisList.stream().collect(Collectors.groupingBy(p -> DateUtil.formatDate(new Date(p.getUpdateTime()))));
        List<NftPriceHisDto.PriceHis> data = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (String key : actionHisMap.keySet()) {
            List<UserAccountActionHis> list = actionHisMap.get(key);
            BigDecimal totalPrice = BigDecimal.ZERO;
            for (UserAccountActionHis actionHis : list) {
                totalPrice = totalPrice.add(actionHis.getAmount());
                totalAmount = totalAmount.add(actionHis.getAmount());
            }
            int number = list.size();
            data.add(NftPriceHisDto.PriceHis.builder()
                    .number(number)
                    .date(key)
                    .totalPrice(totalPrice)
                    .ovgPrice(totalPrice.divide(new BigDecimal(number), 3, RoundingMode.HALF_UP))
                    .build());
        }
        return NftPriceHisDto.builder()
                .ovgAmount(totalAmount.divide(new BigDecimal(actionHisList.size()), 3, RoundingMode.HALF_UP))
                .list(data)
                .build();
    }

}
