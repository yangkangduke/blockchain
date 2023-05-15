package com.seeds.game.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "SkinNftType")
@Data
public class SkinNftTypeResp {

    @ApiModelProperty("职业")
    private String profession;

    @ApiModelProperty("skinName")
    private String skinName;

    @ApiModelProperty("image")
    private String image;

}
