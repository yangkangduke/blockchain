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
 * nft拍卖出价
 * </p>
 *
 * @author hang.yu
 * @since 2023-03-29
 */
@TableName("z_auction_house_biding")
@ApiModel(value = "NftAuctionHouseBiding", description = "nft拍卖出价")
@Data
public class NftAuctionHouseBiding implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("拍卖订单Id")
    private Long auctionId;

    @ApiModelProperty("拍卖合约地址")
    private String auctionHouseAddress;

    @ApiModelProperty("创建时间")
    private Long createTime;

    @ApiModelProperty("取消时间")
    private Long cancelTime;

    @ApiModelProperty("nft 名称")
    private String name;

    @ApiModelProperty("nft 图片/metadata 地址")
    private String url;

    @ApiModelProperty("NFT地址")
    private String mintAddress;

    @ApiModelProperty("NFT holder 地址（非owner地址）")
    private String tokenAddress;

    @ApiModelProperty("签名")
    private String signature;

    @ApiModelProperty("收据")
    private String receipt;

    @ApiModelProperty("出价者")
    private String buyer;

    @ApiModelProperty("出价")
    private BigDecimal price;

    @ApiModelProperty("币种")
    private String currency;

}
