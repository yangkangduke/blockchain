package com.seeds.game.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "nftEventTypeNum")
@Data
public class EventTypeNum {

    @ApiModelProperty("事件类型 1.mint 2.compound 3.other")
    private Integer type;

    @ApiModelProperty("各事件对应的数量")
    private Integer num;
}
