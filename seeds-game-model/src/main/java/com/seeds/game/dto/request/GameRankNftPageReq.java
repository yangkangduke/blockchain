package com.seeds.game.dto.request;

import com.seeds.admin.dto.request.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;


/**
 * @author hang.yu
 * @date 2023/5/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "GameRankNftPageReq", description = "游戏NFT排名")
public class GameRankNftPageReq extends PageReq {

    @ApiModelProperty(value = "排序方式 1 price 2 view 3 slaying 4 goblinKill 5 capture 6 killingSpree 7 maxStreak 8 lose 9 victory")
    private Integer sortField = 1;

    @ApiModelProperty(value = "排序类型 0 :升序 1:降序")
    private Integer sortType = 1;

    @ApiModelProperty(value = "种类标识")
    @NotBlank(message = "Item can not be empty!")
    private String item;

    private String sortTypeStr;

    public static String convert(Integer sortType) {
        String str = "desc";
        if (sortType.equals(0)) {
            str = "asc";
        }
        return str;
    }

}
