package com.seeds.admin.mapstruct;

import com.seeds.account.dto.ChainDepositWithdrawHisDto;
import com.seeds.admin.dto.MgtDepositWithdrawDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DepositWithdrawDtoMapper extends BaseMapstructMapper {


    List<MgtDepositWithdrawDto> convert2MgtDepositWithdrawDtos(List<ChainDepositWithdrawHisDto> dtos);

}
