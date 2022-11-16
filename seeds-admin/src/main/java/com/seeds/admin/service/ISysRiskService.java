package com.seeds.admin.service;

import com.seeds.account.dto.ActionControlDto;
import com.seeds.account.dto.BlacklistAddressDto;
import com.seeds.account.dto.req.BlackListAddressSaveOrUpdateReq;
import com.seeds.account.model.SwitchReq;
import com.seeds.admin.dto.MgtActionControlDto;
import com.seeds.admin.dto.MgtPageDto;
import com.seeds.common.dto.GenericDto;
import java.util.List;

public interface ISysRiskService {


    GenericDto<MgtPageDto<List<BlacklistAddressDto>>> getBlackList(Integer type, String reason);

    GenericDto<Boolean> addDepositBlackList(BlackListAddressSaveOrUpdateReq req);

    GenericDto<Boolean> updateDepositBlackList(BlackListAddressSaveOrUpdateReq req);

    GenericDto<Boolean> deleteBlackList(SwitchReq req);

    GenericDto<Boolean> addWithdrawBlackList(BlackListAddressSaveOrUpdateReq req);

    GenericDto<Boolean> updateWithdrawBlackList(BlackListAddressSaveOrUpdateReq req);

    GenericDto<MgtPageDto<List<ActionControlDto>>> getAllActionControl();

    GenericDto<Boolean> updateActionControl(MgtActionControlDto dto);

    GenericDto<Boolean> addActionControl(MgtActionControlDto dto);
}
