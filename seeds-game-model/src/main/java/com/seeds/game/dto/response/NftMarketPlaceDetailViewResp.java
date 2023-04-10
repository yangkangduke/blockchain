package com.seeds.game.dto.response;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author dengyang
 * @date 2023/3/24
 */
@Data
@ApiModel(value = "NftMarketPlaceDetailViewResp", description = "NFT的浏览")
public class NftMarketPlaceDetailViewResp {

    @ApiModelProperty("浏览量")
    private Integer views = 0;
}
