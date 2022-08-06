package com.seeds.uc.dto.request.security.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/27
 */
@Data
public class PhoneSecurityItemReq {
    @JsonProperty("country_code")
    private String countryCode;
    private String phone;
    @JsonProperty("phone_token")
    private String phoneToken;
    @JsonProperty("auth_token")
    private String authToken;
}
