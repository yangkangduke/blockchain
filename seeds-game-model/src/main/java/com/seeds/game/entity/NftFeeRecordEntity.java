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
 * nft托管费记录
 * </p>
 *
 * @author hang.yu
 * @since 2023-04-19
 */
@TableName("ga_nft_fee_record")
@ApiModel(value = "NftFeeRecord", description = "nft托管费记录")
@Data
public class NftFeeRecordEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("order id")
    private Long orderId;

    @ApiModelProperty("auction id")
    private Long auctionId;

    @ApiModelProperty("应收托管费")
    private BigDecimal receivableFee;

    @ApiModelProperty("退还托管费")
    private BigDecimal refundFee;

    @ApiModelProperty("币种")
    private String currency;

    @ApiModelProperty("退还时间")
    private Long refundTime;

    @ApiModelProperty("0 失败 1 成功")
    private Integer status;

    @ApiModelProperty("退还地址")
    private String toAddress;

    @ApiModelProperty("托管费hash")
    private String feeHash;

    @ApiModelProperty("NFT铸造地址")
    private String mintAddress;

    @ApiModelProperty("退费hash")
    private String txHash;

}
