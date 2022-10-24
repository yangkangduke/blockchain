package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
@ApiModel(value = "NftDeleteListReq", description = "删除NFT请求入参")
public class NftDeleteListReq  {

    @ApiModelProperty(value = "NFT的id")
    @NotNull(message = "The NFT id cannot be empty")
    private Long nftId;

    @ApiModelProperty("状态  0：正常  1：创建中  2：创建失败  5：删除中  6：删除失败")
    @NotNull(message = "NFT init status cannot be empty")
    private Integer initStatus;

    @ApiModelProperty("错误信息")
    private String errorMsg;

}
