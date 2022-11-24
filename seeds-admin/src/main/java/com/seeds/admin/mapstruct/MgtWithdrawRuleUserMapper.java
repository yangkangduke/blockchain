package com.seeds.admin.mapstruct;

import com.seeds.account.dto.WithdrawRuleUserDto;
import com.seeds.admin.dto.MgtWithdrawRuleUserDto;
import org.mapstruct.Mapper;

import java.util.List;
@Mapper(componentModel = "spring")

public interface MgtWithdrawRuleUserMapper extends BaseMapstructMapper {

    List<MgtWithdrawRuleUserDto> convertToMdtWithdrawRuleUserDots(List<WithdrawRuleUserDto> dtos);

}
