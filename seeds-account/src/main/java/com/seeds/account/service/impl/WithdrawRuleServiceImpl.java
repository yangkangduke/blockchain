package com.seeds.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.api.client.util.Lists;
import com.seeds.account.AccountConstants;
import com.seeds.account.dto.WithdrawRuleDto;
import com.seeds.account.dto.req.WithdrawRuleReq;
import com.seeds.account.dto.req.WithdrawRuleSaveOrUpdateReq;
import com.seeds.account.enums.CommonStatus;
import com.seeds.account.ex.ConfigException;
import com.seeds.account.mapper.WithdrawRuleMapper;
import com.seeds.account.model.SwitchReq;
import com.seeds.account.model.WithdrawRule;
import com.seeds.account.service.IWithdrawRuleService;
import com.seeds.account.util.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.seeds.common.enums.ErrorCode.ILLEGAL_WITHDRAW_RULE_CONFIG;

/**
 * <p>
 * 提币规则 服务实现类
 * </p>
 *
 * @author yk
 * @since 2022-10-08
 */
@Service
public class WithdrawRuleServiceImpl extends ServiceImpl<WithdrawRuleMapper, WithdrawRule> implements IWithdrawRuleService {

    @Override
    public List<WithdrawRuleDto> getList(WithdrawRuleReq req) {

        List<WithdrawRuleDto> result = Lists.newArrayList();
        LambdaQueryWrapper<WithdrawRule> queryWrap = new QueryWrapper<WithdrawRule>().lambda()
                .eq(req.getChain() != null, WithdrawRule::getChain, req.getChain())
                .eq(req.getStatus() != null, WithdrawRule::getStatus, req.getStatus())
                .orderByDesc(WithdrawRule::getId);
        List<WithdrawRule> list = list(queryWrap);
        if (null != list) {
            result = list.stream().map(p -> {
                WithdrawRuleDto dto = new WithdrawRuleDto();
                BeanUtils.copyProperties(p, dto);
                return dto;
            }).collect(Collectors.toList());
        }
        return result;
    }

    @Override
    public Boolean add(WithdrawRuleSaveOrUpdateReq req) {

        // 存在已启用的提币规则，不让添加
        this.checkEnableWithdrawRule(req.getChain());

        WithdrawRule withdrawRule = ObjectUtils.copy(req, WithdrawRule.builder().build());
        withdrawRule.setCreateTime(System.currentTimeMillis());
        withdrawRule.setUpdateTime(System.currentTimeMillis());
        withdrawRule.setVersion(AccountConstants.DEFAULT_VERSION);
        withdrawRule.setStatus(CommonStatus.ENABLED.getCode());
        return save(withdrawRule);
    }

    @Override
    public Boolean update(WithdrawRuleSaveOrUpdateReq req) {
        // 存在已启用的提币规则，不让再启用
        if (req.getStatus() == CommonStatus.ENABLED.getCode()) {
            this.checkEnableWithdrawRule(req.getChain());
        }
        WithdrawRule rule = getById(req.getId());
        WithdrawRule withdrawRule = ObjectUtils.copy(req, WithdrawRule.builder().build());
        withdrawRule.setUpdateTime(System.currentTimeMillis());
        withdrawRule.setVersion(rule.getVersion() + 1);
        withdrawRule.setStatus(req.getStatus());
        return updateById(withdrawRule);
    }

    @Override
    public Boolean delete(SwitchReq req) {
        WithdrawRule disableRule = WithdrawRule.builder().id(req.getId()).status(CommonStatus.DISABLED.getCode()).build();

        WithdrawRule withdrawRule = getById(req.getId());
        this.update(disableRule, new LambdaUpdateWrapper<WithdrawRule>().in(WithdrawRule::getChain, withdrawRule.getChain()).ne(WithdrawRule::getId, req.getId()));

        WithdrawRule rule = WithdrawRule.builder().id(req.getId()).chain(withdrawRule.getChain()).status(req.getStatus()).build();
        return updateById(rule);
    }


    private void checkEnableWithdrawRule(Integer chain) {
        // 存在已启用的提币规则，不让添加
        LambdaQueryWrapper<WithdrawRule> queryWrap = new QueryWrapper<WithdrawRule>().lambda()
                .eq(WithdrawRule::getChain, chain)
                .eq(WithdrawRule::getStatus, CommonStatus.ENABLED.getCode());
        WithdrawRule one = getOne(queryWrap);
        if (one != null) {
            throw new ConfigException(ILLEGAL_WITHDRAW_RULE_CONFIG);
        }
    }
}
