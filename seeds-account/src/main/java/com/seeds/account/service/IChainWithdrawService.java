package com.seeds.account.service;


import com.seeds.account.dto.WithdrawLimitRuleDto;
import com.seeds.account.dto.WithdrawRuleDto;
import com.seeds.common.enums.Chain;

import java.util.List;
import java.util.Map;

/**
 *
 * @author yk
 *
 */
public interface IChainWithdrawService {

    /**
     * 从cache获提币限额规则
     * @param currency
     * @return
     */
    WithdrawLimitRuleDto getWithdrawLimitRule(String currency);

    /**
     * 从cache获取所有的提币限额规则
     * @return
     */
    Map<String, WithdrawLimitRuleDto> getWithdrawLimitRuleMap();

    /**
     * 从数据库获取所有
     * @return
     */
    List<WithdrawLimitRuleDto> loadAllLimit();

    /**
     * 从数据库获取所有
     * @return
     */
    List<WithdrawRuleDto> loadAll();

    /**
     * 从cache获提币规则
     * @param chain
     * @param currency
     * @return
     */
    WithdrawRuleDto getWithdrawRule(Chain chain, String currency);

    /**
     * 从cache获取所有的提币规则
     * @return
     */
    Map<String, WithdrawRuleDto> getWithdrawRuleMap();
}
