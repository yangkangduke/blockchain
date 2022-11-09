package com.seeds.admin.service;

import com.seeds.admin.dto.MgtPageDto;
import com.seeds.admin.dto.MgtWithdrawWhitelistDto;
import com.seeds.common.dto.GenericDto;

import java.util.List;

public interface ISysWhitelistService {

    GenericDto<MgtPageDto<List<MgtWithdrawWhitelistDto>>> list(Long userId, String currency);

    GenericDto<Boolean> update(MgtWithdrawWhitelistDto dto);

    GenericDto<Boolean> add(MgtWithdrawWhitelistDto dto);
}
