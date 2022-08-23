package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "NftPropertiesValueReq", description = "NFT属性")
public class NftOwnerChangeReq {

    /**
     * NFT的id
     */
    @ApiModelProperty("NFT的id")
    @NotNull(message = "NFT id cannot be empty")
    private Long id;

    /**
     * NFT的归属人id
     */
    @ApiModelProperty("NFT的归属人id")
    @NotNull(message = "NFT owner id cannot be empty")
    private Long ownerId;

    /**
     * NFT的归属人名称
     */
    @ApiModelProperty("NFT的归属人名称")
    @NotBlank(message = "NFT owner name cannot be empty")
    private String ownerName;

    @ApiModelProperty("记录id")
    @NotNull(message = "NFT actionHistory id cannot be empty")
    private Long actionHistoryId;

    @ApiModelProperty("买家地址")
    @NotBlank(message = "NFT buyers address cannot be empty")
    private String toAddress;

}
