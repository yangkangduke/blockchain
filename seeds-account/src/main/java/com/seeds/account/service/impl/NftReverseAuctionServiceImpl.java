package com.seeds.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.account.mapper.NftReverseAuctionMapper;
import com.seeds.account.service.INftReverseAuctionService;
import com.seeds.account.model.NftReverseAuction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * <p>
 * NFT的反向拍卖 服务类
 * </p>
 *
 * @author hang.yu
 * @since 2022-10-11
 */
@Service
@Slf4j
public class NftReverseAuctionServiceImpl extends ServiceImpl<NftReverseAuctionMapper, NftReverseAuction> implements INftReverseAuctionService {

    @Override
    public NftReverseAuction queryByUserIdAndNftId(Long userId, Long nftId) {
        LambdaQueryWrapper<NftReverseAuction> query = new QueryWrapper<NftReverseAuction>().lambda()
                .eq(NftReverseAuction::getUserId, userId)
                .eq(NftReverseAuction::getNftId, nftId);
        return getOne(query);
    }
}
