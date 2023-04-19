package com.seeds.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.enums.WhetherEnum;
import com.seeds.common.enums.NftOfferStatusEnum;
import com.seeds.game.dto.request.NftOfferPageReq;
import com.seeds.game.dto.response.NftOfferResp;
import com.seeds.game.entity.NftAuctionHouseBiding;
import com.seeds.game.mapper.NftAuctionHouseBidingMapper;
import com.seeds.game.service.INftAuctionHouseBidingService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * nft拍卖出价
 * </p>
 *
 * @author hang.yu
 * @since 2023-03-28
 */
@Service
public class NftAuctionHouseBidingServiceImpl extends ServiceImpl<NftAuctionHouseBidingMapper, NftAuctionHouseBiding> implements INftAuctionHouseBidingService {

    @Override
    public IPage<NftOfferResp.NftOffer> queryPage(NftOfferPageReq req) {
        LambdaQueryWrapper<NftAuctionHouseBiding> queryWrap = new QueryWrapper<NftAuctionHouseBiding>().lambda()
                .eq(NftAuctionHouseBiding::getAuctionId, req.getAuctionId())
                .orderByDesc(NftAuctionHouseBiding::getPrice);
        Page<NftAuctionHouseBiding> page = page(new Page<>(req.getCurrent(), req.getSize()), queryWrap);
        List<NftAuctionHouseBiding> records = page.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }
        List<NftOfferResp.NftOffer> bidderOfferList = new ArrayList<>();
        List<NftOfferResp.NftOffer> list = new ArrayList<>();
        IPage<NftOfferResp.NftOffer> respPage = new Page<>();
        BeanUtils.copyProperties(page, respPage);
        for (NftAuctionHouseBiding record : records) {
            NftOfferResp.NftOffer resp = new NftOfferResp.NftOffer();
            BeanUtils.copyProperties(record, resp);
            if (req.getUsdRate() != null) {
                resp.setUsdPrice("$ " + record.getPrice().multiply(req.getUsdRate()));
            }
            BigDecimal difference = record.getPrice().subtract(req.getPrice())
                    .divide(req.getPrice(), 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(100))
                    .setScale(0, RoundingMode.HALF_UP);
            if (record.getPrice().compareTo(req.getPrice()) > 0) {
                resp.setDifference(difference + "% above");
            } else {
                resp.setDifference(difference.abs() + "% below");
            }
            resp.setStatus(NftOfferStatusEnum.BIDDING.getDescEn());
            if (record.getCancelTime() != null) {
                resp.setStatus(NftOfferStatusEnum.CANCELLED.getDescEn());
            }
            resp.setIsBidder(WhetherEnum.NO.value());
            if (record.getBuyer().equals(req.getPublicAddress())) {
                resp.setIsBidder(WhetherEnum.YES.value());
                bidderOfferList.add(resp);
            } else {
                list.add(resp);
            }
        }
        // 将登录用户出价添加到最前面
        if (!CollectionUtils.isEmpty(bidderOfferList)) {
            list.addAll(0, bidderOfferList);
        }
        respPage.setRecords(list);
        return respPage;
    }

    @Override
    public BigDecimal queryAuctionCurrentPrice(Long auctionId) {
        LambdaQueryWrapper<NftAuctionHouseBiding> queryWrap = new QueryWrapper<NftAuctionHouseBiding>().lambda()
                .eq(NftAuctionHouseBiding::getAuctionId, auctionId)
                .isNull(NftAuctionHouseBiding::getCancelTime)
                .orderByDesc(NftAuctionHouseBiding::getPrice);
        List<NftAuctionHouseBiding> list = list(queryWrap);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.get(0).getPrice();
    }
}
