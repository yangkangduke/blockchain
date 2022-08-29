package com.seeds.uc.dto.request.security.item;

import lombok.Data;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/31
 */
@Data
public class RebindGaReq {
    // 新GA令牌
    private String gaToken;
    // 原ga code
    private String oldGaCode;
    // sms 验证码
    private String smsCode;
    // 邮箱验证码
    private String emailCode;
}
