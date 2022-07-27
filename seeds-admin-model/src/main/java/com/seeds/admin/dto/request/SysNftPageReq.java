package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hang.yu
 * @date 2022/7/22
 */
@Data
@ApiModel(value = "SysNftPageReq", description = "NFT分页请求入参")
public class SysNftPageReq extends PageReq {

    @ApiModelProperty(value = "NFT名称")
    private String name;

    @ApiModelProperty(value = "游戏id")
    private Long gameId;

    @ApiModelProperty(value = "NFT类别id")
    private Long nftTypeId;

    @ApiModelProperty(value = "是否在售  0：否   1：是")
    private Integer status;

}
