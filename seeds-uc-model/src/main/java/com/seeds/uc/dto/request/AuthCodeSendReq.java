package com.seeds.uc.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.seeds.uc.enums.AuthCodeUseTypeEnum;
import lombok.Data;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/1
 */
@Data
public class AuthCodeSendReq {
    @JsonProperty("account_name")
    private String accountName;
    private String email;
    private String phone;
    @JsonProperty("use_type")
    private AuthCodeUseTypeEnum useType;
    @JsonProperty("country_code")
    private String countryCode;
    // 二次验证再次发送sms的时候用到
    private String token;
}
