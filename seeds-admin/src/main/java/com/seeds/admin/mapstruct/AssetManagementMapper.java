package com.seeds.admin.mapstruct;


import com.seeds.account.dto.AddressCollectOrderHisDto;
import com.seeds.admin.dto.MgtAddressCollectOrderHisDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AssetManagementMapper extends BaseMapstructMapper {

    @Mappings({
            @Mapping(source = "amount", target = "amount", qualifiedByName = "convertBigDecimalToString"),
            @Mapping(source = "dto", target = "feeAmount", qualifiedByName = "convertBigDecimalToStringTransfer"),
    })
    List<MgtAddressCollectOrderHisDto> convertToMgtAddressCollectOrderHisDtos(List<AddressCollectOrderHisDto> dtos);
}
