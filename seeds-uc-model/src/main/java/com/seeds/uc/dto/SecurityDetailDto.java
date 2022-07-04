package com.seeds.uc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/26
 */
@Data
@Builder
public class SecurityDetailDto {
    private String phone;
    private String countryCode;
    @JsonProperty("phone_state")
    private Boolean phoneState;
    private String email;
    @JsonProperty("email_state")
    private Boolean emailState;
    @JsonProperty("ga_state")
    private Boolean gaState;
}
