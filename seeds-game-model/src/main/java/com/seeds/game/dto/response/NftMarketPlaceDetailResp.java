package com.seeds.game.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author hang.yu
 * @date 2023/03/22
 */

@Data
@ApiModel(value = "MarketPlaceNftDetailResp")
public class NftMarketPlaceDetailResp {

    @ApiModelProperty("NFT名称")
    private String name;

    @ApiModelProperty("NFT编号")
    private String number;

    @ApiModelProperty("NFT描述")
    private String desc;

    @ApiModelProperty("NFT拥有用户id")
    private Long ownerId;

    @ApiModelProperty("NFT拥有用户名称")
    private String ownerName;

    @ApiModelProperty("点赞数量")
    private Integer favorite;

    @ApiModelProperty("浏览量")
    private Integer views;

    @ApiModelProperty("NFT交易模式：0：Buy Now  1：On Auction")
    private Integer model;

    @ApiModelProperty("NFT状态：0：UnDeposited  1：Deposited")
    private Integer state;

    @ApiModelProperty("NFT一口价价格")
    private BigDecimal maxPrice;

    @ApiModelProperty("NFT地板价格")
    private BigDecimal floorPrice;

    @ApiModelProperty("NFT当前价格")
    private BigDecimal currentPrice;

    @ApiModelProperty("NFT价格差异")
    private String priceDifference;

    @ApiModelProperty("NFT拍卖剩余时间")
    private String timeLeft;

    @ApiModelProperty("NFT动态属性")
    private String attributes;

    @ApiModelProperty("NFT元数据")
    private String metadata;

    @ApiModelProperty("合约地址")
    private String contractAddress;

    @ApiModelProperty("Token Id")
    private String tokenId;

    @ApiModelProperty("Token Standard")
    private String tokenStandard;

    @ApiModelProperty("Chain")
    private String chain;

    @ApiModelProperty("最后更改时间")
    private String lastUpdated;

    @ApiModelProperty("创造者收益")
    private String creatorEarnings;

}

