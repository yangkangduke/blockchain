package com.seeds.admin.dto.request.chain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: hewei
 * @date 2023/5/8
 */
@Data
public class SkinNftCancelAuctionDto {

    @ApiModelProperty("拍卖id")
    private Long auctionId;
}
