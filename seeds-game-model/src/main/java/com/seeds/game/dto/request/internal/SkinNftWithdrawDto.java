package com.seeds.game.dto.request.internal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: hewei
 * @date 2023/5/13
 */
@Data
public class SkinNftWithdrawDto {
    private Long nftPicId;
    @ApiModelProperty("获胜次数")
    private int victory;

    @ApiModelProperty("失败次数")
    private int lose;

    @ApiModelProperty("最大连胜场数")
    private int maxStreak;

    @ApiModelProperty("最大连败场数")
    private int maxLose;

    @ApiModelProperty("击杀玩家数")
    private int capture;

    @ApiModelProperty("最大连杀数")
    private int killingSpree;

    @ApiModelProperty("击杀NPC数")
    private int goblinKill;

    @ApiModelProperty("被玩家击杀数")
    private int slaying;

    @ApiModelProperty("被NPC击杀数")
    private int goblin;
}
