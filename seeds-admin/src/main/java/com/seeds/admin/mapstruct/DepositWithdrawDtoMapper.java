package com.seeds.admin.mapstruct;

import com.seeds.account.dto.ChainDepositWithdrawHisDto;
import com.seeds.account.enums.ChainAction;
import com.seeds.admin.dto.MgtDepositWithdrawDto;
import com.seeds.common.enums.Chain;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DepositWithdrawDtoMapper extends BaseMapstructMapper {


    List<MgtDepositWithdrawDto> convert2MgtDepositWithdrawDtos(List<ChainDepositWithdrawHisDto> dtos);

}
