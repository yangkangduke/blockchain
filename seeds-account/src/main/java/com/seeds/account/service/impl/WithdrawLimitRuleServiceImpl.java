package com.seeds.account.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.api.client.util.Lists;
import com.seeds.account.AccountConstants;
import com.seeds.account.dto.WithdrawLimitRuleDto;
import com.seeds.account.dto.req.ListReq;
import com.seeds.account.dto.req.WithdrawLimitSaveOrUpdateReq;
import com.seeds.account.mapper.WithdrawLimitRuleMapper;
import com.seeds.account.model.WithdrawLimitRule;
import com.seeds.account.service.IWithdrawLimitRuleService;
import com.seeds.account.util.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Override
    public List<WithdrawLimitRuleDto> getList() {
        List<WithdrawLimitRuleDto> result = Lists.newArrayList();
        List<WithdrawLimitRule> list = list();
        if (null != list) {
            result = list.stream().map(p -> {
                WithdrawLimitRuleDto dto = new WithdrawLimitRuleDto();
                BeanUtils.copyProperties(p, dto);
                return dto;
            }).collect(Collectors.toList());
        }
        return result;
    }

    @Override
    public Boolean add(WithdrawLimitSaveOrUpdateReq req) {

        WithdrawLimitRule limitRule = ObjectUtils.copy(req, WithdrawLimitRule.builder().build());
        limitRule.setCreateTime(System.currentTimeMillis());
        limitRule.setUpdateTime(System.currentTimeMillis());
        limitRule.setVersion(AccountConstants.DEFAULT_VERSION);
        return save(limitRule);
    }

    @Override
    public Boolean update(WithdrawLimitSaveOrUpdateReq req) {

        WithdrawLimitRule rule = getById(req.getId());
        WithdrawLimitRule limitRule = ObjectUtils.copy(req, WithdrawLimitRule.builder().build());
        limitRule.setUpdateTime(System.currentTimeMillis());
        limitRule.setVersion(rule.getVersion() + 1);

        return updateById(limitRule);
    }

    @Override
    public Boolean delete(ListReq req) {
        Set<Long> ids = req.getIds();
        return removeBatchByIds(ids);
    }
}
