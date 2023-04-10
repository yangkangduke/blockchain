package com.seeds.game.dto.response;

import com.seeds.game.entity.NftAuctionHouseBiding;
import com.seeds.game.entity.NftAuctionHouseListing;
import com.seeds.game.entity.NftAuctionHouseSetting;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author hang.yu
 * @date 2023/04/08
 */

@Data
@ApiModel(value = "NftOfferDetailResp")
public class NftOfferDetailResp {

    @ApiModelProperty(value = "拍卖出价")
    private NftAuctionHouseBiding zauctionHouseBiding;

    @ApiModelProperty(value = "拍卖信息")
    private NftAuctionHouseListing zauctionHouseListing;

    @ApiModelProperty(value = "拍卖配置")
    private NftAuctionHouseSetting zauctionHouseSetting;

}
