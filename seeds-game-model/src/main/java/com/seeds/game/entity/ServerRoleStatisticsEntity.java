package com.seeds.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
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
public class ServerRoleStatisticsEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("游戏角色id")
    private Long roleId;

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
    private BigDecimal winRate;

    @ApiModelProperty("最大连胜")
    private Integer seqWinNum;

    @ApiModelProperty("总场次")
    private Long fightNum;

    @ApiModelProperty("总游戏时间")
    private Long totalSurvivalTime;

    @ApiModelProperty("综合评价")
    private String overallScore;

    @ApiModelProperty("创建人")
    private Long createdBy;

    @ApiModelProperty("修改人")
    private Long updatedBy;

    @ApiModelProperty("上传时间")
    private Long createdAt;

    @ApiModelProperty("更新时间")
    private Long updatedAt;

}
