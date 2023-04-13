package com.seeds.game.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author hang.yu
 * @date 2023/03/22
 */

@Data
@ApiModel(value = "MarketPlaceNftDetailResp")
public class NftMarketPlaceDetailResp {

    @ApiModelProperty("商品id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;

    @ApiModelProperty("nft id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long nftId;

    @ApiModelProperty("NFT address")
    private String mintAddress;

    @ApiModelProperty("拍卖ID")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long auctionId;

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

    @ApiModelProperty("NFT拥有用户地址")
    private String ownerAddress;

    @ApiModelProperty("是否是拥有者，0 否 1 是")
    private Integer isOwner = 0;

    @ApiModelProperty("浏览量")
    private Integer views;

    @ApiModelProperty("NFT状态：0：UnDeposited  1：Deposited 2:On shelf 3:On auction 4:In settlement 5: Burned")
    private Integer state;

    @ApiModelProperty("当前价格")
    private BigDecimal currentPrice;

    @ApiModelProperty("参考价格")
    private BigDecimal referencePrice;

    @ApiModelProperty("NFT价格差异")
    private String priceDifference;

    @ApiModelProperty("NFT拍卖剩余时间")
    private String timeLeft;

    @ApiModelProperty("NFT动态属性")
    private Map attributes;

    @ApiModelProperty("NFT元数据")
    private Map metadata;

    @ApiModelProperty("合约地址")
    private String contractAddress;

    @ApiModelProperty("Token Id")
    private Long tokenId;

    @ApiModelProperty("角色id")
    private Long serverRoleId;

    @ApiModelProperty("游戏方autoId")
    private Long autoId;

    @ApiModelProperty("Token Standard")
    private String tokenStandard;

    @ApiModelProperty("Chain")
    private String chain;

    @ApiModelProperty("最后更改时间")
    private Long lastUpdated;

    @ApiModelProperty("创造者收益")
    private String creatorEarnings;

    @ApiModelProperty("挂单收据")
    private String listReceipt;

    @ApiModelProperty("NFT 关联账号：托管时候不为空；")
    private String tokenAddress;

    @ApiModelProperty("mint交易地址")
    private String mintTrx;

    @ApiModelProperty("类型：1装备 2道具 3英雄")
    private Integer type;

    @ApiModelProperty("图片")
    private String image;

}

