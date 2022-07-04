package com.seeds.uc.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.seeds.uc.enums.AuthCodeUseTypeEnum;
import com.seeds.uc.enums.ClientAuthTypeEnum;
import lombok.Data;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/26
 */
@Data
public class SecuritySettingReq {
    // 需要修改的安全项, 1-手机，2-邮箱，3-ga
    private ClientAuthTypeEnum item;
    @JsonProperty("sms_code")
    private String smsCode;
    @JsonProperty("email_code")
    private String emailCode;
    @JsonProperty("ga_code")
    private String gaCode;
    @JsonProperty("use_type")
    private AuthCodeUseTypeEnum useType;
    @JsonProperty("auth_token")
    private String authToken;
}
