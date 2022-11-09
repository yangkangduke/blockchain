package com.seeds.admin.service.impl;


import com.seeds.account.dto.BlacklistAddressDto;
import com.seeds.account.enums.CommonStatus;
import com.seeds.account.feign.AccountFeignClient;
import com.seeds.admin.annotation.AuditLog;
import com.seeds.admin.dto.MgtBlacklistAddressDto;
import com.seeds.admin.dto.MgtPageDto;
import com.seeds.admin.enums.Action;
import com.seeds.admin.enums.Module;
import com.seeds.admin.enums.SubModule;
import com.seeds.admin.service.MgtRiskService;
import com.seeds.common.dto.GenericDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import static cn.hutool.core.text.CharSequenceUtil.isNotBlank;

@Service
@Slf4j
public class ISysRiskServiceImpl implements MgtRiskService {

    @Autowired
    private AccountFeignClient accountFeignClient;


    @Override
    public GenericDto<MgtPageDto<List<BlacklistAddressDto>>> getBlackList(Integer type, String reason) {
        GenericDto<List<BlacklistAddressDto>> dto = accountFeignClient.getAllBlacklistAddress(type);
        if (!dto.isSuccess()) GenericDto.failure(dto.getMessage(), dto.getCode());
        return GenericDto.success(MgtPageDto.<List<BlacklistAddressDto>>builder().data(dto.getData().stream().filter(item -> {
            boolean result = true;
            if (isNotBlank(reason)) {
                result = reason.equalsIgnoreCase(item.getReason());
            }
            return result;
        }).collect(Collectors.toList())).build());
    }

    @Override
    @AuditLog(module = Module.RISK_MANAGEMENT, subModule = SubModule.DEPOSIT_BLACKLIST, action = Action.ADD)
    public GenericDto<Boolean> addDepositBlackList(MgtBlacklistAddressDto blacklistAddressDto) {
        blacklistAddressDto.setStatus(CommonStatus.ENABLED);
        return accountFeignClient.addBlacklistAddress(blacklistAddressDto);
    }

    @Override
    @AuditLog(module = Module.RISK_MANAGEMENT, subModule = SubModule.DEPOSIT_BLACKLIST, action = Action.EDIT)
    public GenericDto<Boolean> updateDepositBlackList(MgtBlacklistAddressDto blacklistAddressDto) {
        return accountFeignClient.updateBlacklistAddress(blacklistAddressDto);
    }

    @Override
    @AuditLog(module = Module.RISK_MANAGEMENT, subModule = SubModule.DEPOSIT_BLACKLIST, action = Action.DELETE)
    public GenericDto<Boolean> deleteDepositBlackList(MgtBlacklistAddressDto blacklistAddressDto) {
        return accountFeignClient.deleteBlacklistAddress(blacklistAddressDto);
    }

    @Override
    @AuditLog(module = Module.RISK_MANAGEMENT, subModule = SubModule.WITHDRAW_BLACKLIST, action = Action.ADD)
    public GenericDto<Boolean> addWithdrawBlackList(MgtBlacklistAddressDto blacklistAddressDto) {
        blacklistAddressDto.setStatus(CommonStatus.ENABLED);
        return accountFeignClient.addBlacklistAddress(blacklistAddressDto);
    }

    @Override
    @AuditLog(module = Module.RISK_MANAGEMENT, subModule = SubModule.WITHDRAW_BLACKLIST, action = Action.EDIT)
    public GenericDto<Boolean> updateWithdrawBlackList(MgtBlacklistAddressDto blacklistAddressDto) {
        return accountFeignClient.updateBlacklistAddress(blacklistAddressDto);
    }

    @Override
    @AuditLog(module = Module.RISK_MANAGEMENT, subModule = SubModule.WITHDRAW_BLACKLIST, action = Action.DELETE)
    public GenericDto<Boolean> deleteWithdrawBlackList(MgtBlacklistAddressDto blacklistAddressDto) {
        return accountFeignClient.deleteBlacklistAddress(blacklistAddressDto);
    }
}
