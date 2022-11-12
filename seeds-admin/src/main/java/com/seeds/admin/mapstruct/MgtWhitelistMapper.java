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

    @Mappings({
            @Mapping(source = "userId", target = "userId"),
            @Mapping(source = "currency", target = "currency"),
            @Mapping(source = "maxAmount", target = "maxAmount", qualifiedByName = "convertStringToBigDecimal"),
            @Mapping(source = "intradayAmount", target = "intradayAmount", qualifiedByName = "convertStringToBigDecimal"),
            @Mapping(source = "autoAmount", target = "autoAmount", qualifiedByName = "convertStringToBigDecimal"),
            @Mapping(source = "comments", target = "comments"),
           //@Mapping(source = "status", target = "status", qualifiedByName = "getStatusEnum")
    })
    WithdrawWhitelistDto convertToWithdrawWhitelistDto(MgtWithdrawWhitelistDto dto);
}
