package com.seeds.uc.dto.mapstruct;

import com.seeds.uc.dto.KycDto;
import com.seeds.uc.dto.KycMgtDto;
import com.seeds.uc.model.KycDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author allen
 */
@Mapper(componentModel = "spring")
public interface KycDtoMapper {
    @Mapping(source = "id", target = "id")
    KycDto kycToDto(KycDetail kycDetail);

    @Mapping(source = "id", target = "id")
    KycDetail dtoToKyc(KycDto kycDto);

    @Mapping(source = "id", target = "id")
    KycDetail mgtDtoToKyc(KycMgtDto kycMgtDto);

    @Mapping(source = "id", target = "id")
    KycMgtDto kycToMgtDto(KycDetail kycDetail);
}