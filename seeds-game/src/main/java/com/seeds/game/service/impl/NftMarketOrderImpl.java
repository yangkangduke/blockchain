package com.seeds.game.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.game.entity.NftMarketOrderEntity;
import com.seeds.game.mapper.NftMarketOrderMapper;
import com.seeds.game.service.INftMarketOrderService;
import org.springframework.stereotype.Service;

@Service
public class NftMarketOrderImpl extends ServiceImpl<NftMarketOrderMapper, NftMarketOrderEntity>implements INftMarketOrderService {

    @Override
    public NftMarketOrderEntity queryByMintAddressAndStatus(String mintAddress, Integer status) {
        return getOne(new LambdaQueryWrapper<NftMarketOrderEntity>()
                .eq(NftMarketOrderEntity::getMintAddress, mintAddress)
                .eq(NftMarketOrderEntity::getStatus, status));
    }

    @Override
    public NftMarketOrderEntity queryByAuctionId(Long auctionId) {
        return getOne(new LambdaQueryWrapper<NftMarketOrderEntity>()
                .eq(NftMarketOrderEntity::getAuctionId, auctionId));
    }
}
