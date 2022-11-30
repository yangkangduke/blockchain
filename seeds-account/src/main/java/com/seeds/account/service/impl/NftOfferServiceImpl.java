package com.seeds.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.account.mapper.NftOfferMapper;
import com.seeds.account.service.INftOfferService;
import com.seeds.admin.dto.request.NftOwnerChangeReq;
import com.seeds.admin.dto.response.SysNftDetailResp;
import com.seeds.admin.feign.RemoteNftService;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.enums.NFTOfferStatusEnum;
import com.seeds.common.web.context.UserContext;
import com.seeds.account.dto.resp.NftOfferResp;
import com.seeds.uc.dto.response.UcUserResp;
import com.seeds.uc.enums.*;
import com.seeds.uc.exceptions.GenericException;
import com.seeds.account.model.NftOffer;
import com.seeds.uc.feign.UserCenterFeignClient;
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
    private UserCenterFeignClient userCenterFeignClient;

    @Autowired
    private RemoteNftService adminRemoteNftService;

    @Override
    public List<NftOfferResp> offerList(Long nftId) {
        LambdaQueryWrapper<NftOffer> query = new QueryWrapper<NftOffer>().lambda()
                .eq(NftOffer::getStatus, NFTOfferStatusEnum.BIDDING)
                .eq(NftOffer::getNftId, nftId);
        List<NftOffer> list = list(query);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<NftOfferResp> respList = new ArrayList<>();
        List<Long> userIds = list.stream().map(NftOffer::getUserId).collect(Collectors.toList());
        Map<Long, UcUserResp> userMap = new HashMap<>(userIds.size());
        try {
            GenericDto<List<UcUserResp>> userList = userCenterFeignClient.getUserList(userIds);
            userMap = userList.getData().stream().collect(Collectors.toMap(UcUserResp::getId, p -> p));
        } catch (Exception e) {
            log.error("内部请求uc获取用户信息失败");
        }
        for (NftOffer nftOffer : list) {
            NftOfferResp resp = NftOfferResp.builder().build();
            BeanUtils.copyProperties(nftOffer, resp);
            UcUserResp ucUser = userMap.get(nftOffer.getUserId());
            if (ucUser != null) {
                resp.setUserName(ucUser.getNickname());
            }
            respList.add(resp);
        }
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
        adminRemoteNftService.ownerChange(list);
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
            GenericDto<SysNftDetailResp> sysNftDetailRespGenericDto = adminRemoteNftService.ucDetail(offer.getNftId());
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
