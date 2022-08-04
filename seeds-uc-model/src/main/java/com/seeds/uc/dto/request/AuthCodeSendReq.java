package com.seeds.uc.dto.request;

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
    @ApiModelProperty(value = "需要登陆并且从DB中取的不需要传该参数")
    private String email;
    @ApiModelProperty(value = "授权码使用类型：REGISTER-注册，RESET_PASSWORD-忘记密码，CHANGE_PASSWORD-修改密码", required = true)
    private AuthCodeUseTypeEnum useType;
    @ApiModelProperty(value = "二次验证再次发送sms的时候用到")
    private String token;
}
