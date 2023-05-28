package com.seeds.game.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Admin
 * @date 2023/5/9 14:09
 * @description FeeMemoMessageResqDto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeeMemoMessageRespDto {

    @ApiModelProperty(value = "type")
    private String type;

    @ApiModelProperty(value = "orderId")
    private String mintAddress;

    @ApiModelProperty(value = "是否托管")
    private Boolean isDeposit;

    @ApiModelProperty(value = "burnNFTS")
    private List<String> burnNFTs;

    @ApiModelProperty(value = "placeTime: 小时")
    private Integer time;

    @ApiModelProperty(value = "上架价格")
    private BigDecimal price;

    @ApiModelProperty(value = "fee")
    private BigDecimal fee;

    @ApiModelProperty(value = "eventId")
    private Long eventId;
}
