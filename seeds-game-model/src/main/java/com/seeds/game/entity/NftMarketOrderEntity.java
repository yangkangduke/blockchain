package com.seeds.game.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@TableName("z_market_order")
@ApiModel(value = "NftMarketOrder对象", description = "Marketplace列表")
@Data
public class NftMarketOrderEntity {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("售卖人id")
    private Long sellerId;

    @ApiModelProperty("售卖人Solana地址")
    private String sellerAddress;

    @ApiModelProperty("买家id")
    private Long buyerId;

    @ApiModelProperty("买家地址")
    private String buyerAddress;

    @ApiModelProperty("Nft地址")
    private String mintAddress;

    @ApiModelProperty("nft编号")
    private Long tokenId;

    @ApiModelProperty("价格")
    private BigDecimal price;

    @ApiModelProperty("是否托管 0：否  1：是")
    private Integer isDeposit;

    @ApiModelProperty("状态：1，挂单中， 2，已成交  3，已取消")
    private Integer status;

    @ApiModelProperty("挂单收据")
    private String listReceipt;

    @ApiModelProperty("挂单签名")
    private String placeTx;

    @ApiModelProperty("执行交易签名")
    private String fulfillTx;

    @ApiModelProperty("取消交易签名")
    private String cancelTx;

    @ApiModelProperty("create_time")
    private Date createTime;

    @ApiModelProperty("place_time")
    private Date placeTime;

    @ApiModelProperty("fulfill_time")
    private Date fulfillTime;

    @ApiModelProperty("cancel_time")
    private Date cancelTime;
}
