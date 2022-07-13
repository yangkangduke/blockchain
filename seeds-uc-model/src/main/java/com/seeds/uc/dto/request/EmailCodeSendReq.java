package com.seeds.uc.dto.request;

import com.seeds.uc.enums.AuthCodeUseTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yk
 * @email 819628513@qq.com
 * @date 2022/07/13
 */
@Data
@ApiModel(value = "邮箱验证码发送请求", description = "邮箱验证码发送请求")
public class EmailCodeSendReq {
    @ApiModelProperty(value = "邮箱", required = true)
    private String email;
    @ApiModelProperty(value = "用户类型", required = true)
    private AuthCodeUseTypeEnum useType;

}
