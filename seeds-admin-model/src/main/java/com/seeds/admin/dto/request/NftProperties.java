package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "NFT属性")
public class NftProperties {

    /**
     * NFT属性名称
     */
    @ApiModelProperty("NFT属性名称")
    @NotBlank(message = "NFT properties name cannot be empty")
    private String name;

    /**
     * NFT属性值
     */
    @ApiModelProperty("NFT属性值")
    @NotBlank(message = "NFT properties value cannot be empty")
    private String value;


}
