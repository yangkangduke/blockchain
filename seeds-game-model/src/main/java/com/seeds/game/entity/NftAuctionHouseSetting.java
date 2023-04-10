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
 * nft拍卖配置
 * </p>
 *
 * @author hang.yu
 * @since 2023-03-29
 */
@TableName("z_auction_house_setting")
@ApiModel(value = "NftAuctionHouseSetting", description = "nft拍卖配置")
@Data
public class NftAuctionHouseSetting implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("挂单Id(listing 表id)")
    private Long listingId;

    @ApiModelProperty("出价id(biding 表id)")
    private Long bidingId;

    @ApiModelProperty("拍卖合约地址")
    private String auctionHouseAddress;

    @ApiModelProperty("中标人排序：0 highest bidder")
    private Integer bidder;

    @ApiModelProperty("开始时间")
    private Long startTime;

    @ApiModelProperty("持续时间")
    private Long duration;

    @ApiModelProperty("开始价格")
    private BigDecimal startPrice;

    @ApiModelProperty("结束价格")
    private BigDecimal endPrice;

    @ApiModelProperty("拍卖方式：0 English 1 Dutch")
    private Integer method;

    @ApiModelProperty("成交收据")
    private String purchaseReceipt;

    @ApiModelProperty("创建时间")
    private Long createTime;

    @ApiModelProperty("取消时间")
    private Long cancelTime;

    @ApiModelProperty("更新时间")
    private Long updateTime;

    @ApiModelProperty("拍卖是否结束： 0 没，1 结束")
    private Integer isFinished;

}
