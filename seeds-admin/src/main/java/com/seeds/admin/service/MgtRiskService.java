package com.seeds.admin.service;

import com.seeds.account.dto.ActionControlDto;
import com.seeds.account.dto.BlacklistAddressDto;
import com.seeds.admin.dto.MgtActionControlDto;
import com.seeds.admin.dto.MgtBlacklistAddressDto;
import com.seeds.admin.dto.MgtDepositRule;
import com.seeds.admin.dto.MgtPageDto;
import com.seeds.common.dto.GenericDto;

import java.util.List;

public interface MgtRiskService {


    GenericDto<MgtPageDto<List<BlacklistAddressDto>>> getBlackList(Integer type, String reason);

    GenericDto<Boolean> addDepositBlackList(MgtBlacklistAddressDto blacklistAddressDto);

    GenericDto<Boolean> updateDepositBlackList(MgtBlacklistAddressDto blacklistAddressDto);

    GenericDto<Boolean> deleteDepositBlackList(MgtBlacklistAddressDto blacklistAddressDto);

    GenericDto<Boolean> addWithdrawBlackList(MgtBlacklistAddressDto blacklistAddressDto);

    GenericDto<Boolean> updateWithdrawBlackList(MgtBlacklistAddressDto blacklistAddressDto);

    GenericDto<Boolean> deleteWithdrawBlackList(MgtBlacklistAddressDto blacklistAddressDto);

    GenericDto<MgtPageDto<List<ActionControlDto>>> getAllActionControl();

    GenericDto<Boolean> updateActionControl(MgtActionControlDto dto);

    GenericDto<Boolean> addActionControl(MgtActionControlDto dto);
}
