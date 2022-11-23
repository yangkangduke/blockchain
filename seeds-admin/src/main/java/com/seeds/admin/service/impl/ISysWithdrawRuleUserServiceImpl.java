package com.seeds.admin.service.impl;
import com.seeds.account.dto.WithdrawRuleUserDto;
import com.seeds.account.dto.req.WithdrawRuleUserSaveOrUpdateReq;
import com.seeds.account.feign.AccountFeignClient;
import com.seeds.account.model.SwitchReq;
import com.seeds.admin.dto.MgtPageDto;
import com.seeds.admin.dto.MgtWithdrawRuleUserDto;
import com.seeds.admin.mapstruct.MgtWithdrawRuleUserMapper;
import com.seeds.admin.service.ISysWithdrawRuleUserService;
import com.seeds.common.dto.GenericDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static cn.hutool.core.text.CharSequenceUtil.isNotBlank;


@Service
public class ISysWithdrawRuleUserServiceImpl implements ISysWithdrawRuleUserService {

    @Autowired
    private AccountFeignClient accountFeignClient;

    @Autowired
    private MgtWithdrawRuleUserMapper mgtWithdrawRuleUserMapper;

    @Override
    public GenericDto<MgtPageDto<List<MgtWithdrawRuleUserDto>>> list(Long userId, String currency, Integer chain) {
        GenericDto<List<WithdrawRuleUserDto>> dto = accountFeignClient.getAllWithdrawRuleUser();
        if (!dto.isSuccess()) return GenericDto.failure(dto.getCode(), dto.getMessage());
        return GenericDto.success(MgtPageDto.<List<MgtWithdrawRuleUserDto>>builder()
                .data(mgtWithdrawRuleUserMapper.convertToMdtWithdrawRuleUserDots(dto.getData().stream().filter(item -> {
                    boolean result = true;
                    if (userId != null) {
                        result = item.getUserId().equals(userId);
                    }
                    if (isNotBlank(currency)) {
                        result = currency.equalsIgnoreCase(item.getCurrency());
                    }
                    if (chain != null){
                        result = item.getChain().equals(chain);
                    }
                    return result;
                }).collect(Collectors.toList())))
                .build());
    }


    @Override
    public GenericDto<Boolean> addWithdrawRuleUser(WithdrawRuleUserSaveOrUpdateReq req) {
        return accountFeignClient.addWithdrawRuleUser(req);
    }

    @Override
    public GenericDto<Boolean> updateWithdrawRuleUser(WithdrawRuleUserSaveOrUpdateReq req) {
        return accountFeignClient.updateWithdrawRuleUser(req);
    }

    @Override
    public GenericDto<Boolean> deleteWithdrawRuleUser(SwitchReq req) {
        return accountFeignClient.deleteWithdrawRuleUser(req);
    }

}
