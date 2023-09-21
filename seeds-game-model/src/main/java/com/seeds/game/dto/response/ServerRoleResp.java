package com.seeds.game.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: hewei
 * @date 2023/1/31
 */

@Data
@ApiModel(value = "ServerRoleResp")
public class ServerRoleResp {
    @ApiModelProperty("id，游戏那边传的，唯一值")
    private Long id;

    @ApiModelProperty("角色名称")
    private String name;

    @ApiModelProperty("大区")
    private Integer region;

    @ApiModelProperty("大区名字")
    private String regionName;

    @ApiModelProperty("游戏服")
    private Integer gameServer;

    @ApiModelProperty("游戏服名字")
    private String gameServerName;

    @ApiModelProperty("等级")
    private Integer level;
}