package com.seeds.uc.dto.mapstruct;

import com.seeds.uc.dto.FileDto;
import com.seeds.uc.model.File;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author allen
 */
@Mapper(componentModel = "spring")
public interface FileDtoMapper {
    @Mapping(source = "id", target = "id")
    FileDto kycToDto(File file);
}