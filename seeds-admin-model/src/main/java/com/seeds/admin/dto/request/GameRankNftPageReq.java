package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;


/**
 * @author hang.yu
 * @date 2023/5/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "GameRankNftPageReq", description = "游戏NFT排名")
public class GameRankNftPageReq extends PageReq {

    @ApiModelProperty(value = "排序方式 1 Price 2 View")
    private Integer sortField = 1;

    @ApiModelProperty(value = "排序类型 0 :升序 1:降序")
    private Integer sortType = 1;

    @ApiModelProperty("NFT类型 1 Equip 2 Item 3 Hero")
    @NotNull(message = "NFT type cannot be empty")
    private Integer nftType;

}
