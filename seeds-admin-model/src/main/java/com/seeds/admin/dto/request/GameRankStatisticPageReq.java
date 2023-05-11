package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;


/**
 * @author hang.yu
 * @date 2023/5/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "GameRankStatisticPageReq", description = "游戏排名统计")
public class GameRankStatisticPageReq extends PageReq {

    @ApiModelProperty(value = "排序方式 1 Ladder Tournament 2 Game Played 3 Win Rate 4 Total Wins 5Highest  Streak 6 Killing Spree")
    private Integer sortField = 1;

    @ApiModelProperty(value = "排序类型 0 :升序 1:降序")
    private Integer sortType = 1;

    @ApiModelProperty("游戏服id")
    @NotNull(message = "gameServerId cannot be empty")
    private Long gameServerId;

}
