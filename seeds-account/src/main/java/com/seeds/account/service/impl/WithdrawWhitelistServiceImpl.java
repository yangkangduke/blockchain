package com.seeds.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.seeds.account.dto.WithdrawWhitelistDto;
import com.seeds.account.enums.CommonStatus;
import com.seeds.account.mapper.WithdrawWhitelistMapper;
import com.seeds.account.model.WithdrawWhitelist;
import com.seeds.account.service.IWithdrawWhitelistService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.account.tool.ListMap;
import com.seeds.account.util.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 提币白名单 服务实现类
 * </p>
 *
 * @author yk
 * @since 2022-10-08
 */
@Service
public class WithdrawWhitelistServiceImpl extends ServiceImpl<WithdrawWhitelistMapper, WithdrawWhitelist> implements IWithdrawWhitelistService {

    final static String ALL = "all";

    @Autowired
    WithdrawWhitelistMapper withdrawWhitelistMapper;

    LoadingCache<String, ListMap<WithdrawWhitelistDto>> rules = Caffeine.newBuilder()
            .refreshAfterWrite(15, TimeUnit.SECONDS)
            .recordStats()
            .build(k -> {
                List<WithdrawWhitelistDto> list = loadAll();
                Map<String, WithdrawWhitelistDto> map = list.stream()
                        .filter(e -> e.getStatus() == CommonStatus.ENABLED)
                        .collect(Collectors.toMap(e -> toKey(e.getUserId(), e.getCurrency()), e -> e));
                return ListMap.init(list, map);
            });

    private String toKey(long userId, String currency) {
        return userId + ":" + currency;
    }

    @Override
    public WithdrawWhitelistDto get(long userId, String currency) {
        return getAllMap().get(toKey(userId, currency));
    }

    @Override
    public Map<String, WithdrawWhitelistDto> getAllMap() {
        return Objects.requireNonNull(rules.get(ALL)).getMap();
    }

    @Override
    public List<WithdrawWhitelistDto> loadAll() {
        return withdrawWhitelistMapper.selectList(new QueryWrapper<>())
                .stream()
                .map(e -> ObjectUtils.copy(e, new WithdrawWhitelistDto()))
                .collect(Collectors.toList());
    }
}