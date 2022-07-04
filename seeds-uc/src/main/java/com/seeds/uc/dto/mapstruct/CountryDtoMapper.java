package com.seeds.uc.dto.mapstruct;

import com.seeds.uc.dto.CountryDto;
import com.seeds.uc.model.Country;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/2
 */
@Mapper(componentModel = "spring")
public interface CountryDtoMapper {
    CountryDto countryToDto(Country country);

    List<CountryDto> countryToDto(List<Country> countries);
}
