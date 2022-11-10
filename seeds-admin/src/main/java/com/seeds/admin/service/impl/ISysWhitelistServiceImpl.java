package com.seeds.admin.service.impl;

import com.seeds.account.dto.WithdrawWhitelistDto;
import com.seeds.account.enums.CommonStatus;
import com.seeds.account.feign.AccountFeignClient;
import com.seeds.admin.annotation.AuditLog;
import com.seeds.admin.dto.MgtPageDto;
import com.seeds.admin.dto.MgtWithdrawWhitelistDto;
import com.seeds.admin.enums.Action;
import com.seeds.admin.enums.Module;
import com.seeds.admin.enums.SubModule;
import com.seeds.admin.mapstruct.MgtWhitelistMapper;
import com.seeds.admin.service.ISysWhitelistService;
import com.seeds.common.dto.GenericDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static cn.hutool.core.text.CharSequenceUtil.isNotBlank;

@Service
public class ISysWhitelistServiceImpl implements ISysWhitelistService {

    @Autowired
    private AccountFeignClient accountFeignClient;

    @Autowired
    private MgtWhitelistMapper mgtWhitelistMapper;

    @Override
    public GenericDto<MgtPageDto<List<MgtWithdrawWhitelistDto>>> list(Long userId, String currency) {
        GenericDto<List<WithdrawWhitelistDto>> dto = accountFeignClient.getAllWithdrawWhitelist();
        if (!dto.isSuccess()) return GenericDto.failure(dto.getCode(), dto.getMessage());
        return GenericDto.success(MgtPageDto.<List<MgtWithdrawWhitelistDto>>builder()
                .data(mgtWhitelistMapper.convertToMdtWithdrawWhitelistDots(dto.getData().stream().filter(item -> {
                    boolean result = true;
                    if (userId != null) {
                        result = item.getUserId().equals(userId);
                    }
                    if (isNotBlank(currency)) {
                        result = currency.equalsIgnoreCase(item.getCurrency());
                    }
                    return result;
                }).collect(Collectors.toList())))
                .build());
    }

    @Override
    @AuditLog(module = Module.USER_MANAGEMENT, subModule = SubModule.WHITE_LIST_MANAGEMENT, action = Action.EDIT)
    public GenericDto<Boolean> update(MgtWithdrawWhitelistDto dto) {
        return accountFeignClient.updateWithdrawWhitelist(mgtWhitelistMapper.convertToWithdrawWhitelistDto(dto));
    }

    @Override
    @AuditLog(module = Module.USER_MANAGEMENT, subModule = SubModule.WHITE_LIST_MANAGEMENT, action = Action.ADD)
    public GenericDto<Boolean> add(MgtWithdrawWhitelistDto dto) {
        dto.setStatus(CommonStatus.ENABLED);
        WithdrawWhitelistDto withdrawWhitelistDto = mgtWhitelistMapper.convertToWithdrawWhitelistDto(dto);
        withdrawWhitelistDto.setComments(dto.getComments());
        return accountFeignClient.addWithdrawWhitelist(withdrawWhitelistDto);
    }
}
