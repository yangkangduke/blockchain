package com.seeds.game.dto.response;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


/**
 * @author hang.yu
 * @date 2023/03/22
 */

@Data
@ApiModel(value = "NftOfferRespNew")
public class NftOfferResp {

    @ApiModelProperty(value = "最高出价")
    private BigDecimal highestOffer;

    @ApiModelProperty(value = "拍卖出价列表")
    private IPage<NftOffer> nftOffers;

    @Data
    @ApiModel(value = "NftOfferNew", description = "拍卖出价")
    public static class NftOffer {

        @ApiModelProperty("offerId")
        @JsonSerialize(using= ToStringSerializer.class)
        private Long id;

        @ApiModelProperty("拍卖订单Id")
        @JsonSerialize(using= ToStringSerializer.class)
        private Long auctionId;

        @ApiModelProperty("收据")
        private String receipt;

        @ApiModelProperty("拍卖合约地址")
        private String auctionHouseAddress;

        @ApiModelProperty("出价价格")
        private BigDecimal price;

        @ApiModelProperty("美元价格")
        private String usdPrice;

        @ApiModelProperty("价格差异")
        private String difference;

        @ApiModelProperty("出价人")
        private String buyer;

        @ApiModelProperty("状态")
        private String status;

        @ApiModelProperty("签名")
        private String signature;

        @ApiModelProperty("是否是出价人，0 否 1 是")
        private Integer isBidder;

        @ApiModelProperty("取消时间")
        private Long cancelTime;

    }

}
