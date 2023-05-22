package com.seeds.game.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;


/**
 * @author hang.yu
 * @date 2023/5/22
 */
@Data
@ApiModel(value = "GameRankNftSkinReq", description = "游戏NFT皮肤")
public class GameRankNftSkinReq {

    @ApiModelProperty(value = "表示结束行数")
    @Max(value = 200, message = "endRow:Maximum cannot exceed 200")
    private Integer endRow = 200;

}
