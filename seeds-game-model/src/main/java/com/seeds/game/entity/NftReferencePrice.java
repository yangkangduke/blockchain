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

    @ApiModelProperty("主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("游戏道具id")
    private Long itemId;

    @ApiModelProperty("游戏道具类型id")
    private Long typeId;

    @ApiModelProperty("登记")
    private Integer grade;

    @ApiModelProperty("编号")
    private Integer number;

    @ApiModelProperty("参考价格")
    private BigDecimal referencePrice;

    @ApiModelProperty("平均价格")
    private BigDecimal averagePrice;

    @ApiModelProperty("创建时间")
    private Long createTime;

    @ApiModelProperty("修改时间")
    private Long updateTime;

}
