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

    @ApiModelProperty("创建人")
    private Long createdBy;

    @ApiModelProperty("修改人")
    private Long updatedBy;

    @ApiModelProperty("上传时间")
    private Long createdAt;

    @ApiModelProperty("更新时间")
    private Long updatedAt;

}
