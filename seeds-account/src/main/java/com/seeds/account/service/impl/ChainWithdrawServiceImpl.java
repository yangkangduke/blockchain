package com.seeds.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.seeds.account.dto.WithdrawRuleDto;
import com.seeds.account.enums.CommonStatus;
import com.seeds.account.mapper.WithdrawRuleMapper;
import com.seeds.account.service.IChainWithdrawService;
import com.seeds.account.tool.ListMap;
import com.seeds.account.util.ObjectUtils;
import com.seeds.common.enums.Chain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 *
 * @author yk
 *
 */
@Slf4j
@Service
public class ChainWithdrawServiceImpl implements IChainWithdrawService {

    @Autowired
    WithdrawRuleMapper withdrawRuleMapper;

    final static String ALL = "all";

    /**
     * 提币规则（与链相关）
     */
    LoadingCache<String, ListMap<WithdrawRuleDto>> rules = Caffeine.newBuilder()
            .refreshAfterWrite(15, TimeUnit.SECONDS)
            .recordStats()
            .build(k -> {
                List<WithdrawRuleDto> list = loadAll();
                Map<String, WithdrawRuleDto> map = list.stream()
                        .filter(e -> e.getStatus() == CommonStatus.ENABLED.getCode())
                        .collect(Collectors.toMap(e -> toKey(e.getChain(), e.getCurrency()), e -> e));
                return ListMap.init(list, map);
            });

    private String toKey(int chain, String currency) {
        return chain + ":" + currency;
    }

    @Override
    public WithdrawRuleDto getWithdrawRule(Chain chain, String currency) {
        return getWithdrawRuleMap().get(toKey(chain.getCode(), currency));
    }

    @Override
    public Map<String, WithdrawRuleDto> getWithdrawRuleMap() {
        return Objects.requireNonNull(rules.get(ALL)).getMap();
    }

    @Override
    public List<WithdrawRuleDto> getWithdrawRules() {
        return Objects.requireNonNull(rules.get(ALL)).getList();
    }

    @Override
    public List<WithdrawRuleDto> loadAll() {
        return withdrawRuleMapper.selectList(new QueryWrapper<>())
                .stream()
                .map(e -> ObjectUtils.copy(e, new WithdrawRuleDto()))
                .collect(Collectors.toList());
    }

}