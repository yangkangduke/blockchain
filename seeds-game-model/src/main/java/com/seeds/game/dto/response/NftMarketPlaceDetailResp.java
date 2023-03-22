package com.seeds.game.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

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

    @ApiModelProperty("NFT游戏内部属性")
    private NftGameAttrResp gameAttr;

    @ApiModelProperty("NFT链相关信息")
    private NftChainDetailResp chainDetail;

    @ApiModelProperty("NFT出价列表")
    private List<NftOfferResp> offers;

}

