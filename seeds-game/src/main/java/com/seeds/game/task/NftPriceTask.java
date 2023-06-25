package com.seeds.game.task;

import com.seeds.game.entity.NftMarketOrderEntity;
import com.seeds.game.entity.NftPublicBackpackEntity;
import com.seeds.game.entity.NftReferencePrice;
import com.seeds.game.enums.NftOrderStatusEnum;
import com.seeds.game.service.INftMarketOrderService;
import com.seeds.game.service.INftPublicBackpackService;
import com.seeds.game.service.INftReferencePriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
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

    @Scheduled(cron = "0 0 3 * * ?")
    public void updateReferencePrice() {
        // 先拿截止时间
        NftReferencePrice referencePrice = nftReferencePriceService.queryOneNewestRecord();
        Long timeMillis = referencePrice == null ? 0L : referencePrice.getUpdateTime();
        // 查询最近成交的订单
        List<NftMarketOrderEntity> orders = nftMarketOrderService.queryByGtFulfillTimeAndStatus(timeMillis, NftOrderStatusEnum.COMPLETED.getCode());
        if (CollectionUtils.isEmpty(orders)) {
            return;
        }
        Set<String> mintAddresses = orders.stream().map(NftMarketOrderEntity::getMintAddress).collect(Collectors.toSet());
        Map<String, NftPublicBackpackEntity> nftBackMap = nftPublicBackpackService.queryMapByMintAddress(mintAddresses);
        for (NftMarketOrderEntity order : orders) {

        }
    }

}