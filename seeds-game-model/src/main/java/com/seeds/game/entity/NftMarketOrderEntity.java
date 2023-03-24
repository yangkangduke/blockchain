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
    private String id;

    @ApiModelProperty("seller_id")
    private Long sellerId;

    @ApiModelProperty("seller_address")
    private String sellerAddress;

    @ApiModelProperty("buyer_id")
    private Long buyerId;

    @ApiModelProperty("buyer_address")
    private String buyerAddress;

    @ApiModelProperty("token_address")
    private String tokenAddress;

    @ApiModelProperty("token_id")
    private Long tokenId;

    @ApiModelProperty("price")
    private BigDecimal price;

    @ApiModelProperty("is_deposit")
    private Integer isDeposit;

    @ApiModelProperty("status")
    private Integer status;

    @ApiModelProperty("挂单收据")
    private String listReceipt;

    @ApiModelProperty("place_tx")
    private String placeTx;

    @ApiModelProperty("fulfill_tx")
    private String fulfillTx;

    @ApiModelProperty("cancel_tx")
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
