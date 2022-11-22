package com.seeds.uc.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author yk
 * @email 819628513@qq.com
 * @date 2022/07/13
 */
@Data
public class InviteCodeReq {

    @ApiModelProperty(value = "账号")
    @NotBlank(message = "Account can not be empty")
    private String email;

    @ApiModelProperty(value = "邀请码")
    private String inviteCode;

}
