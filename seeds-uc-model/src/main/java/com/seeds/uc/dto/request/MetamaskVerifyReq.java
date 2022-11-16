package com.seeds.uc.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yk
 * @email 819628513@qq.com
 * @date 2022/07/13
 */
@Data
public class MetamaskVerifyReq {

    @ApiModelProperty(value = "钱包地址", required = true)
    private String publicAddress;
    @ApiModelProperty(value = "签名")
    private String signature;
    @ApiModelProperty(value = "签名原文")
    private String message;
    private Long userId;

    @ApiModelProperty(value = "邀请码")
    private String inviteCode;

}
