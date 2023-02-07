package com.seeds.game.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 物品分配
 *
 * @author: hewei
 * @date 2023/1/31
 */
@Data
public class OpenNftPublicBackpackDisResp {

    @ApiModelProperty("nft物品Id")
    private Long id;

    @ApiModelProperty("nft物品名称")
    private String itemName;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("大区")
    private Integer region;

    @ApiModelProperty("游戏服")
    private Integer gameServer;

    @ApiModelProperty("等级")
    private Integer level;
}
