package com.seeds.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.entity.SysMerchantGameEntity;
import com.seeds.admin.mapper.SysMerchantGameMapper;
import com.seeds.admin.service.SysMerchantGameService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 系统商家与游戏关联
 *
 * @author hang.yu
 * @date 2022/7/21
 */
@Service
public class SysMerchantGameServiceImpl extends ServiceImpl<SysMerchantGameMapper, SysMerchantGameEntity> implements SysMerchantGameService {

    @Override
    public void add(Long merchantId, Collection<Long> gameIds) {
        // 排除已存在商家和游戏的关系
        Set<Long> existSet = queryGameIdByMerchantId(merchantId);
        if (!CollectionUtils.isEmpty(existSet)) {
            gameIds = gameIds.stream().filter(p -> !existSet.contains(p)).collect(Collectors.toSet());
        }
        if (CollectionUtils.isEmpty(gameIds)) {
            return;
        }
        // 建立商家和用户关系
        for (Long gameId : gameIds) {
            SysMerchantGameEntity merchantGame = new SysMerchantGameEntity();
            merchantGame.setMerchantId(merchantId);
            merchantGame.setGameId(gameId);
            save(merchantGame);
        }
    }

    @Override
    public List<SysMerchantGameEntity> queryByMerchantId(Long merchantId) {
        LambdaQueryWrapper<SysMerchantGameEntity> query = new QueryWrapper<SysMerchantGameEntity>().lambda()
                .eq(SysMerchantGameEntity::getMerchantId, merchantId);
        return list(query);
    }

    @Override
    public Set<Long> queryGameIdByMerchantId(Long merchantId) {
        List<SysMerchantGameEntity> list = queryByMerchantId(merchantId);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptySet();
        }
        return list.stream().map(SysMerchantGameEntity::getGameId).collect(Collectors.toSet());
    }

    @Override
    public void deleteByMerchantIdAndGameIds(Long merchantId, Collection<Long> gameIds) {
        LambdaQueryWrapper<SysMerchantGameEntity> query = new QueryWrapper<SysMerchantGameEntity>().lambda()
                .eq(SysMerchantGameEntity::getMerchantId, merchantId)
                .in(SysMerchantGameEntity::getGameId, gameIds);
        remove(query);
    }

    @Override
    public void deleteByMerchantIds(Collection<Long> merchantIds) {
        LambdaQueryWrapper<SysMerchantGameEntity> query = new QueryWrapper<SysMerchantGameEntity>().lambda()
                .in(SysMerchantGameEntity::getMerchantId, merchantIds);
        remove(query);
    }
}