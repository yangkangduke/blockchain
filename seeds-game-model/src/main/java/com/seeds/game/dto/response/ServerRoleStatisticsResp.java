package com.seeds.game.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 游戏角色统计
 *
 * @author hang.yu
 * @date 2023/02/08
 */
@Data
@ApiModel(value = "ServerRoleResp")
public class ServerRoleStatisticsResp {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("击杀评分")
    private BigDecimal killingRate;

    @ApiModelProperty("存活评分")
    private BigDecimal survivalRate;

    @ApiModelProperty("生产评分")
    private BigDecimal productionRate;

    @ApiModelProperty("排行评分")
    private BigDecimal rankingRate;

    @ApiModelProperty("掠夺评分")
    private BigDecimal lootRate;

    @ApiModelProperty("得分评分")
    private BigDecimal scoreRate;

    @ApiModelProperty("胜率")
    private BigDecimal winningRate;

    @ApiModelProperty("最高连胜")
    private Integer highWinStreak;

    @ApiModelProperty("总游戏次数")
    private Long totalGames;

    @ApiModelProperty("总游戏时间")
    private Long totalGameTime;

    @ApiModelProperty("综合评价")
    private String overallScore;

}
