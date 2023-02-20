package com.seeds.game.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * <p>
 * 游戏区服
 * </p>
 *
 * @author hang.yu
 * @since 2023-02-16
 */
@TableName("ga_server_region")
@ApiModel(value = "ServerRegionEntity对象", description = "游戏区服")
@Data
@EqualsAndHashCode(callSuper = true)
public class ServerRegionEntity extends BaseEntity  {

    @ApiModelProperty("大区")
    private Integer region;

    @ApiModelProperty("大区名称")
    private String regionName;

    @ApiModelProperty("游戏服")
    private Integer gameServer;

    @ApiModelProperty("游戏服名称")
    private String gameServerName;

}
