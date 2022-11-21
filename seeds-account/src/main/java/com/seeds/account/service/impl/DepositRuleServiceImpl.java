package com.seeds.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.api.client.util.Lists;
import com.seeds.account.AccountConstants;
import com.seeds.account.dto.DepositRuleDto;
import com.seeds.account.dto.req.DepositRuleReq;
import com.seeds.account.dto.req.DepositRuleSaveOrUpdateReq;
import com.seeds.account.enums.CommonStatus;
import com.seeds.account.ex.ConfigException;
import com.seeds.account.mapper.DepositRuleMapper;
import com.seeds.account.model.DepositRule;
import com.seeds.account.model.SwitchReq;
import com.seeds.account.service.IDepositRuleService;
import com.seeds.account.util.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.seeds.common.enums.ErrorCode.ILLEGAL_DEPOSIT_RULE_CONFIG;

/**
 * <p>
 * 充提规则 服务实现类
 * </p>
 *
 * @author yk
 * @since 2022-10-10
 */
@Service
public class DepositRuleServiceImpl extends ServiceImpl<DepositRuleMapper, DepositRule> implements IDepositRuleService {

    @Override
    public List<DepositRuleDto> getList(DepositRuleReq req) {
        List<DepositRuleDto> result = Lists.newArrayList();

        LambdaQueryWrapper<DepositRule> queryWrap = new QueryWrapper<DepositRule>().lambda()
                .eq(req.getChain() != null, DepositRule::getChain, req.getChain())
                .eq(req.getStatus() != null, DepositRule::getStatus, req.getStatus())
                .orderByDesc(DepositRule::getId);
        List<DepositRule> list = list(queryWrap);
        if (null != list) {
            result = list.stream().map(p -> {
                DepositRuleDto dto = new DepositRuleDto();
                BeanUtils.copyProperties(p, dto);
                return dto;
            }).collect(Collectors.toList());
        }
        return result;
    }

    @Override
    public Boolean add(DepositRuleSaveOrUpdateReq req) {

        // 一条链已经存在充币规则，不让添加,只能在原有的规则基础上修改
        this.checkEnableDepositRule(req.getChain());

        DepositRule depositRule = ObjectUtils.copy(req, DepositRule.builder().build());
        depositRule.setCreateTime(System.currentTimeMillis());
        depositRule.setUpdateTime(System.currentTimeMillis());
        depositRule.setVersion(AccountConstants.DEFAULT_VERSION);
        return save(depositRule);
    }

    @Override
    public Boolean update(DepositRuleSaveOrUpdateReq req) {
        // chain 不能修改为已经存在的充币规则
        this.checkEnableDepositRule(req.getChain());

        DepositRule rule = getById(req.getId());
        DepositRule depositRule = ObjectUtils.copy(req, DepositRule.builder().build());
        depositRule.setUpdateTime(System.currentTimeMillis());
        depositRule.setVersion(rule.getVersion() + 1);
        depositRule.setStatus(req.getStatus());
        return updateById(depositRule);
    }

    @Override
    public Boolean delete(SwitchReq req) {
        DepositRule disableRule = DepositRule.builder()
                .status(CommonStatus.DISABLED.getCode())
                .build();

        DepositRule depositRule = getById(req.getId());
        disableRule.setChain(depositRule.getChain());
        this.update(disableRule, new LambdaUpdateWrapper<DepositRule>()
                .eq(DepositRule::getId, req.getId()));

        DepositRule rule = DepositRule.builder()
                .id(req.getId())
                .status(req.getStatus())
                .build();
        return updateById(rule);
    }

    private void checkEnableDepositRule(Integer chain) {
        // 该链已存在充币规则，不让添加
        LambdaQueryWrapper<DepositRule> queryWrap = new QueryWrapper<DepositRule>().lambda()
                .eq(DepositRule::getChain, chain);
        DepositRule one = getOne(queryWrap);
        if (one != null) {
            throw new ConfigException(ILLEGAL_DEPOSIT_RULE_CONFIG);
        }
    }
}
