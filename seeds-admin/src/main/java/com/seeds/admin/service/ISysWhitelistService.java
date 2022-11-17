package com.seeds.admin.service;

import com.seeds.account.dto.req.WithdrawWhitelistSaveOrUpdateReq;
import com.seeds.account.model.SwitchReq;
import com.seeds.admin.dto.MgtPageDto;
import com.seeds.admin.dto.MgtWithdrawWhitelistDto;
import com.seeds.common.dto.GenericDto;

import java.util.List;

public interface ISysWhitelistService {

    GenericDto<MgtPageDto<List<MgtWithdrawWhitelistDto>>> list(Long userId, String currency,Integer chain);

    GenericDto<Boolean> addWithdrawWhiteList(WithdrawWhitelistSaveOrUpdateReq req);

    GenericDto<Boolean> updateWithdrawWhiteList(WithdrawWhitelistSaveOrUpdateReq req);

    GenericDto<Boolean> deleteWithdrawWhiteList(SwitchReq req);
}
