package com.seeds.admin.service;

import com.seeds.account.dto.req.WithdrawRuleUserSaveOrUpdateReq;
import com.seeds.account.model.SwitchReq;
import com.seeds.admin.dto.MgtPageDto;
import com.seeds.admin.dto.MgtWithdrawRuleUserDto;
import com.seeds.common.dto.GenericDto;

import java.util.List;

public interface ISysWithdrawRuleUserService {

    GenericDto<MgtPageDto<List<MgtWithdrawRuleUserDto>>> list(Long userId, String currency, Integer chain);

    GenericDto<Boolean> addWithdrawRuleUser(WithdrawRuleUserSaveOrUpdateReq req);

    GenericDto<Boolean> updateWithdrawRuleUser(WithdrawRuleUserSaveOrUpdateReq req);

    GenericDto<Boolean> deleteWithdrawRuleUser(SwitchReq req);
}
