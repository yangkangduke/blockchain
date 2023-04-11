package com.seeds.game.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author hang.yu
 * @date 2023/03/22
 */

@Data
@ApiModel(value = "NftGameAttrResp")
public class NftGameAttrResp {

    @ApiModelProperty("胜利场次")
    private Integer winsNum;

    @ApiModelProperty("失败场次")
    private Integer failedNum;

    @ApiModelProperty("平局次数")
    private Integer tiesNum;

    @ApiModelProperty("连胜次数")
    private Integer winStreak;

    @ApiModelProperty("连败次数")
    private Integer consecutiveDefeats;

    @ApiModelProperty("击杀玩家次数")
    private Integer playerKills;

    @ApiModelProperty("最多击杀次数")
    private Integer maxKills;

    @ApiModelProperty("击杀npc次数")
    private Integer npcKills;

    @ApiModelProperty("被玩家击杀次数")
    private Integer playerKilled;

    @ApiModelProperty("被npc击杀次数")
    private Integer npcKilled;

}
