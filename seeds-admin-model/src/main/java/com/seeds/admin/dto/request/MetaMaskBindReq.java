package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author yk
 * @email 819628513@qq.com
 * @date 2022/07/13
 */
@Data
public class MetaMaskBindReq implements Serializable {

    @ApiModelProperty(value = "地址", required = true)
    @NotBlank
    private String publicAddress;
    @ApiModelProperty(value = "签名", required = true)
    @NotBlank
    private String signature;
    @ApiModelProperty(value = "信息")
    private String message;

}
