package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "NftPropertiesValueReq", description = "NFT属性")
public class NftPropertiesValueModifyReq {

    /**
     * NFT属性id
     */
    @ApiModelProperty("NFT属性id")
    @NotNull(message = "NFT properties id cannot be empty")
    private Long id;

    /**
     * NFT属性值
     */
    @ApiModelProperty("NFT属性值")
    @NotBlank(message = "NFT properties value cannot be empty")
    private String value;

}
