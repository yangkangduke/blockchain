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
 * nft活动记录
 * </p>
 *
 * @author hang.yu
 * @since 2023-03-28
 */
@TableName("z_item_activity")
@ApiModel(value = "NftActivity", description = "nft活动记录")
@Data
public class NftActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("出价id(biding 表id)")
    private Long bidingId;

    @ApiModelProperty("NFT地址")
    private String mintAddress;

    @ApiModelProperty("事件: 0 mint, 1 list 2 offers 3 Sale 4 transfer")
    private String activityType;

    @ApiModelProperty("价格")
    private BigDecimal price;

    @ApiModelProperty("卖家")
    private String fromAddress;

    @ApiModelProperty("买家")
    private String toAddress;

    @ApiModelProperty("交易hash")
    private String txHash;

    @ApiModelProperty("创建时间")
    private Long createTime;

}
