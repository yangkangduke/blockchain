package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "NftProperties", description = "NFT属性")
public class NftPropertiesReq {

    /**
     * NFT属性类别id
     */
    @ApiModelProperty("NFT属性类别id")
    @NotNull(message = "NFT properties type id cannot be empty")
    private Long typeId;

    /**
     * NFT属性值
     */
    @ApiModelProperty("NFT属性值")
    @NotBlank(message = "NFT properties value cannot be empty")
    private String value;


}