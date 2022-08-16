package com.seeds.uc.dto.request.security.item;

import lombok.Data;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/31
 */
@Data
public class RebindPhoneReq {
    // 新手机验证令牌
    private String newPhoneToken;
    // 原短信验证码
    private String oldSmsCode;
    // GA验证码
    private String gaCode;
    // 验邮箱证码
    private String emailCode;
}
