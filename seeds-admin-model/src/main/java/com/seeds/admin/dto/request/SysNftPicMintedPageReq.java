package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "SysNftPicMintedPageReq", description = "NFT上架分页请求入参")
public class SysNftPicMintedPageReq extends PageReq {

    @ApiModelProperty("mint时间")
    private String queryTime;

    @ApiModelProperty("0 未上架 1 已上架")
    private Integer listState;

    @ApiModelProperty("游戏内物品的唯一id")
    private Long autoId;

    @ApiModelProperty("游戏那边的conf_id")
    private Long confId;

    @ApiModelProperty("tokenAddress")
    private String tokenAddress;
}
