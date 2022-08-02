package com.seeds.uc.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.seeds.uc.enums.AuthCodeUseTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/1
 */
@Data
public class AuthCodeSendReq {
    private String email;
    @ApiModelProperty(value = "注册：REGISTER，忘记密码：RESET_PASSWORD", required = true)
    private AuthCodeUseTypeEnum useType;
    // 二次验证再次发送sms的时候用到
    private String token;
}
