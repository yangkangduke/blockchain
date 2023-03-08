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
    @NotNull(message = "autoId can not null")
    private Integer autoId;

    @ApiModelProperty("userId")
    private Long userId;

    @ApiModelProperty(value = "游戏服务角色id", required = true)
    @NotNull(message = "游戏服角色ID不能为空")
    private Long serverRoleId;
}
