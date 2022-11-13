package com.seeds.admin.mapstruct;

import com.seeds.account.dto.WithdrawWhitelistDto;
import com.seeds.admin.dto.MgtWithdrawWhitelistDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import java.util.List;
@Mapper(componentModel = "spring")

public interface MgtWhitelistMapper extends BaseMapstructMapper {

    List<MgtWithdrawWhitelistDto> convertToMdtWithdrawWhitelistDots(List<WithdrawWhitelistDto> dtos);

}
