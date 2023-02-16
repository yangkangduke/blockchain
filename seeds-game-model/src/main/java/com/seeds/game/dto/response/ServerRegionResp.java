package com.seeds.game.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 游戏区服
 * </p>
 *
 * @author hang.yu
 * @since 2023-02-16
 */
@Data
@ApiModel(value = "ServerRegionResp")
public class ServerRegionResp {

    @ApiModelProperty("大区")
    private Integer region;

    @ApiModelProperty("大区名称")
    private String regionName;

    @ApiModelProperty("大区名称")
    private List<GameServer> gameServers;

    @Data
    @ApiModel(value = "GameServer", description = "游戏服")
    public static class GameServer {

        @ApiModelProperty("游戏服")
        private Integer server;

        @ApiModelProperty("游戏服名称")
        private String serverName;

    }

}
