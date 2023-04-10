package com.seeds.game.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author hang.yu
 * @date 2023/3/24
 */
@Data
public class NftSaleSuccessReq {

    @ApiModelProperty("接受的拍卖出价id")
    @NotNull(message = "Biding id cannot be empty")
    private Long bidingId;

    @ApiModelProperty("收据")
    @NotBlank(message = "Receipt cannot be empty")
    private String receipt;

    @ApiModelProperty("签名")
    @NotBlank(message = "Signature cannot be empty")
    private String sig;

}
