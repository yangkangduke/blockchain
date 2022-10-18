package com.seeds.uc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.uc.mapper.UcNftForwardAuctionMapper;
import com.seeds.uc.model.*;
import com.seeds.uc.service.*;
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
public class UcNftForwardAuctionServiceImpl extends ServiceImpl<UcNftForwardAuctionMapper, UcNftForwardAuction> implements IUcNftForwardAuctionService {

    @Override
    public UcNftForwardAuction queryByUserIdAndNftId(Long userId, Long nftId) {
        LambdaQueryWrapper<UcNftForwardAuction> query = new QueryWrapper<UcNftForwardAuction>().lambda()
                .eq(UcNftForwardAuction::getUserId, userId)
                .eq(UcNftForwardAuction::getNftId, nftId);
        return getOne(query);
    }
}
