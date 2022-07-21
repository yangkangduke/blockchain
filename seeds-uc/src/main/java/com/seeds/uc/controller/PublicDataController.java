package com.seeds.uc.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/2
 */
@Slf4j
@RestController
@RequestMapping("/uc-public/")
public class PublicDataController {
//    @Autowired
//    private CountryMapper countryMapper;
//
//    @Autowired
//    private CountryDtoMapper countryDtoMapper;
//
//    @GetMapping("country/list")
//    public GenericDto<List<CountryDto>> getCountryList() {
//        List<Country> countries = countryMapper.selectAll();
//        return GenericDto.success(countryDtoMapper.countryToDto(countries));
//    }
}