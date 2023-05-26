package com.seeds.admin.dto.request.chain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: hewei
 * @date 2023/5/8
 */
@Data
public class SkinNftCancelAssetDto {
    @ApiModelProperty("上架收据")
    private String listReceipt;

    private String auctionHouse;

    private Long id;
}
