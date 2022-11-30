package com.seeds.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.account.mapper.NftForwardAuctionMapper;
import com.seeds.account.model.NftForwardAuction;
import com.seeds.account.service.INftForwardAuctionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * <p>
 * NFT的正向拍卖 服务类
 * </p>
 *
 * @author hang.yu
 * @since 2022-10-11
 */
@Service
@Slf4j
public class NftForwardAuctionServiceImpl extends ServiceImpl<NftForwardAuctionMapper, NftForwardAuction> implements INftForwardAuctionService {

    @Override
    public NftForwardAuction queryByUserIdAndNftId(Long userId, Long nftId) {
        LambdaQueryWrapper<NftForwardAuction> query = new QueryWrapper<NftForwardAuction>().lambda()
                .eq(NftForwardAuction::getUserId, userId)
                .eq(NftForwardAuction::getNftId, nftId);
        return getOne(query);
    }
}
