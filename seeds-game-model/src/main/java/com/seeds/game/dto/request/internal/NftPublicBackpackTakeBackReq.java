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

    @ApiModelProperty(value = "登陆用户的token", example = "5df3072916f6c67886917600d65d29651db5c24e")
    private String ucToken;

    @ApiModelProperty("nft物品")
    @NotNull(message = "物品Id不能为空")
    private Long id;

    @ApiModelProperty("userId")
    private Long userId;
}
