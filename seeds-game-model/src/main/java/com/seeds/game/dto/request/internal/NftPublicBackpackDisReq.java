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

    @ApiModelProperty("nft物品")
    @NotNull(message = "物品Id不能为空")
    private Long id;

    @ApiModelProperty("userId")
    private Long userId;

    @ApiModelProperty("游戏服务角色id")
    @NotNull(message = "游戏服角色ID不能为空")
    private Long serverRoleId;
}
