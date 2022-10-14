package com.seeds.account.service.impl;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.seeds.account.dto.SystemWalletAddressDto;
import com.seeds.account.enums.CommonStatus;
import com.seeds.account.enums.WalletAddressType;
import com.seeds.account.ex.DataInconsistencyException;
import com.seeds.account.mapper.SystemWalletAddressMapper;
import com.seeds.account.model.SystemWalletAddress;
import com.seeds.account.service.ISystemWalletAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.account.tool.ListMap;
import com.seeds.account.util.ObjectUtils;
import com.seeds.common.enums.Chain;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统使用的钱包地址 服务实现类
 * </p>
 *
 * @author yk
 * @since 2022-09-28
 */
@Service
public class SystemWalletAddressServiceImpl extends ServiceImpl<SystemWalletAddressMapper, SystemWalletAddress> implements ISystemWalletAddressService {

    final static String ALL = "all";

    LoadingCache<String, ListMap<SystemWalletAddressDto>> rules = Caffeine.newBuilder()
            .refreshAfterWrite(15, TimeUnit.SECONDS)
            .recordStats()
            .build(k -> {
                List<SystemWalletAddressDto> list = loadAll();
                Map<String, SystemWalletAddressDto> map = list.stream()
                        .collect(Collectors.toMap(e -> toKey(e.getChain(), e.getType(), e.getAddress()), e -> e));
                return ListMap.init(list, map);
            });

    private String toKey(int chain, int type, String address) {
        return chain + ":" + type + ":" + address;
    }
    @Override
    public String getOne(Chain chain, WalletAddressType walletAddressType) {
        List<SystemWalletAddressDto> list = getByChainAndType(chain.getCode(), walletAddressType.getCode())
                .stream()
                .filter(e -> e.getStatus() == CommonStatus.ENABLED.getCode())
                .collect(Collectors.toList());
        if (list.size() > 1) {
            throw new DataInconsistencyException("multiple wallet " + walletAddressType);
        }
        return list.size() > 0 ? list.get(0).getAddress() : null;
    }

    private List<SystemWalletAddressDto> getByChainAndType(int chain, int type) {
        return getAll().stream()
                .filter(e -> e.getChain() == chain && e.getType() == type)
                .collect(Collectors.toList());
    }

    @Override
    public List<SystemWalletAddressDto> getAll() {
        return Objects.requireNonNull(rules.get(ALL)).getList();
    }

    @Override
    public List<SystemWalletAddressDto> loadAll() {
        return baseMapper.selectAll()
                .stream()
                .map(e -> ObjectUtils.copy(e, new SystemWalletAddressDto()))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getList(Chain chain, WalletAddressType walletAddressType) {
        return getByChainAndType(chain.getCode(), walletAddressType.getCode())
                .stream()
                .filter(e -> e.getStatus() == CommonStatus.ENABLED.getCode())
                .map(SystemWalletAddressDto::getAddress)
                .collect(Collectors.toList());
    }
}
