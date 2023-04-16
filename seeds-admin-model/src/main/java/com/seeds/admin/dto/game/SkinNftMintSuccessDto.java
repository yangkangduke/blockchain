package com.seeds.admin.dto.game;

import lombok.Data;

/**
 * @author: hewei
 * @date 2023/4/15
 */
@Data
public class SkinNftMintSuccessDto {
    private Long autoId;
    private Long configId;
    private String tokenAddress;
    private String rarity;
}
