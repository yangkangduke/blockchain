package com.seeds.game.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@EqualsAndHashCode(callSuper = true)
public class ServerRoleHeroStatisticsEntity extends BaseEntity {

    @ApiModelProperty("游戏角色id")
    private Long roleId;

    @ApiModelProperty("游戏英雄id")
    private Long heroId;

    @ApiModelProperty("游戏服id")
    private String gameServerId;

    @ApiModelProperty("胜率")
    private BigDecimal winRate;

    @ApiModelProperty("熟练度")
    private Long proficiencyLvl;

    @ApiModelProperty("天赋等级")
    private Integer heroLvl;

    @ApiModelProperty("击杀其他玩家次数")
    private Long killNum;

    @ApiModelProperty(value = "英雄总分（排名时使用）")
    private BigDecimal totalScore;

}
