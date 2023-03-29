package com.seeds.game.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "NftType")
@Data
public class NftType {

    @ApiModelProperty("name")
    private String name;

    @ApiModelProperty("image")
    private String image;

    @ApiModelProperty("itemTypeId")
    private Long itemTypeId;
}
