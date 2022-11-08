package com.seeds.admin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.account.dto.DepositRuleDto;
import com.seeds.account.dto.req.DepositRulePageReq;
import com.seeds.account.dto.req.DepositRuleSaveOrUpdateReq;
import com.seeds.account.dto.req.ListReq;
import com.seeds.account.feign.AccountFeignClient;
import com.seeds.admin.service.ISysDepositWithdrawConfigService;
import com.seeds.common.dto.GenericDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: hewei
 * @date 2022/11/8
 */

@Service
public class SysDepositWithdrawConfigService implements ISysDepositWithdrawConfigService {

    @Autowired
    private AccountFeignClient accountFeignClient;

    @Override
    public GenericDto<Boolean> add(DepositRuleSaveOrUpdateReq req) {
        return accountFeignClient.addDepositRule(req);
    }

    @Override
    public GenericDto<Page<DepositRuleDto>> getList(DepositRulePageReq req) {
        return accountFeignClient.getDepositRuleList(req);
    }

    @Override
    public GenericDto<Boolean> update(DepositRuleSaveOrUpdateReq req) {
        return accountFeignClient.updateDepositRule(req);
    }

    @Override
    public GenericDto<Boolean> delete(ListReq req) {
        return accountFeignClient.deleteDepositRule(req);
    }
}
