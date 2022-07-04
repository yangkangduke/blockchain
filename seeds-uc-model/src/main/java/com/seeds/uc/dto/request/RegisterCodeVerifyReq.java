package com.seeds.uc.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/2
 */
@Data
public class RegisterCodeVerifyReq {
    @JsonProperty("country_code")
    private String countryCode;
    private String phone;
    private String email;
    @JsonProperty("auth_code")
    private String authCode;
}
