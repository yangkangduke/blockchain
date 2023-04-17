package com.seeds.uc.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.seeds.uc.enums.ClientAuthTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author yk
 * @email 819628513@qq.com
 * @date 2022/07/13
 */
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResp {

    @ApiModelProperty(value = "the real token for login proof")
    private String ucToken;
    @ApiModelProperty(value = "2fa时使用")
    private String token;
    @ApiModelProperty(value = "账号")
    private String account;
    @ApiModelProperty(value = "授权类型: 2-email, 3-ga")
    private ClientAuthTypeEnum authType;
    @ApiModelProperty(value = "用于刷新token")
    private String refreshToken;
    @ApiModelProperty(value = "邀请码")
    private String inviteCode;

}