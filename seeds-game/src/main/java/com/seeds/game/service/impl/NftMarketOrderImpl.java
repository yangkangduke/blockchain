package com.seeds.game.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.game.entity.NftMarketOrderEntity;
import com.seeds.game.mapper.NftMarketOrderMapper;
import com.seeds.game.service.INftMarketOrderService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NftMarketOrderImpl extends ServiceImpl<NftMarketOrderMapper, NftMarketOrderEntity>implements INftMarketOrderService {

    @Override
    public NftMarketOrderEntity queryByMintAddressAndStatus(String mintAddress, Integer status) {
        return getOne(new LambdaQueryWrapper<NftMarketOrderEntity>()
                .eq(NftMarketOrderEntity::getMintAddress, mintAddress)
                .eq(NftMarketOrderEntity::getStatus, status));
    }

    @Override
    public List<NftMarketOrderEntity> queryUserTradesByAddressAndStatus(String publicAddress, Integer status) {
        return list(new LambdaQueryWrapper<NftMarketOrderEntity>()
                .eq(NftMarketOrderEntity::getStatus, status)
                .eq(NftMarketOrderEntity::getSellerAddress, publicAddress)
                .or()
                .eq(NftMarketOrderEntity::getBuyerAddress, publicAddress));
    }

    @Override
    public NftMarketOrderEntity queryByAuctionId(Long auctionId) {
        return getOne(new LambdaQueryWrapper<NftMarketOrderEntity>()
                .eq(NftMarketOrderEntity::getAuctionId, auctionId));
    }

    @Override
    public Map<Long, NftMarketOrderEntity> queryMapByAuctionIds(Collection<Long> auctionIds) {
        if (CollectionUtils.isEmpty(auctionIds)) {
            return Collections.emptyMap();
        }
        List<NftMarketOrderEntity> list = list(new LambdaQueryWrapper<NftMarketOrderEntity>()
                .in(NftMarketOrderEntity::getAuctionId, auctionIds));
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return list.stream().collect(Collectors.toMap(NftMarketOrderEntity::getAuctionId, p -> p));
    }
}
