package com.seeds.game.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 游戏角色的英雄统计
 *
 * @author hang.yu
 * @date 2023/02/08
 */
@Data
@ApiModel(value = "ServerRoleHeroResp")
public class ServerRoleHeroStatisticsResp {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("胜率")
    private String winRate;

    @ApiModelProperty("熟练度")
    private Long proficiencyLvl;

    @ApiModelProperty("成长度")
    private Integer heroLvl;

    @ApiModelProperty("血腥度")
    private Long killNum;

    @ApiModelProperty(value = "英雄总分（排名时使用）")
    private Long score;

}
