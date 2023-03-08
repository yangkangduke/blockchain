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
public class NftPublicBackpackTakeBackReq {

    @ApiModelProperty(value = "nft物品", required = true)
    @NotNull(message = "autoId can not null")
    private Integer autoId;

    @ApiModelProperty("userId")
    private Long userId;
}
