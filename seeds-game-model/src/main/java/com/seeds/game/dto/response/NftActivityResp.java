package com.seeds.game.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


/**
 * @author hang.yu
 * @date 2023/03/22
 */

@Data
@ApiModel(value = "NftOfferResp")
public class NftActivityResp {

    @ApiModelProperty(value = "事件")
    private String event;

    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    @ApiModelProperty(value = "来自")
    private String from;

    @ApiModelProperty(value = "去往")
    private String to;

    @ApiModelProperty(value = "距离多久")
    private String date;

}
