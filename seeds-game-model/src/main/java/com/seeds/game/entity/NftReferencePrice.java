package com.seeds.game.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * nft参考价
 * </p>
 *
 * @author hang.yu
 * @since 2023-06-25
 */
@TableName("ga_nft_reference_price")
@ApiModel(value = "NftReferencePrice", description = "nft参考价")
@Data
public class NftReferencePrice implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("游戏道具id")
    @TableId(value = "id")
    private Long id;

    @ApiModelProperty("游戏道具类型id")
    private Long typeId;

    @ApiModelProperty("登记")
    private Integer grade;

    @ApiModelProperty("编号")
    private String number;

    @ApiModelProperty("最近成交数")
    private Integer dealNum;

    @ApiModelProperty("最近成交总额")
    private BigDecimal totalPrice;

    @ApiModelProperty("参考单价")
    private BigDecimal referencePrice;

    @ApiModelProperty("平均单价")
    private BigDecimal averagePrice;

    @ApiModelProperty("创建时间")
    private Long createTime;

    @ApiModelProperty("修改时间")
    private Long updateTime;

}
