package com.seeds.game.task;

import com.alibaba.fastjson.JSONObject;
import com.seeds.game.entity.*;
import com.seeds.game.enums.NftOrderStatusEnum;
import com.seeds.game.service.INftAuctionHouseSettingService;
import com.seeds.game.service.INftMarketOrderService;
import com.seeds.game.service.INftPublicBackpackService;
import com.seeds.game.service.INftReferencePriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author hang.yu
 * @date 2023/6/25
 * NFT价格任务
 */
@Component
@Slf4j
public class NftPriceTask {

    @Autowired
    private INftMarketOrderService nftMarketOrderService;

    @Autowired
    private INftPublicBackpackService nftPublicBackpackService;

    @Autowired
    private INftReferencePriceService nftReferencePriceService;

    @Autowired
    private INftAuctionHouseSettingService nftAuctionHouseSettingService;

    @Scheduled(cron = "0 0 0 * * ?")
    @PostConstruct
    @Transactional(rollbackFor = Exception.class)
    public void referencePrice() {
        // 先拿截止时间
        NftReferencePrice referencePrice = nftReferencePriceService.queryOneNewestRecord();
        Long startTime = referencePrice.getUpdateTime();
        if (startTime == 0) {
            // 初始化历史数据参考单价
            initReferencePrice();
        }
        long endTime = startTime + 24 * 60 * 60 * 1000;
        // 每24h算一次参考价格
        while (endTime <= System.currentTimeMillis()) {
            updateReferencePrice(startTime, endTime);
            startTime = endTime;
            endTime = startTime + 24 * 60 * 60 * 1000;
        }
    }

    private void updateReferencePrice(Long startTime, Long endTime) {
        // 查询最近成交的订单
        List<NftMarketOrderEntity> orders = nftMarketOrderService.queryByTimeIntervalAndStatus(startTime, endTime, NftOrderStatusEnum.COMPLETED.getCode());
        if (!CollectionUtils.isEmpty(orders)) {
            Set<String> mintAddresses = new HashSet<>();
            Set<Long> auctionIds = new HashSet<>();
            Map<String, List<NftMarketOrderEntity>> orderMap = new HashMap<>(orders.size());
            for (NftMarketOrderEntity order : orders) {
                mintAddresses.add(order.getMintAddress());
                if (order.getAuctionId() > 0) {
                    auctionIds.add(order.getAuctionId());
                }
                List<NftMarketOrderEntity> list = orderMap.get(order.getMintAddress());
                if (CollectionUtils.isEmpty(list)) {
                    list = new ArrayList<>();
                }
                list.add(order);
                orderMap.put(order.getMintAddress(), list);
            }
            List<NftPublicBackpackEntity> backpacks = nftPublicBackpackService.queryByMintAddress(mintAddresses);
            Map<Long, NftAuctionHouseSetting> auctionMap = nftAuctionHouseSettingService.queryMapByIds(auctionIds);
            Map<Long, NftReferencePrice> priceMap = new HashMap<>(backpacks.size());
            for (NftPublicBackpackEntity backpack : backpacks) {
                List<NftMarketOrderEntity> orderList = orderMap.get(backpack.getTokenAddress());
                BigDecimal totalPrice = BigDecimal.ZERO;
                for (NftMarketOrderEntity order : orderList) {
                    NftAuctionHouseSetting auction = auctionMap.get(order.getAuctionId());
                    if (auction != null) {
                        totalPrice = totalPrice.add(auction.getEndPrice());
                    } else {
                        totalPrice = totalPrice.add(order.getPrice());
                    }
                }
                Long itemId = backpack.getItemId();
                NftReferencePrice nftPrice = priceMap.get(itemId);
                if (nftPrice == null) {
                    nftPrice = new NftReferencePrice();
                    nftPrice.setId(itemId);
                    nftPrice.setTypeId(Long.valueOf(itemId.toString().substring(0, 4)));
                    nftPrice.setGrade(Integer.valueOf(itemId.toString().substring(4, 6)));
                    nftPrice.setNumber(itemId.toString().substring(6));
                    nftPrice.setDealNum(1);
                    nftPrice.setTotalPrice(totalPrice);
                    nftPrice.setCreateTime(endTime);
                    nftPrice.setUpdateTime(endTime);
                } else {
                    nftPrice.setDealNum(nftPrice.getDealNum() + 1);
                    nftPrice.setTotalPrice(nftPrice.getTotalPrice().add(totalPrice));
                }
                priceMap.put(itemId, nftPrice);
            }
            Set<Long> itemIdSet = priceMap.keySet();
            Map<Long, NftReferencePrice> referencePriceMap = nftReferencePriceService.queryMapByIds(itemIdSet);
            Map<Long, BigDecimal> packPriceMap = new HashMap<>(itemIdSet.size());
            for (Long key : itemIdSet) {
                NftReferencePrice nftReferencePrice = priceMap.get(key);
                BigDecimal averagePrice = nftReferencePrice.getTotalPrice()
                        .divide(new BigDecimal(nftReferencePrice.getDealNum()), 4, RoundingMode.HALF_UP);
                nftReferencePrice.setAveragePrice(averagePrice);
                NftReferencePrice oldPrice = referencePriceMap.get(key);
                if (oldPrice == null) {
                    nftReferencePriceService.save(nftReferencePrice);
                } else {
                    nftReferencePriceService.updateById(nftReferencePrice);
                }
                // 关联更新高等级的参考单价
                updateHighGradeReferencePrice(nftReferencePrice, averagePrice, packPriceMap);
                packPriceMap.put(key, averagePrice);
            }
            // 关联更新背包中的参考价格数据
            updateProposedPrice(packPriceMap);
        }
    }

    private void initReferencePrice() {
        List<NftReferencePrice> list = nftReferencePriceService.list();
        Map<Long, NftReferencePrice> map = list.stream().collect(Collectors.toMap(NftReferencePrice::getTypeId, p -> p));
        List<NftPublicBackpackEntity> backpacks = nftPublicBackpackService.list();
        NftMarketOrderEntity order = nftMarketOrderService.queryByStatusEarliest(NftOrderStatusEnum.COMPLETED.getCode());
        long fulfillTime = order == null ? System.currentTimeMillis() : order.getFulfillTime();
        LocalDate localDate = new Date(fulfillTime).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        long initTime = LocalDateTime.of(localDate, LocalTime.MIDNIGHT).toInstant(ZoneOffset.UTC).toEpochMilli();
        if (!CollectionUtils.isEmpty(backpacks)) {
            for (NftPublicBackpackEntity backpack : backpacks) {
                Long itemId = backpack.getItemId();
                Long typeId = Long.valueOf(itemId.toString().substring(0, 4));
                Integer grade = Integer.valueOf(itemId.toString().substring(4, 6));
                NftReferencePrice referencePrice = map.get(typeId);
                BigDecimal unitPrice = new BigDecimal((grade - referencePrice.getGrade())).subtract(referencePrice.getAveragePrice());
                NftReferencePrice reference = new NftReferencePrice();
                reference.setId(itemId);
                reference.setTypeId(typeId);
                reference.setGrade(grade);
                reference.setNumber(itemId.toString().substring(6));
                reference.setCreateTime(initTime);
                reference.setUpdateTime(initTime);
                reference.setReferencePrice(unitPrice);
                nftReferencePriceService.save(reference);
                JSONObject attrObject = JSONObject.parseObject(backpack.getAttributes());
                int durability = (int) attrObject.get("durability");
                backpack.setProposedPrice(unitPrice.subtract(new BigDecimal(durability)));
                nftPublicBackpackService.updateById(backpack);
            }
        }
    }

    private void updateProposedPrice(Map<Long, BigDecimal> packPriceMap) {
        List<NftPublicBackpackEntity> backpacks = nftPublicBackpackService.queryByItemIds(packPriceMap.keySet());
        for (NftPublicBackpackEntity backpack : backpacks) {
            JSONObject attrObject = JSONObject.parseObject(backpack.getAttributes());
            int durability = (int) attrObject.get("durability");
            backpack.setProposedPrice(packPriceMap.get(backpack.getItemId()).subtract(new BigDecimal(durability)));
            nftPublicBackpackService.updateById(backpack);
        }
    }

    private void updateHighGradeReferencePrice(NftReferencePrice nftReferencePrice, BigDecimal averagePrice, Map<Long, BigDecimal> packPriceMap) {
        Integer grade = nftReferencePrice.getGrade();
        List<NftReferencePrice> highGrades = nftReferencePriceService.queryByTypeAndHighGradeNoAvg(nftReferencePrice.getTypeId(), grade);
        if (!CollectionUtils.isEmpty(highGrades)) {
            for (NftReferencePrice highGrade : highGrades) {
                BigDecimal referencePrice = new BigDecimal((highGrade.getGrade() - grade)).subtract(averagePrice);
                if (referencePrice.compareTo(highGrade.getReferencePrice()) < 0) {
                    highGrade.setReferencePrice(referencePrice);
                    nftReferencePriceService.updateById(highGrade);
                    packPriceMap.put(highGrade.getId(), referencePrice);
                }
            }
        }
    }

}