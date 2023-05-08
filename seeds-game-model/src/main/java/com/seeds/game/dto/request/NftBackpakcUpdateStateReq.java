package com.seeds.game.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author hewei
 * @date 2023/4/5
 */
@Data
public class NftBackpakcUpdateStateReq {

    @ApiModelProperty("mintAddress")
    @NotBlank(message = "nftId cannot be empty")
    private String mintAddress;

    @ApiModelProperty("state ：1 burned 2 LOCK (作为合成材料被临时锁定)，3 DEPOSITED (托管给平台) 4 UNDEPOSITED (未托管) 5 on shelf (固定价格售卖中) 6 拍卖中 ")
    @NotNull(message = "state cannot be empty")
    private Integer state;

    @ApiModelProperty("owner")
    private String owner;
}
