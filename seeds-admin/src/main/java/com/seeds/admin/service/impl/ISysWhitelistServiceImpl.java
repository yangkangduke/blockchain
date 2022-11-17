package com.seeds.admin.service.impl;
import com.seeds.account.dto.WithdrawWhitelistDto;
import com.seeds.account.dto.req.WithdrawWhitelistReq;
import com.seeds.account.dto.req.WithdrawWhitelistSaveOrUpdateReq;
import com.seeds.account.feign.AccountFeignClient;
import com.seeds.account.model.SwitchReq;
import com.seeds.admin.dto.MgtPageDto;
import com.seeds.admin.dto.MgtWithdrawWhitelistDto;
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
    public GenericDto<MgtPageDto<List<MgtWithdrawWhitelistDto>>> list(Long userId, String currency,Integer chain) {
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
                    if (chain != null){
                        result = item.getChain().equals(chain);
                    }
                    return result;
                }).collect(Collectors.toList())))
                .build());
    }


    @Override
    public GenericDto<Boolean> addWithdrawWhiteList(WithdrawWhitelistSaveOrUpdateReq req) {
        return accountFeignClient.addWithdrawWhiteList(req);
    }

    @Override
    public GenericDto<Boolean> updateWithdrawWhiteList(WithdrawWhitelistSaveOrUpdateReq req) {
        return accountFeignClient.updateWithdrawWhitelist(req);
    }

    @Override
    public GenericDto<Boolean> deleteWithdrawWhiteList(SwitchReq req) {
        return accountFeignClient.deleteWithdrawWhitelist(req);
    }

}
