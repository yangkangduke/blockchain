package com.seeds.game.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 游戏区服
 * </p>
 *
 * @author hewei
 * @since 2023-01-31
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "OpenServerRegionCreateUpdateReq")
@Data
public class OpenServerRegionCreateUpdateReq extends OpenSignReq {

    @ApiModelProperty("大区")
    private Integer regionId;

    @ApiModelProperty("大区名称")
    private String regionName;

    @ApiModelProperty("游戏服")
    private Integer serverId;

    @ApiModelProperty("游戏服名称")
    private String serverName;

    @ApiModelProperty("ip地址")
    private String innerHost;
}
