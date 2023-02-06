package com.seeds.game.dto.request.internal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 物品分配
 *
 * @author: hewei
 * @date 2023/1/31
 */
@Data
public class NftPublicBackpackDisReq {

    @ApiModelProperty(value = "nft物品", required = true)
    @NotNull(message = "物品Id不能为空")
    private Long id;

    @ApiModelProperty(value = "登陆用户的token", example = "5df3072916f6c67886917600d65d29651db5c24e")
    private String ucToken;

    @ApiModelProperty("userId")
    private Long userId;

    @ApiModelProperty(value = "游戏服务角色id", required = true)
    @NotNull(message = "游戏服角色ID不能为空")
    private Long serverRoleId;
}
