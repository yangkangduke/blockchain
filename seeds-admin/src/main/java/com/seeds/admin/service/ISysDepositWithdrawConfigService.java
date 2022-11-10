package com.seeds.admin.service;

import com.seeds.account.dto.DepositRuleDto;
import com.seeds.account.dto.WithdrawLimitRuleDto;
import com.seeds.account.dto.WithdrawRuleDto;
import com.seeds.account.dto.req.*;
import com.seeds.account.model.SwitchReq;
import com.seeds.common.dto.GenericDto;

import java.util.List;

/**
 * @author: hewei
 * @date 2022/11/8
 */
public interface ISysDepositWithdrawConfigService {
    GenericDto<Boolean> addDepositRule(DepositRuleSaveOrUpdateReq req);

    GenericDto<List<DepositRuleDto>> getDepositRuleList(DepositRuleReq req);

    GenericDto<Boolean> updateDepositRule(DepositRuleSaveOrUpdateReq req);

    GenericDto<Boolean> deleteDepositRule(SwitchReq req);

    GenericDto<Boolean> addWithdrawRule(WithdrawRuleSaveOrUpdateReq req);

    GenericDto<List<WithdrawRuleDto>> getWithdrawRuleList(WithdrawRuleReq req);

    GenericDto<Boolean> updateWithdrawRule(WithdrawRuleSaveOrUpdateReq req);

    GenericDto<Boolean> deleteWithdrawRule(SwitchReq req);


    GenericDto<Boolean> addWithdrawLimitRule(WithdrawLimitSaveOrUpdateReq req);

    GenericDto<List<WithdrawLimitRuleDto>> getWithdrawLimitRuleList();

    GenericDto<Boolean> updateWithdrawLimitRule(WithdrawLimitSaveOrUpdateReq req);

    GenericDto<Boolean> deleteWithdrawLimitRule(ListReq req);
}
