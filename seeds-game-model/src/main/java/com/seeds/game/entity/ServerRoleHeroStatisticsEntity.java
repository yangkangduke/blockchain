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
 * 游戏服角色的英雄统计
 * </p>
 *
 * @author hang。yu
 * @since 2023-02-08
 */
@TableName("ga_server_role_hero_statistics")
@ApiModel(value = "ServerRoleHeroStatistics对象", description = "游戏服角色的英雄统计")
@Data
public class ServerRoleHeroStatisticsEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("游戏角色id")
    private Long roleId;

    @ApiModelProperty("游戏英雄id")
    private Long heroId;

    @ApiModelProperty("击杀次数")
    private Long killing;

    @ApiModelProperty("击杀评分")
    private BigDecimal killingRate;

    @ApiModelProperty("伤害值")
    private Long damage;

    @ApiModelProperty("伤害评分")
    private BigDecimal damageRate;

    @ApiModelProperty("存活次数")
    private Long survival;

    @ApiModelProperty("存活评分")
    private BigDecimal survivalRate;

    @ApiModelProperty("采集次数")
    private Long framing;

    @ApiModelProperty("采集评分")
    private BigDecimal framingRate;

    @ApiModelProperty("移动次数")
    private Long movement;

    @ApiModelProperty("移动得分")
    private BigDecimal movementRate;

    @ApiModelProperty("胜率")
    private BigDecimal winningRate;

    @ApiModelProperty("创建人")
    private Long createdBy;

    @ApiModelProperty("修改人")
    private Long updatedBy;

    @ApiModelProperty("上传时间")
    private Long createdAt;

    @ApiModelProperty("更新时间")
    private Long updatedAt;

}
