package com.seeds.uc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/2
 */
@Data
public class CountryDto {

    @JsonProperty("area_code")
    private String areaCode;

    @JsonProperty("country_id")
    private Long id;

    @JsonProperty("name_cn")
    private String nameCn;

    @JsonProperty("name_en")
    private String nameEn;
}
