package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hang.yu
 * @date 2022/8/19
 */
@Data
@ApiModel(value = "UcNftPageReq", description = "NFT分页请求入参")
public class UcNftPageReq extends PageReq {

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "游戏id")
    private Long gameId;

    @ApiModelProperty(value = "用户账户id")
    private Long accountId;

    @ApiModelProperty(value = "NFT名称")
    private String name;

    @ApiModelProperty(value = "NFT类别id")
    private Long nftTypeId;

    @ApiModelProperty(value = "是否在售  0：否   1：是")
    private Integer status;

}
