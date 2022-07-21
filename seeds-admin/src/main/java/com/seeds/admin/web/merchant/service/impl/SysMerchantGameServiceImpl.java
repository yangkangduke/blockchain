package com.seeds.admin.web.merchant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.entity.merchant.SysMerchantGameEntity;
import com.seeds.admin.web.merchant.mapper.SysMerchantGameMapper;
import com.seeds.admin.web.merchant.service.SysMerchantGameService;
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
        QueryWrapper<SysMerchantGameEntity> query = new QueryWrapper<>();
        query.eq("merchant_id", merchantId);
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
        QueryWrapper<SysMerchantGameEntity> query = new QueryWrapper<>();
        query.eq("merchant_id", merchantId);
        query.in("game_id", gameIds);
        remove(query);
    }

    @Override
    public void deleteByMerchantId(Long merchantId) {
        QueryWrapper<SysMerchantGameEntity> query = new QueryWrapper<>();
        query.eq("merchant_id", merchantId);
        remove(query);
    }
}