package com.seeds.admin.mapstruct;

import com.seeds.account.dto.AddressCollectOrderRequestDto;
import com.seeds.account.dto.FundCollectRequestDto;
import com.seeds.admin.dto.MgtAddressCollectOrderRequestDto;
import com.seeds.admin.dto.MgtWalletTransferRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WalletTransferRequestMapper extends BaseMapstructMapper {

    @Mappings({
            @Mapping(source = "amount", target = "amount", qualifiedByName = "convertStringToBigDecimal"),
    })
    FundCollectRequestDto convert2FundCollectRequestDto(MgtWalletTransferRequestDto dto);

    @Mappings({
            @Mapping(source = "amount", target = "amount", qualifiedByName = "convertStringToBigDecimal"),
    })
    AddressCollectOrderRequestDto.AddressOrderDetail convert2AddressOrderDetail(MgtAddressCollectOrderRequestDto.MgtAddressOrderDetail dto);

    List<AddressCollectOrderRequestDto.AddressOrderDetail> convert2AddressOrderDetails(List<MgtAddressCollectOrderRequestDto.MgtAddressOrderDetail> dtos);

    @Mappings({
            @Mapping(source = "gasPrice", target = "gasPrice", qualifiedByName = "convertStringToBigDecimal"),
            @Mapping(source = "list", target = "list"),
    })
    AddressCollectOrderRequestDto convert2AddressCollectOrderRequestDto(MgtAddressCollectOrderRequestDto dto);
}
