package com.seeds.game.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * <p>
 * 游戏服角色统计
 * </p>
 *
 * @author hang。yu
 * @since 2023-02-08
 */
@TableName("ga_server_role_statistics")
@ApiModel(value = "ServerRoleStatistics对象", description = "游戏服角色统计")
@Data
@EqualsAndHashCode(callSuper = true)
public class ServerRoleStatisticsEntity extends BaseEntity {

    @ApiModelProperty("游戏角色id")
    private Long roleId;

    @ApiModelProperty("游戏服id")
    private Long gameServerId;

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
    private BigDecimal winRate;

    @ApiModelProperty("最大连胜")
    private Integer seqWinNum;

    @ApiModelProperty("总场次")
    private Long fightNum;

    @ApiModelProperty("总游戏时间")
    private Long totalSurvivalTime;

    @ApiModelProperty("综合评价")
    private String overallScore;

    @ApiModelProperty("获胜场次")
    private Long winNum;

    @ApiModelProperty("最大击杀数")
    private Integer maxKillNum;

}
