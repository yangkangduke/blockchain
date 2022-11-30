package com.seeds.account.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.seeds.account.AccountConstants;
import com.seeds.account.dto.NftPriceHisDto;
import com.seeds.account.dto.req.NftBuyCallbackReq;
import com.seeds.account.dto.req.NftBuyReq;
import com.seeds.account.dto.req.NftPriceHisReq;
import com.seeds.account.dto.resp.NftAuctionResp;
import com.seeds.account.dto.resp.NftOfferResp;
import com.seeds.account.enums.AccountAction;
import com.seeds.account.enums.CommonActionStatus;
import com.seeds.account.model.NftForwardAuction;
import com.seeds.account.model.NftOffer;
import com.seeds.account.model.NftReverseAuction;
import com.seeds.account.model.UserAccountActionHis;
import com.seeds.account.mq.producer.KafkaProducer;
import com.seeds.account.service.*;
import com.seeds.admin.dto.request.NftOwnerChangeReq;
import com.seeds.admin.dto.request.SysNftShelvesReq;
import com.seeds.admin.dto.response.SysNftDetailResp;
import com.seeds.admin.enums.AdminErrorCodeEnum;
import com.seeds.admin.enums.SysOwnerTypeEnum;
import com.seeds.admin.enums.WhetherEnum;
import com.seeds.admin.feign.RemoteNftService;
import com.seeds.common.constant.mq.KafkaTopic;
import com.seeds.common.enums.CurrencyEnum;
import com.seeds.common.enums.NFTOfferStatusEnum;
import com.seeds.common.enums.TargetSource;
import com.seeds.common.web.context.UserContext;
import com.seeds.account.dto.req.NftForwardAuctionReq;
import com.seeds.account.dto.req.NftMakeOfferReq;
import com.seeds.account.dto.req.NftReverseAuctionReq;
import com.seeds.account.dto.req.NftDeductGasFeeReq;
import com.seeds.uc.enums.*;
import com.seeds.uc.exceptions.GenericException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
    private INftForwardAuctionService nftForwardAuctionService;
    @Autowired
    private INftReverseAuctionService nftReverseAuctionService;

    @Autowired
    private IWalletAccountService walletAccountService;

    @Autowired
    private RemoteNftService adminRemoteNftService;

    @Autowired
    private INftOfferService nftOfferService;

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
        // 验证NFT
        Long currentUserId = validateNft(nftDetail, req.getUserId());
        // 购买和记录NFT
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
        kafkaProducer.send(KafkaTopic.AC_NFT_OWNER_CHANGE, JSONUtil.toJsonStr(list));
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
    @Transactional(rollbackFor = Exception.class)
    public void makeOffer(NftMakeOfferReq req, SysNftDetailResp nftDetail) {
        BigDecimal price = nftDetail.getPrice();
        // 验证NFT
        Long currentUserId = validateNft(nftDetail, req.getUserId());

        // 冻结金额
        walletAccountService.freeze(currentUserId, CurrencyEnum.from(nftDetail.getUnit()).name(), price);
        // 添加记录信息
        long currentTimeMillis = System.currentTimeMillis();
        // 存储offer
        NftOffer nftOffer = NftOffer.builder().build();
        BeanUtils.copyProperties(req, nftOffer);
        nftOffer.setUserId(currentUserId);
        nftOffer.setCreateTime(currentTimeMillis);
        nftOffer.setUpdateTime(currentTimeMillis);
        nftOffer.setStatus(NFTOfferStatusEnum.BIDDING);
        // 计算差异
        nftOffer.setDifference(req.getPrice().subtract(price));
        nftOfferService.save(nftOffer);
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
                    .totalPrice(totalPrice.setScale(3, RoundingMode.HALF_UP))
                    .ovgPrice(totalPrice.divide(new BigDecimal(number), 3, RoundingMode.HALF_UP))
                    .build());
        }
        return NftPriceHisDto.builder()
                .ovgAmount(totalAmount.divide(new BigDecimal(actionHisList.size()), 3, RoundingMode.HALF_UP))
                .list(data)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void forwardAuction(NftForwardAuctionReq req) {
        // 判断是否已存在正向拍卖
        NftForwardAuction forwardAuction = nftForwardAuctionService.queryByUserIdAndNftId(req.getUserId(), req.getNftId());
        if (forwardAuction != null) {
            throw new GenericException(AdminErrorCodeEnum.ERR_40013_THIS_NFT_AUCTION_IS_IN_PROGRESS.getDescEn());
        }
        // 上架NFT
        SysNftShelvesReq shelvesReq = new SysNftShelvesReq();
        shelvesReq.setUserId(req.getUserId());
        shelvesReq.setNftId(req.getNftId());
        shelvesReq.setPrice(req.getPrice());
        shelvesReq.setUnit(req.getCurrency().getCode());
        if (!adminRemoteNftService.shelves(shelvesReq).isSuccess()) {
            throw new GenericException(UcErrorCodeEnum.ERR_500_SYSTEM_BUSY.getDescEn());
        }
        // 保存正向拍卖记录
        forwardAuction = NftForwardAuction.builder().build();
        BeanUtil.copyProperties(req, forwardAuction);
        nftForwardAuctionService.save(forwardAuction);
    }

    @Override
    public void reverseAuction(NftReverseAuctionReq req) {
        // 判断是否已存在反向拍卖
        NftReverseAuction reverseAuction = nftReverseAuctionService.queryByUserIdAndNftId(req.getUserId(), req.getNftId());
        if (reverseAuction != null) {
            throw new GenericException(AdminErrorCodeEnum.ERR_40013_THIS_NFT_AUCTION_IS_IN_PROGRESS.getDescEn());
        }
        // 上架NFT
        SysNftShelvesReq shelvesReq = new SysNftShelvesReq();
        shelvesReq.setNftId(req.getNftId());
        shelvesReq.setUserId(req.getUserId());
        shelvesReq.setPrice(req.getPrice());
        shelvesReq.setUnit(req.getCurrency().getCode());
        if (!adminRemoteNftService.shelves(shelvesReq).isSuccess()) {
            throw new GenericException(UcErrorCodeEnum.ERR_500_SYSTEM_BUSY.getDescEn());
        }
        // 保存反向拍卖记录
        reverseAuction = NftReverseAuction.builder().build();
        BeanUtil.copyProperties(req, reverseAuction);
        nftReverseAuctionService.save(reverseAuction);
    }

    @Override
    public void forwardBids(NftMakeOfferReq req) {
        SysNftDetailResp sysNftDetailResp;
        try {
            sysNftDetailResp = adminRemoteNftService.ucDetail(req.getNftId()).getData();
        } catch (Exception e) {
            throw new GenericException(UcErrorCodeEnum.ERR_18009_FAILED_TO_BID_NFT);
        }
        // 判断是否已存在正向拍卖
        NftReverseAuction reverseAuction = nftReverseAuctionService.queryByUserIdAndNftId(sysNftDetailResp.getOwnerId(), req.getNftId());
        if (reverseAuction == null) {
            throw new GenericException(AdminErrorCodeEnum.ERR_40015_THIS_NFT_IS_NOT_IN_THE_AUCTION.getDescEn());
        }
        makeOffer(req, sysNftDetailResp);
    }

    @Override
    public void reverseBids(NftBuyReq req) {
        SysNftDetailResp nftDetail;
        try {
            nftDetail = adminRemoteNftService.ucDetail(req.getNftId()).getData();
        } catch (Exception e) {
            throw new GenericException(UcErrorCodeEnum.ERR_18005_ACCOUNT_BUY_FAIL);
        }
        // 判断是否已存在反向拍卖
        NftReverseAuction reverseAuction = nftReverseAuctionService.queryByUserIdAndNftId(nftDetail.getOwnerId(), req.getNftId());
        if (reverseAuction == null) {
            throw new GenericException(AdminErrorCodeEnum.ERR_40015_THIS_NFT_IS_NOT_IN_THE_AUCTION.getDescEn());
        }
        // 验证NFT
        Long currentUserId = validateNft(nftDetail, req.getUserId());
        // 购买和记录NFT
        buyNftAndRecord(nftDetail, req.getSource(), currentUserId);
    }

    @Override
    public List<NftOfferResp> offerList(Long id) {
        return nftOfferService.offerList(id);
    }

    @Override
    public NftAuctionResp actionInfo(Long id, Long userId) {
        NftAuctionResp resp = new NftAuctionResp();
        NftForwardAuction forwardAuction = nftForwardAuctionService.queryByUserIdAndNftId(userId, id);
        NftReverseAuction reverseAuction = nftReverseAuctionService.queryByUserIdAndNftId(userId, id);
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
    public void deductGasFee(NftDeductGasFeeReq req) {

    }

    private Long validateNft(SysNftDetailResp nftDetail, Long currentUserId) {
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

        return currentUserId;
    }

}
