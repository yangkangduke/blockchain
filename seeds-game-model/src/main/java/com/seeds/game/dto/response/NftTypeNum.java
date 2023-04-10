package com.seeds.game.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "NftTypeNum")
@Data
public class NftTypeNum {

    @ApiModelProperty("事件类型 1装备 2道具 3英雄")
    private Integer type;

    @ApiModelProperty("各事件对应的数量")
    private Integer num;
}
