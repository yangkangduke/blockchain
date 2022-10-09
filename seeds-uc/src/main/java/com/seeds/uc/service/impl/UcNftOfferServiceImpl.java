package com.seeds.uc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.request.NftOwnerChangeReq;
import com.seeds.admin.dto.response.SysNftDetailResp;
import com.seeds.admin.enums.NftStatusEnum;
import com.seeds.admin.enums.WhetherEnum;
import com.seeds.admin.feign.RemoteNftService;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.context.UserContext;
import com.seeds.uc.dto.request.NFTMakeOfferReq;
import com.seeds.uc.dto.response.NFTOfferResp;
import com.seeds.uc.dto.response.UcUserAccountInfoResp;
import com.seeds.uc.enums.*;
import com.seeds.uc.exceptions.GenericException;
import com.seeds.uc.mapper.UcNftOfferMapper;
import com.seeds.uc.model.UcNftOffer;
import com.seeds.uc.model.UcUser;
import com.seeds.uc.model.UcUserAccount;
import com.seeds.uc.model.UcUserAccountActionHistory;
import com.seeds.uc.service.IUcNftOfferService;
import com.seeds.uc.service.IUcUserAccountActionHistoryService;
import com.seeds.uc.service.IUcUserAccountService;
import com.seeds.uc.service.IUcUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


/**
 * <p>
 * NFT的Offer管理表 服务类
 * </p>
 *
 * @author hang.yu
 * @since 2022-09-05
 */
@Service
@Slf4j
public class UcNftOfferServiceImpl extends ServiceImpl<UcNftOfferMapper, UcNftOffer> implements IUcNftOfferService {

    @Autowired
    private IUcUserService ucUserService;

    @Autowired
    private RemoteNftService remoteNftService;

    @Autowired
    private IUcUserAccountService ucUserAccountService;

    @Autowired
    private IUcUserAccountActionHistoryService ucUserAccountActionHistoryService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void makeOffer(NFTMakeOfferReq req) {
        SysNftDetailResp sysNftDetailResp;
        BigDecimal price;
        try {
            GenericDto<SysNftDetailResp> sysNftDetailRespGenericDto = remoteNftService.ucDetail(req.getNftId());
            sysNftDetailResp = sysNftDetailRespGenericDto.getData();
            price = sysNftDetailResp.getPrice();
        } catch (Exception e) {
            throw new GenericException(UcErrorCodeEnum.ERR_18005_ACCOUNT_BUY_FAIL);
        }
        // 判断NFT是否可以购买
        if (sysNftDetailResp.getStatus() != NftStatusEnum.ON_SALE.getCode()) {
            throw new GenericException(UcErrorCodeEnum.ERR_18006_ACCOUNT_BUY_FAIL_INVALID_NFT_STATUS);
        }
        // 判断NFT是否已锁定
        if (WhetherEnum.YES.value() == sysNftDetailResp.getLockFlag()) {
            throw new GenericException(UcErrorCodeEnum.ERR_18007_ACCOUNT_BUY_FAIL_NFT_LOCKED);
        }
        Long currentUserId = UserContext.getCurrentUserId();
        // 检查余额
        BigDecimal reqPrice = req.getPrice();
        if (!ucUserAccountService.checkBalance(currentUserId, reqPrice)) {
            throw new GenericException(UcErrorCodeEnum.ERR_18004_ACCOUNT_BALANCE_INSUFFICIENT);
        }
        // 冻结金额
        UcUserAccountInfoResp info = ucUserAccountService.getInfo();
        ucUserAccountService.update(UcUserAccount.builder()
                .balance(info.getBalance().subtract(reqPrice))
                .freeze(info.getFreeze().add(reqPrice))
                .build(), new LambdaQueryWrapper<UcUserAccount>()
                .eq(UcUserAccount::getUserId, currentUserId)
                .eq(UcUserAccount::getCurrency, CurrencyEnum.USDC));
        // 添加记录信息
        long currentTimeMillis = System.currentTimeMillis();
        UcUserAccountActionHistory ucUserAccountActionHistory = UcUserAccountActionHistory.builder()
                .userId(currentUserId)
                .createTime(currentTimeMillis)
                .actionEnum(AccountActionEnum.BUY_NFT)
                .accountType(AccountTypeEnum.ACTUALS)
                .currency(CurrencyEnum.USDC)
                .status(AccountActionStatusEnum.PROCESSING)
                .build();
        ucUserAccountActionHistoryService.save(ucUserAccountActionHistory);
        // 存储offer
        UcNftOffer nftOffer = UcNftOffer.builder().build();
        BeanUtils.copyProperties(req, nftOffer);
        nftOffer.setUserId(currentUserId);
        nftOffer.setCreateTime(currentTimeMillis);
        nftOffer.setUpdateTime(currentTimeMillis);
        nftOffer.setStatus(NFTOfferStatusEnum.BIDDING);
        nftOffer.setActionHistoryId(ucUserAccountActionHistory.getId());
        // TODO 汇率转换
        // 计算差异
        nftOffer.setDifference(reqPrice.subtract(price));
        save(nftOffer);
    }

    @Override
    public List<NFTOfferResp> offerList(Long nftId) {
        LambdaQueryWrapper<UcNftOffer> query = new QueryWrapper<UcNftOffer>().lambda()
                .eq(UcNftOffer::getStatus, NFTOfferStatusEnum.BIDDING)
                .eq(UcNftOffer::getNftId, nftId);
        List<UcNftOffer> list = list(query);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<NFTOfferResp> respList = new ArrayList<>();
        Set<Long> userIds = list.stream().map(UcNftOffer::getUserId).collect(Collectors.toSet());
        Map<Long, String> userMap = ucUserService.queryNameByIds(userIds);
        list.forEach(p -> {
            NFTOfferResp resp = NFTOfferResp.builder().build();
            BeanUtils.copyProperties(p, resp);
            resp.setUserName(userMap.get(p.getUserId()));
            respList.add(resp);
        });
        return respList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void offerReject(Long id) {
        // 验证offer
        UcNftOffer offer = getById(id);
        validateOffer(offer);
        Long userId = offer.getUserId();
        // 解冻金额
        BigDecimal price = offer.getPrice();
        UcUserAccountInfoResp info = ucUserAccountService.getInfoByUserId(userId);
        ucUserAccountService.update(UcUserAccount.builder()
                .balance(info.getBalance().add(price))
                .freeze(info.getFreeze().subtract(price))
                .build(), new LambdaQueryWrapper<UcUserAccount>()
                .eq(UcUserAccount::getUserId, userId)
                .eq(UcUserAccount::getCurrency, CurrencyEnum.USDC));
        // 修改记录信息
        ucUserAccountActionHistoryService.update(UcUserAccountActionHistory.builder()
                .status(AccountActionStatusEnum.FAIL)
                .build(), new LambdaQueryWrapper<UcUserAccountActionHistory>()
                .eq(UcUserAccountActionHistory::getId, offer.getActionHistoryId()));
        // 修改offer
        offer.setStatus(NFTOfferStatusEnum.REJECTED);
        updateById(offer);
    }

    @Override
    public void offerAccept(Long id) {
        // 验证offer
        UcNftOffer offer = getById(id);
        SysNftDetailResp nftDetail = validateOffer(offer);
        Long userId = offer.getUserId();
        // 远程调用admin端归属人变更接口
        UcUser buyer = ucUserService.getById(userId);
        NftOwnerChangeReq nftOwnerChangeReq = new NftOwnerChangeReq();
        if (null != buyer) {
            // 买家名字、地址
            nftOwnerChangeReq.setOwnerName(buyer.getNickname());
            nftOwnerChangeReq.setToAddress(buyer.getPublicAddress());
        }
        nftOwnerChangeReq.setOwnerId(userId);
        nftOwnerChangeReq.setId(offer.getNftId());
        nftOwnerChangeReq.setActionHistoryId(offer.getActionHistoryId());
        nftOwnerChangeReq.setOfferId(id);
        nftOwnerChangeReq.setOwnerType(nftDetail.getOwnerType());
        if (nftDetail.getOwnerType() == 1) {
            // 卖家地址
            UcUser saleUser = ucUserService.getById(nftDetail.getOwnerId());
            nftOwnerChangeReq.setFromAddress(saleUser.getPublicAddress());
        }
        List<NftOwnerChangeReq> list = Collections.singletonList(nftOwnerChangeReq);
        remoteNftService.ownerChange(list);
    }

    @Override
    public List<UcNftOffer> queryExpiredOffers() {
        LambdaQueryWrapper<UcNftOffer> query = new QueryWrapper<UcNftOffer>().lambda()
                .eq(UcNftOffer::getStatus, NFTOfferStatusEnum.BIDDING)
                .lt(UcNftOffer::getExpireTime, System.currentTimeMillis());
        return list(query);
    }

    @Override
    public List<UcNftOffer> queryBiddingByNftId(Long nftId) {
        LambdaQueryWrapper<UcNftOffer> query = new QueryWrapper<UcNftOffer>().lambda()
                .eq(UcNftOffer::getStatus, NFTOfferStatusEnum.BIDDING)
                .eq(UcNftOffer::getNftId, nftId);
        return list(query);
    }

    private SysNftDetailResp validateOffer(UcNftOffer offer) {
        // 检查状态
        if (offer == null || NFTOfferStatusEnum.BIDDING != offer.getStatus() ) {
            throw new GenericException(UcErrorCodeEnum.ERR_500_SYSTEM_BUSY);
        }
        // 判断用户
        SysNftDetailResp sysNftDetailResp;
        Long ownerId;
        try {
            GenericDto<SysNftDetailResp> sysNftDetailRespGenericDto = remoteNftService.ucDetail(offer.getNftId());
            sysNftDetailResp = sysNftDetailRespGenericDto.getData();
            ownerId = sysNftDetailResp.getOwnerId();
        } catch (Exception e) {
            throw new GenericException(UcErrorCodeEnum.ERR_500_SYSTEM_BUSY);
        }
        Long currentUserId = UserContext.getCurrentUserId();
        if (!Objects.equals(currentUserId, ownerId)) {
            throw new GenericException(UcErrorCodeEnum.ERR_500_SYSTEM_BUSY);
        }
        return sysNftDetailResp;
    }
}
