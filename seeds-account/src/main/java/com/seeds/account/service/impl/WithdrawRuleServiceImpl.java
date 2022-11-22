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

import static com.seeds.common.enums.ErrorCode.*;

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

        // 一条链已经有提币规则，无法新增,只能在原来规则的基础上修改
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
        // chain 不能修改为已经存在的提币规则；
        LambdaQueryWrapper<WithdrawRule> queryWrap = new QueryWrapper<WithdrawRule>().lambda()
                .eq(WithdrawRule::getChain,req.getChain());
        WithdrawRule one = getOne(queryWrap);
        if (!one.getChain().equals(req.getChain()) || one.getId() != req.getId()){
            throw new ConfigException(WITHDRAW_RULE_ON_CHAIN_ALREADY_EXIST);
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
        WithdrawRule disableRule = WithdrawRule.builder()
                .status(CommonStatus.DISABLED.getCode())
                .build();

        WithdrawRule withdrawRule = getById(req.getId());
        disableRule.setChain(withdrawRule.getChain());
        this.update(disableRule, new LambdaUpdateWrapper<WithdrawRule>()
                .eq(WithdrawRule::getId, req.getId()));

        WithdrawRule rule = WithdrawRule.builder()
                .id(req.getId())
                .status(req.getStatus())
                .build();
        return updateById(rule);
    }

    private void checkEnableWithdrawRule(Integer chain) {
        // 该链上存在提币规则，无法添加
        LambdaQueryWrapper<WithdrawRule> queryWrap = new QueryWrapper<WithdrawRule>().lambda()
                .eq(WithdrawRule::getChain, chain);
        WithdrawRule one = getOne(queryWrap);
        if (one != null) {
            throw new ConfigException(ILLEGAL_WITHDRAW_RULE_CONFIG);
        }
    }
}
