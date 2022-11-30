package com.seeds.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.account.mapper.NftOfferMapper;
import com.seeds.account.service.INftOfferService;
import com.seeds.admin.dto.request.NftOwnerChangeReq;
import com.seeds.admin.dto.response.SysNftDetailResp;
import com.seeds.admin.enums.WhetherEnum;
import com.seeds.admin.feign.RemoteNftService;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.enums.NFTOfferStatusEnum;
import com.seeds.common.web.context.UserContext;
import com.seeds.uc.dto.request.NFTMakeOfferReq;
import com.seeds.uc.dto.response.NFTOfferResp;
import com.seeds.uc.enums.*;
import com.seeds.uc.exceptions.GenericException;
import com.seeds.account.model.NftOffer;
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
public class NftOfferServiceImpl extends ServiceImpl<NftOfferMapper, NftOffer> implements INftOfferService {

    @Autowired
    private IUcUserService ucUserService;

    @Autowired
    private RemoteNftService remoteNftService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void makeOffer(NFTMakeOfferReq req, SysNftDetailResp sysNftDetailResp) {
        Long currentUserId = req.getUserId();
        if (currentUserId == null) {
            currentUserId = UserContext.getCurrentUserId();
        }
        BigDecimal price = sysNftDetailResp.getPrice();
        // 是否已拥有该NFT
        if (Objects.equals(sysNftDetailResp.getOwnerId(), currentUserId)) {
            throw new GenericException(UcErrorCodeEnum.ERR_18008_YOU_ALREADY_OWN_THIS_NFT);
        }
        // 判断NFT是否可以购买
        if (sysNftDetailResp.getStatus() != WhetherEnum.YES.value()) {
            throw new GenericException(UcErrorCodeEnum.ERR_18006_ACCOUNT_BUY_FAIL_INVALID_NFT_STATUS);
        }
        // 判断NFT是否已锁定
        if (WhetherEnum.YES.value() == sysNftDetailResp.getLockFlag()) {
            throw new GenericException(UcErrorCodeEnum.ERR_18007_ACCOUNT_BUY_FAIL_NFT_LOCKED);
        }
        // 检查余额
        BigDecimal reqPrice = req.getPrice();
//        if (!ucUserAccountService.checkBalance(currentUserId, reqPrice, CurrencyEnum.USDT)) {
//            throw new GenericException(UcErrorCodeEnum.ERR_18004_ACCOUNT_BALANCE_INSUFFICIENT);
//        }
        // 冻结金额
//        UcUserAccountInfoResp info = ucUserAccountService.getInfo(currentUserId);
//        ucUserAccountService.update(UcUserAccount.builder()
//                .balance(info.getBalance().subtract(reqPrice))
//                .freeze(info.getFreeze().add(reqPrice))
//                .build(), new LambdaQueryWrapper<UcUserAccount>()
//                .eq(UcUserAccount::getUserId, currentUserId)
//                .eq(UcUserAccount::getCurrency, CurrencyEnum.USDT));
        // 添加记录信息
        long currentTimeMillis = System.currentTimeMillis();
//        UcUserAccountActionHistory ucUserAccountActionHistory = UcUserAccountActionHistory.builder()
//                .userId(currentUserId)
//                .createTime(currentTimeMillis)
//                .actionEnum(AccountActionEnum.BUY_NFT)
//                .accountType(AccountTypeEnum.ACTUALS)
//                .currency(CurrencyEnum.USDT)
//                .status(AccountActionStatusEnum.PROCESSING)
//                .build();
//        ucUserAccountActionHistoryService.save(ucUserAccountActionHistory);
        // 存储offer
        NftOffer nftOffer = NftOffer.builder().build();
        BeanUtils.copyProperties(req, nftOffer);
        nftOffer.setUserId(currentUserId);
        nftOffer.setCreateTime(currentTimeMillis);
        nftOffer.setUpdateTime(currentTimeMillis);
        nftOffer.setStatus(NFTOfferStatusEnum.BIDDING);
//        nftOffer.setActionHistoryId(ucUserAccountActionHistory.getId());
        // TODO 汇率转换
        // 计算差异
        nftOffer.setDifference(reqPrice.subtract(price));
        save(nftOffer);
    }

    @Override
    public List<NFTOfferResp> offerList(Long nftId) {
        LambdaQueryWrapper<NftOffer> query = new QueryWrapper<NftOffer>().lambda()
                .eq(NftOffer::getStatus, NFTOfferStatusEnum.BIDDING)
                .eq(NftOffer::getNftId, nftId);
        List<NftOffer> list = list(query);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<NFTOfferResp> respList = new ArrayList<>();
        Set<Long> userIds = list.stream().map(NftOffer::getUserId).collect(Collectors.toSet());
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
        NftOffer offer = getById(id);
        validateOffer(offer);
        Long userId = offer.getUserId();
        // 解冻金额
        BigDecimal price = offer.getPrice();
//        UcUserAccountInfoResp info = ucUserAccountService.getInfoByUserId(userId);
//        ucUserAccountService.update(UcUserAccount.builder()
//                .balance(info.getBalance().add(price))
//                .freeze(info.getFreeze().subtract(price))
//                .build(), new LambdaQueryWrapper<UcUserAccount>()
//                .eq(UcUserAccount::getUserId, userId)
//                .eq(UcUserAccount::getCurrency, CurrencyEnum.USDT));
//        // 修改记录信息
//        ucUserAccountActionHistoryService.update(UcUserAccountActionHistory.builder()
//                .status(AccountActionStatusEnum.FAIL)
//                .build(), new LambdaQueryWrapper<UcUserAccountActionHistory>()
//                .eq(UcUserAccountActionHistory::getId, offer.getActionHistoryId()));
        // 修改offer
        offer.setStatus(NFTOfferStatusEnum.REJECTED);
        updateById(offer);
    }

    @Override
    public void offerAccept(Long id) {
        // 验证offer
        NftOffer offer = getById(id);
        SysNftDetailResp nftDetail = validateOffer(offer);
        Long userId = offer.getUserId();
        // 远程调用admin端归属人变更接口
        NftOwnerChangeReq nftOwnerChangeReq = new NftOwnerChangeReq();
        nftOwnerChangeReq.setOwnerId(userId);
        nftOwnerChangeReq.setId(offer.getNftId());
        nftOwnerChangeReq.setActionHistoryId(offer.getActionHistoryId());
        nftOwnerChangeReq.setOfferId(id);
        nftOwnerChangeReq.setOwnerType(nftDetail.getOwnerType());
        List<NftOwnerChangeReq> list = Collections.singletonList(nftOwnerChangeReq);
        remoteNftService.ownerChange(list);
    }

    @Override
    public List<NftOffer> queryExpiredOffers() {
        LambdaQueryWrapper<NftOffer> query = new QueryWrapper<NftOffer>().lambda()
                .eq(NftOffer::getStatus, NFTOfferStatusEnum.BIDDING)
                .lt(NftOffer::getExpireTime, System.currentTimeMillis());
        return list(query);
    }

    @Override
    public List<NftOffer> queryBiddingByNftId(Long nftId) {
        LambdaQueryWrapper<NftOffer> query = new QueryWrapper<NftOffer>().lambda()
                .eq(NftOffer::getStatus, NFTOfferStatusEnum.BIDDING)
                .eq(NftOffer::getNftId, nftId);
        return list(query);
    }

    private SysNftDetailResp validateOffer(NftOffer offer) {
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
