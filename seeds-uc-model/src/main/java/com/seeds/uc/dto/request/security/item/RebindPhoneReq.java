package com.seeds.uc.dto.request.security.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/31
 */
@Data
public class RebindPhoneReq {
    // 新手机验证令牌
    @JsonProperty("new_phone_token")
    private String newPhoneToken;
    // 原短信验证码
    @JsonProperty("old_sms_code")
    private String oldSmsCode;
    // GA验证码
    @JsonProperty("ga_code")
    private String gaCode;
    // 验邮箱证码
    @JsonProperty("email_code")
    private String emailCode;
}
