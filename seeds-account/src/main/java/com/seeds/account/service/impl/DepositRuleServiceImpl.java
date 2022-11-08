package com.seeds.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.account.AccountConstants;
import com.seeds.account.dto.DepositRuleDto;
import com.seeds.account.dto.req.DepositRulePageReq;
import com.seeds.account.dto.req.DepositRuleSaveOrUpdateReq;
import com.seeds.account.dto.req.ListReq;
import com.seeds.account.enums.CommonStatus;
import com.seeds.account.ex.ConfigException;
import com.seeds.account.mapper.DepositRuleMapper;
import com.seeds.account.model.DepositRule;
import com.seeds.account.service.IDepositRuleService;
import com.seeds.account.util.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Set;

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
    public IPage<DepositRuleDto> getList(DepositRulePageReq req) {
        LambdaQueryWrapper<DepositRule> queryWrap = new QueryWrapper<DepositRule>().lambda()
                .eq(req.getChain() != null, DepositRule::getChain, req.getChain())
                .eq(req.getStatus() != null, DepositRule::getStatus, req.getStatus())
                .orderByDesc(DepositRule::getId);
        Page<DepositRule> page = new Page<>(req.getCurrent(), req.getSize());

        return page(page, queryWrap).convert(p -> {
            DepositRuleDto depositRuleDto = new DepositRuleDto();
            BeanUtils.copyProperties(p, depositRuleDto);
            return depositRuleDto;
        });
    }

    @Override
    public Boolean add(DepositRuleSaveOrUpdateReq req) {

        // 存在已启用的充币规则，不让添加
        this.checkEnableDepositRule(req.getChain());

        DepositRule depositRule = ObjectUtils.copy(req, DepositRule.builder().build());
        depositRule.setCreateTime(System.currentTimeMillis());
        depositRule.setUpdateTime(System.currentTimeMillis());
        depositRule.setVersion(AccountConstants.DEFAULT_VERSION);
        depositRule.setStatus(CommonStatus.ENABLED.getCode());
        return save(depositRule);
    }

    @Override
    public Boolean update(DepositRuleSaveOrUpdateReq req) {
        // 存在已启用的充币规则，不让再启用
        if (req.getStatus() == CommonStatus.ENABLED.getCode()) {
            this.checkEnableDepositRule(req.getChain());
        }
        DepositRule depositRule = ObjectUtils.copy(req, DepositRule.builder().build());
        depositRule.setUpdateTime(System.currentTimeMillis());
        depositRule.setVersion(AccountConstants.DEFAULT_VERSION);
        depositRule.setStatus(req.getStatus());
        return updateById(depositRule);
    }

    @Override
    public Boolean delete(ListReq req) {
        Set<Long> ids = req.getIds();
        return removeBatchByIds(ids);
    }


    private void checkEnableDepositRule(Integer chain) {
        // 存在已启用的充币规则，不让添加
        LambdaQueryWrapper<DepositRule> queryWrap = new QueryWrapper<DepositRule>().lambda()
                .eq(DepositRule::getChain, chain)
                .eq(DepositRule::getStatus, CommonStatus.ENABLED.getCode());
        DepositRule one = getOne(queryWrap);
        if (one != null) {
            throw new ConfigException(ILLEGAL_DEPOSIT_RULE_CONFIG);
        }
    }
}
