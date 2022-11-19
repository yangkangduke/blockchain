package com.seeds.uc.dto.request;

import com.seeds.uc.enums.AuthCodeUseTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
* @author yk
 * @date 2020/8/1
 */
@Data
public class AuthCodeSendReq {
    @ApiModelProperty(value = "从DB中取的不需要传该参数")
    private String email;
    @ApiModelProperty(value = "授权码使用类型", required = true)
    private AuthCodeUseTypeEnum useType;
    @ApiModelProperty(value = "二次验证再次发送的时候用到")
    private String token;
    @ApiModelProperty(value = "邀请码")
    private String inviteCode;
}
