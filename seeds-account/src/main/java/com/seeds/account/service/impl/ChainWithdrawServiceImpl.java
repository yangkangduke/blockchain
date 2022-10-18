package com.seeds.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.seeds.account.AccountConstants;
import com.seeds.account.dto.WithdrawLimitRuleDto;
import com.seeds.account.dto.WithdrawRuleDto;
import com.seeds.account.enums.CommonStatus;
import com.seeds.account.ex.MissingElementException;
import com.seeds.account.mapper.WithdrawLimitRuleMapper;
import com.seeds.account.mapper.WithdrawRuleMapper;
import com.seeds.account.model.WithdrawRule;
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
    @Autowired
    WithdrawLimitRuleMapper withdrawLimitRuleMapper;

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
                        .filter(e -> e.getStatus() == CommonStatus.ENABLED)
                        .collect(Collectors.toMap(e -> toKey(e.getChain().getCode(), e.getCurrency()), e -> e));
                return ListMap.init(list, map);
            });

    private String toKey(int chain, String currency) {
        return chain + ":" + currency;
    }


    @Override
    public WithdrawLimitRuleDto getWithdrawLimitRule(String currency) {
        return getWithdrawLimitRuleMap().get(currency);
    }

    @Override
    public Map<String, WithdrawLimitRuleDto> getWithdrawLimitRuleMap() {
        return Objects.requireNonNull(limits.get(ALL)).getMap();
    }

    @Override
    public List<WithdrawLimitRuleDto> loadAllLimit() {
        return withdrawLimitRuleMapper.selectList(new QueryWrapper<>())
                .stream()
                .map(e -> ObjectUtils.copy(e, new WithdrawLimitRuleDto()))
                .collect(Collectors.toList());
    }

    /**
     * 提币限额(与链无关)
     */
    LoadingCache<String, ListMap<WithdrawLimitRuleDto>> limits = Caffeine.newBuilder()
            .refreshAfterWrite(15, TimeUnit.SECONDS)
            .recordStats()
            .build(k -> {
                List<WithdrawLimitRuleDto> list = loadAllLimit();
                Map<String, WithdrawLimitRuleDto> map = list.stream()
                        .collect(Collectors.toMap(WithdrawLimitRuleDto::getCurrency, e -> e));
                return ListMap.init(list, map);
            });

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
    public List<WithdrawLimitRuleDto> getWithdrawLimitRules() {
        return Objects.requireNonNull(limits.get(ALL)).getList();
    }

    @Override
    public List<WithdrawRuleDto> loadAll() {
        return withdrawRuleMapper.selectList(new QueryWrapper<>())
                .stream()
                .map(e -> ObjectUtils.copy(e, new WithdrawRuleDto()))
                .collect(Collectors.toList());
    }

}
