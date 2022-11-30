package com.seeds.account.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.seeds.common.enums.CurrencyEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * NFT的反向拍卖
 * </p>
 *
 * @author hang.yu
 * @since 2022/10/11
 */
@TableName("ac_nft_reverse_auction")
@ApiModel(value = "NftReverseAuction", description = "NFT的反向拍卖")
@Data
@Builder
public class NftReverseAuction implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("primary key")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("NFT的id")
    private Long nftId;

    @ApiModelProperty("拥有NFT的用户id")
    private Long userId;

    @ApiModelProperty("初始最高价格")
    private BigDecimal price;

    @ApiModelProperty("单位")
    private CurrencyEnum currency;

    @ApiModelProperty("间隔时间")
    private Long intervalTime;

    @ApiModelProperty("间隔时间单位 m,h,d,s")
    private String intervalUnit;

    @ApiModelProperty("下降百分比")
    private BigDecimal dropPoint;

    @ApiModelProperty("create time")
    private Long createTime;

    @ApiModelProperty("update time")
    private Long updateTime;

}
