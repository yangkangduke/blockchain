package com.seeds.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * nft拍卖上架
 * </p>
 *
 * @author hang.yu
 * @since 2023-03-29
 */
@TableName("z_auction_house_listing")
@ApiModel(value = "NftAuctionHouseListing", description = "nft拍卖上架")
@Data
public class NftAuctionHouseListing implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("拍卖合约地址")
    private String auctionHouseAddress;

    @ApiModelProperty("创建时间")
    private Long createTime;

    @ApiModelProperty("取消时间")
    private Long canceledTime;

    @ApiModelProperty("tokenId")
    private String name;

    @ApiModelProperty("NFT链接")
    private String url;

    @ApiModelProperty("NFT地址")
    private String mintAddress;

    @ApiModelProperty("NFT holder 地址（非owner地址）")
    private String tokenAddress;

    @ApiModelProperty("签名")
    private String signature;

    @ApiModelProperty("挂单者")
    private String seller;

    @ApiModelProperty("收据")
    private String receipt;

    @ApiModelProperty("价格")
    private BigDecimal price;

    @ApiModelProperty("币种")
    private String currency;

}
