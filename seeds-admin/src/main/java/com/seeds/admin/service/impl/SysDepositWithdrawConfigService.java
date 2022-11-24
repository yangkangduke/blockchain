package com.seeds.admin.service.impl;

import com.seeds.account.dto.DepositRuleDto;
import com.seeds.account.dto.WithdrawRuleDto;
import com.seeds.account.dto.req.*;
import com.seeds.account.feign.AccountFeignClient;
import com.seeds.account.model.SwitchReq;
import com.seeds.admin.service.ISysDepositWithdrawConfigService;
import com.seeds.common.dto.GenericDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: hewei
 * @date 2022/11/8
 */

@Service
public class SysDepositWithdrawConfigService implements ISysDepositWithdrawConfigService {

    @Autowired
    private AccountFeignClient accountFeignClient;

    @Override
    public GenericDto<Boolean> addDepositRule(DepositRuleSaveOrUpdateReq req) {
        return accountFeignClient.addDepositRule(req);
    }

    @Override
    public GenericDto<List<DepositRuleDto>> getDepositRuleList(DepositRuleReq req) {
        return accountFeignClient.getDepositRuleList(req);
    }

    @Override
    public GenericDto<Boolean> updateDepositRule(DepositRuleSaveOrUpdateReq req) {
        return accountFeignClient.updateDepositRule(req);
    }

    @Override
    public GenericDto<Boolean> deleteDepositRule(SwitchReq req) {
        return accountFeignClient.deleteDepositRule(req);
    }

    @Override
    public GenericDto<Boolean> addWithdrawRule(WithdrawRuleSaveOrUpdateReq req) {
        return accountFeignClient.addWithdrawRule(req);
    }

    @Override
    public GenericDto<List<WithdrawRuleDto>> getWithdrawRuleList(WithdrawRuleReq req) {
        return accountFeignClient.getWithdrawRuleList(req);
    }

    @Override
    public GenericDto<Boolean> updateWithdrawRule(WithdrawRuleSaveOrUpdateReq req) {
        return accountFeignClient.updateWithdrawRule(req);
    }

    @Override
    public GenericDto<Boolean> deleteWithdrawRule(SwitchReq req) {
        return accountFeignClient.deleteWithdrawRule(req);
    }

}
