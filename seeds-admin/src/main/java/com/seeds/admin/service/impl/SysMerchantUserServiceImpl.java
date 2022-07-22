package com.seeds.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.entity.SysMerchantUserEntity;
import com.seeds.admin.mapper.SysMerchantUserMapper;
import com.seeds.admin.service.SysMerchantUserService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 系统商家与用户关联
 *
 * @author hang.yu
 * @date 2022/7/20
 */
@Service
public class SysMerchantUserServiceImpl extends ServiceImpl<SysMerchantUserMapper, SysMerchantUserEntity> implements SysMerchantUserService {

    @Override
    public void add(Long merchantId, Collection<Long> userIds) {
        // 排除已存在商家和用户的关系
        Set<Long> existSet = queryUserIdByMerchantId(merchantId);
        if (!CollectionUtils.isEmpty(existSet)) {
            userIds = userIds.stream().filter(p -> !existSet.contains(p)).collect(Collectors.toSet());
        }
        if (CollectionUtils.isEmpty(userIds)) {
            return;
        }
        // 建立商家和用户关系
        for (Long userId : userIds) {
            SysMerchantUserEntity merchantUser = new SysMerchantUserEntity();
            merchantUser.setMerchantId(merchantId);
            merchantUser.setUserId(userId);
            save(merchantUser);
        }
    }

    @Override
    public List<SysMerchantUserEntity> queryByMerchantId(Long merchantId) {
        QueryWrapper<SysMerchantUserEntity> query = new QueryWrapper<>();
        query.eq("merchant_id", merchantId);
        return list(query);
    }

    @Override
    public List<SysMerchantUserEntity> queryByMerchantIds(Collection<Long> merchantIds) {
        QueryWrapper<SysMerchantUserEntity> query = new QueryWrapper<>();
        query.in("merchant_id", merchantIds);
        return list(query);
    }

    @Override
    public Set<Long> queryUserIdByMerchantId(Long merchantId) {
        List<SysMerchantUserEntity> list = queryByMerchantId(merchantId);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptySet();
        }
        return list.stream().map(SysMerchantUserEntity::getUserId).collect(Collectors.toSet());
    }

    @Override
    public Map<Long, Set<Long>> queryMapByMerchantIds(Collection<Long> merchantIds) {
        QueryWrapper<SysMerchantUserEntity> query = new QueryWrapper<>();
        query.in("merchant_id", merchantIds);
        List<SysMerchantUserEntity> list = list(query);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return list.stream().collect(Collectors.groupingBy(SysMerchantUserEntity::getMerchantId,
                Collectors.mapping(SysMerchantUserEntity::getUserId, Collectors.toSet())));
    }

    @Override
    public void batchDelete(List<SysMerchantUserEntity> merchantUsers) {
        removeBatchByIds(merchantUsers);
    }

    @Override
    public List<SysMerchantUserEntity> queryByUserIds(Collection<Long> userIds) {
        QueryWrapper<SysMerchantUserEntity> query = new QueryWrapper<>();
        query.in("user_id", userIds);
        return list(query);
    }

}