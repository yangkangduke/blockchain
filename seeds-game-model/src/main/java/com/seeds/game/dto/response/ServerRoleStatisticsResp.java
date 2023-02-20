package com.seeds.game.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


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
    private Integer killScore;

    @ApiModelProperty("生存积分")
    private Integer survivalScore;

    @ApiModelProperty("生产积分")
    private Integer drawingScore;

    @ApiModelProperty("天梯积分")
    private Integer ladderScore;

    @ApiModelProperty("竞技场积分")
    private Integer rankScore;

    @ApiModelProperty("专业积分")
    private Integer professionalScore;

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
