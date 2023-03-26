package com.seeds.game.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * nft 物品所有权转移
 *
 * @author: hewei
 * @date 2023/1/31
 */
@Data
public class OpenNftOwnershipTransferReq {

    @ApiModelProperty(value = "访问键", required = true)
    @NotBlank(message = "Access key cannot be empty")
    private String accessKey;

    @ApiModelProperty(value = "签名", required = true)
    @NotBlank(message = "Signature key cannot be empty")
    private String signature;

    @ApiModelProperty(value = "时间戳", required = true)
    @NotNull(message = "Timestamp key cannot be empty")
    private Long timestamp;

    @ApiModelProperty(value = "nft物品", required = true)
    @NotNull(message = "autoId can not null")
    private Long autoId;

    @ApiModelProperty("toUserId")
    @NotNull(message = "toUserId can not null")
    private Long toUserId;

    @ApiModelProperty(value = "游戏服务角色id", required = true)
    @NotNull(message = "游戏服角色ID不能为空")
    private Long serverRoleId;
}
