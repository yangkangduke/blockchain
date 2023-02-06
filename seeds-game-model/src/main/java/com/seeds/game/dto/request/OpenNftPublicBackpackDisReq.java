package com.seeds.game.dto.request;

import com.seeds.game.dto.request.internal.NftPublicBackpackDisReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 物品分配
 *
 * @author: hewei
 * @date 2023/1/31
 */
@Data
public class OpenNftPublicBackpackDisReq extends NftPublicBackpackDisReq {

    @ApiModelProperty(value = "访问键", required = true)
    @NotBlank(message = "Access key cannot be empty")
    private String accessKey;

    @ApiModelProperty(value = "签名", required = true)
    @NotBlank(message = "Signature key cannot be empty")
    private String signature;

    @ApiModelProperty(value = "时间戳", required = true)
    @NotNull(message = "Timestamp key cannot be empty")
    private Long timestamp;
}
