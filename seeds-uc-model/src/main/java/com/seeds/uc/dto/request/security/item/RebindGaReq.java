package com.seeds.uc.dto.request.security.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/31
 */
@Data
public class RebindGaReq {
    // 新GA令牌
    @JsonProperty("ga_token")
    private String gaToken;
    // 原ga code
    @JsonProperty("old_ga_code")
    private String oldGaCode;
    // sms 验证码
    @JsonProperty("sms_code")
    private String smsCode;
    // 邮箱验证码
    @JsonProperty("email_code")
    private String emailCode;
}
