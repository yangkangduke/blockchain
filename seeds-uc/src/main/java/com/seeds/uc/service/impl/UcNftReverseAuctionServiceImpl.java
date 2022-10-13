package com.seeds.uc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.uc.enums.NFTOfferStatusEnum;
import com.seeds.uc.mapper.UcNftReverseAuctionMapper;
import com.seeds.uc.model.UcNftReverseAuction;
import com.seeds.uc.service.IUcNftReverseAuctionService;
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
public class UcNftReverseAuctionServiceImpl extends ServiceImpl<UcNftReverseAuctionMapper, UcNftReverseAuction> implements IUcNftReverseAuctionService {

    @Override
    public UcNftReverseAuction queryByUserIdAndNftId(Long userId, Long nftId) {
        LambdaQueryWrapper<UcNftReverseAuction> query = new QueryWrapper<UcNftReverseAuction>().lambda()
                .eq(UcNftReverseAuction::getUserId, NFTOfferStatusEnum.BIDDING)
                .eq(UcNftReverseAuction::getNftId, nftId);
        return getOne(query);
    }
}
