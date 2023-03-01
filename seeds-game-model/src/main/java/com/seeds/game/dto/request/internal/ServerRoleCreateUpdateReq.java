package com.seeds.game.dto.request.internal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 游戏服角色
 * </p>
 *
 * @author hewei
 * @since 2023-01-31
 */
@Data
@ApiModel(value = "ServerRole对象", description = "游戏服角色")
public class ServerRoleCreateUpdateReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id，游戏那边传的，唯一值")
    private Long id;

    @ApiModelProperty("角色名称")
    private String name;

    @ApiModelProperty("UC服务中的用户id")
    private Long userId;

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
