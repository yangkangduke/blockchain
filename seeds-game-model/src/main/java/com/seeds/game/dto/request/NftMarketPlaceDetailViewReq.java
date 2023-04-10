package com.seeds.game.dto.request;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author dengyang
 * @date 2023/3/24
 */
@Data
@ApiModel(value = "NftMarketPlaceDetailViewReq", description = "NFT的浏览")
public class NftMarketPlaceDetailViewReq {

    @ApiModelProperty("nftId")
    private Long nftId;

    private Long userId;
}
