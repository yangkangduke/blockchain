package com.seeds.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.seeds.account.dto.BlacklistAddressDto;
import com.seeds.account.enums.CommonStatus;
import com.seeds.account.mapper.BlacklistAddressMapper;
import com.seeds.account.model.BlacklistAddress;
import com.seeds.account.service.IBlacklistAddressService;
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
 * Ethereum黑地址 服务实现类
 * </p>
 *
 * @author yk
 * @since 2022-10-08
 */
@Service
public class BlacklistAddressServiceImpl extends ServiceImpl<BlacklistAddressMapper, BlacklistAddress> implements IBlacklistAddressService {
    final static String ALL = "all";

    @Autowired
    BlacklistAddressMapper blacklistAddressMapper;

    LoadingCache<String, ListMap<BlacklistAddressDto>> rules = Caffeine.newBuilder()
            .refreshAfterWrite(2, TimeUnit.SECONDS)
            .recordStats()
            .build(k -> {
                List<BlacklistAddressDto> list = loadAll();
                Map<String, BlacklistAddressDto> map = list.stream()
                        .collect(Collectors.toMap(e -> e.getType() + ":" + e.getUserId() + ":" + e.getAddress(), e -> e));
                return ListMap.init(list, map);
            });

    @Override
    public List<BlacklistAddressDto> loadAll() {
        return blacklistAddressMapper.selectList(new QueryWrapper<>())
                .stream()
                .map(e -> ObjectUtils.copy(e, new BlacklistAddressDto()))
                .collect(Collectors.toList());
    }

    @Override
    public BlacklistAddressDto get(int type, long userId, String address) {
        // milo 由于要兼容大小写，所以不能直接使用map
        return getAll().stream()
                .filter(e -> e.getType() == type &&
                        e.getStatus() == CommonStatus.ENABLED &&
                        (e.getUserId() == userId ||
                                ObjectUtils.isAddressEquals(e.getAddress(), address)))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<BlacklistAddressDto> getAll() {
        return Objects.requireNonNull(rules.get(ALL)).getList();
    }
}