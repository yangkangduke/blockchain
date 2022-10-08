package com.seeds.account.service.impl;

import com.seeds.account.mapper.WithdrawLimitRuleMapper;
import com.seeds.account.model.WithdrawLimitRule;
import com.seeds.account.service.IWithdrawLimitRuleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 提币限额规则 服务实现类
 * </p>
 *
 * @author yk
 * @since 2022-10-08
 */
@Service
public class WithdrawLimitRuleServiceImpl extends ServiceImpl<WithdrawLimitRuleMapper, WithdrawLimitRule> implements IWithdrawLimitRuleService {

}
