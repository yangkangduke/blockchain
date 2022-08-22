package com.seeds.uc.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 用户账号交易
 * </p>
 *
 * @author yk
 * @since 2022-07-26
 */
@Data
@Builder
public class NFTBuyReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("卖方用户id")
    private Long fromUserId;

    @ApiModelProperty("售卖金额")
    private BigDecimal amount;

    @ApiModelProperty("手续费")
    private BigDecimal fee;

    @ApiModelProperty("备注")
    private String comments;

    @ApiModelProperty("NFT的tokenId")
    private String tokenId;


}
