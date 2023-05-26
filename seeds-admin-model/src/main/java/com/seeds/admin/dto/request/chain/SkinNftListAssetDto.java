package com.seeds.admin.dto.request.chain;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: hewei
 * @date 2023/5/5
 */
@Data
public class SkinNftListAssetDto {
    private String nftAddress;
    private BigDecimal price;
    private String auctionHouse;
    private Long id;
}
