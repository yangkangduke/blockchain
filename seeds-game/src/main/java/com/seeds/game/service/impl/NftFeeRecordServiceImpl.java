package com.seeds.game.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.game.entity.NftFeeRecordEntity;
import com.seeds.game.mapper.NftFeeRecordMapper;
import com.seeds.game.service.INftFeeRecordService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * nft托管费记录
 * </p>
 *
 * @author hang.yu
 * @since 2023-04-19
 */
@Service
public class NftFeeRecordServiceImpl extends ServiceImpl<NftFeeRecordMapper, NftFeeRecordEntity>implements INftFeeRecordService {

    @Override
    public NftFeeRecordEntity queryByOrderId(Long orderId) {
        LambdaQueryWrapper<NftFeeRecordEntity> queryWrap = new QueryWrapper<NftFeeRecordEntity>().lambda()
                .eq(NftFeeRecordEntity::getOrderId, orderId);
        return getOne(queryWrap);
    }

    @Override
    public NftFeeRecordEntity queryByAuctionId(Long auctionId) {
        LambdaQueryWrapper<NftFeeRecordEntity> queryWrap = new QueryWrapper<NftFeeRecordEntity>().lambda()
                .eq(NftFeeRecordEntity::getAuctionId, auctionId);
        return getOne(queryWrap);
    }
}
