package com.seeds.uc.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.seeds.uc.enums.ClientAuthTypeEnum;
import lombok.Builder;
import lombok.Data;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/7/31
 */
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResp {
    // token to do 2FA if needed
    private String token;
    // the real token for login proof
    private String ucToken;
    // 手机号
    private String phone;
    // 邮箱
    private String email;
    // 验证项：1-Phone, 2-Email, 3-GA
    private ClientAuthTypeEnum type;
}