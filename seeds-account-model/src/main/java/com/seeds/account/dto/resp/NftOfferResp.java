package com.seeds.account.dto.resp;

import com.seeds.common.enums.CurrencyEnum;
import com.seeds.common.enums.NFTOfferStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author hang.yu
 * @date 2022/09/15
 */
@Data
@Builder
public class NftOfferResp {

    @ApiModelProperty("offer的id")
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户名称")
    private String userName;

    @ApiModelProperty("价格")
    private BigDecimal price;

    @ApiModelProperty("单位")
    private CurrencyEnum currency;

    @ApiModelProperty("状态 0：竞价中 1：已接受 2：已拒绝 3：已过期")
    private NFTOfferStatusEnum status;

    @ApiModelProperty("过期时间")
    private Long expireTime;

    @ApiModelProperty("差异")
    private BigDecimal difference;

    @ApiModelProperty("create time")
    private Long createTime;

}