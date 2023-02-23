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

    @ApiModelProperty("血腥积分")
    private BigDecimal killScore;

    @ApiModelProperty("生存积分")
    private BigDecimal survivalScore;

    @ApiModelProperty("生产积分")
    private BigDecimal drawingScore;

    @ApiModelProperty("掠夺积分")
    private BigDecimal lootScore;

    @ApiModelProperty("天梯积分")
    private BigDecimal ladderScore;

    @ApiModelProperty("竞技场积分")
    private BigDecimal rankScore;

    @ApiModelProperty("专业积分")
    private BigDecimal professionalScore;

    @ApiModelProperty("胜率")
    private String winRate;

    @ApiModelProperty("最大连胜")
    private Integer seqWinNum;

    @ApiModelProperty("总场次")
    private Long fightNum;

    @ApiModelProperty("总游戏时间")
    private Long totalSurvivalTime;

    @ApiModelProperty("综合评价")
    private String overallScore;

}
