package com.seeds.uc.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.seeds.uc.enums.AuthCodeUseTypeEnum;
import lombok.Data;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/27
 */
@Data
public class AuthCodeVerifyReq {
    @JsonProperty("use_type")
    private AuthCodeUseTypeEnum useType;

    private String email;
    @JsonProperty("email_code")
    private String emailCode;

    @JsonProperty("country_code")
    private String countryCode;
    private String phone;
    @JsonProperty("sms_code")
    private String smsCode;

}
