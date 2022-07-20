package com.seeds.admin.web.merchant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.entity.merchant.SysMerchantUserEntity;
import com.seeds.admin.web.merchant.mapper.SysMerchantUserMapper;
import com.seeds.admin.web.merchant.service.SysMerchantUserService;
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
        List<SysMerchantUserEntity> merchantUserList = queryByMerchantId(merchantId);
        Set<Long> userIdSet = new HashSet<>();
        if (!CollectionUtils.isEmpty(merchantUserList)) {
            userIdSet = merchantUserList.stream().map(SysMerchantUserEntity::getUserId)
                    .filter(p -> !userIds.contains(p)).collect(Collectors.toSet());
        }
        if (CollectionUtils.isEmpty(userIdSet)) {
            return;
        }
        // 建立商家和用户关系
        for (Long userId : userIdSet) {
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