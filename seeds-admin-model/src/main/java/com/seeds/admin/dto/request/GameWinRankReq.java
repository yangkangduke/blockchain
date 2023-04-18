package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author hang.yu
 * @date 2022/12/12
 */
@Data
@ApiModel(value = "GameWinRankReq", description = "胜场排行请求入参")
public class GameWinRankReq {

    @ApiModelProperty(value = "游戏id")
    @NotNull(message = "The game id can not be empty")
    private Long gameId = 1L;

    @ApiModelProperty(value = "排序方式 1 Total Score 2 Total Wins 3 Highest Streak 4 Game Played")
    private Integer sortType = 1;

    @ApiModelProperty(value = "表示起始行数（从0开始，包含）")
    private Integer startRow = 0;

    @ApiModelProperty(value = "表示结束行数（不包含）")
    private Integer endRow = 50;

}
