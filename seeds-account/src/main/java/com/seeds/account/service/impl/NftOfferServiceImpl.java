package com.seeds.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.account.mapper.NftOfferMapper;
import com.seeds.account.service.INftOfferService;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.enums.NftOfferStatusEnum;
import com.seeds.account.dto.resp.NftOfferResp;
import com.seeds.uc.dto.response.UcUserResp;
import com.seeds.account.model.NftOffer;
import com.seeds.uc.feign.UserCenterFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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

    @Override
    public List<NftOfferResp> offerList(Long nftId) {
        LambdaQueryWrapper<NftOffer> query = new QueryWrapper<NftOffer>().lambda()
                .eq(NftOffer::getStatus, NftOfferStatusEnum.BIDDING)
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
    public List<NftOffer> queryExpiredOffers() {
        LambdaQueryWrapper<NftOffer> query = new QueryWrapper<NftOffer>().lambda()
                .eq(NftOffer::getStatus, NftOfferStatusEnum.BIDDING)
                .lt(NftOffer::getExpireTime, System.currentTimeMillis());
        return list(query);
    }

    @Override
    public List<NftOffer> queryBiddingByNftId(Long nftId) {
        LambdaQueryWrapper<NftOffer> query = new QueryWrapper<NftOffer>().lambda()
                .eq(NftOffer::getStatus, NftOfferStatusEnum.BIDDING)
                .eq(NftOffer::getNftId, nftId);
        return list(query);
    }
}
