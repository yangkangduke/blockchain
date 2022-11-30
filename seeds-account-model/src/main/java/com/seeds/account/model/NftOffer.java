package com.seeds.account.model;

import com.baomidou.mybatisplus.annotation.*;
import com.seeds.common.enums.CurrencyEnum;
import com.seeds.common.enums.NFTOfferStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * NFT的Offer
 * </p>
 *
 * @author hang.yu
 * @since 2022/09/05
 */
@TableName("ac_nft_offer")
@ApiModel(value = "NftOffer对象", description = "NFT的Offer")
@Data
@Builder
public class NftOffer implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("primary key")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("NFT的id")
    private Long nftId;

    @ApiModelProperty("出价用户id")
    private Long userId;

    @ApiModelProperty("账号交易历史记录id")
    private Long actionHistoryId;

    @ApiModelProperty("价格")
    private BigDecimal price;

    @ApiModelProperty("单位")
    private CurrencyEnum currency;

    @ApiModelProperty("状态")
    private NFTOfferStatusEnum status;

    @ApiModelProperty("过期时间")
    private Long expireTime;

    @ApiModelProperty("差异")
    private BigDecimal difference;

    @ApiModelProperty("删除标记  0：未删除")
    @TableLogic(value = "0", delval = "NULL")
    private Integer deleteFlag;

    @ApiModelProperty("create time")
    private Long createTime;

    @ApiModelProperty("update time")
    private Long updateTime;

}
