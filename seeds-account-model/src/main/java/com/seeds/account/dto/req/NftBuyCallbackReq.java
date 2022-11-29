package com.seeds.account.dto.req;

import com.seeds.account.enums.CommonActionStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
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
public class NftBuyCallbackReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("卖方用户id")
    private Long fromUserId;

    @NotNull
    @ApiModelProperty("买方用户id")
    private Long toUserId;

    @NotNull
    @ApiModelProperty("NFT 归属方类型  0：平台  1：uc用户")
    private Integer ownerType;

    @ApiModelProperty("售卖金额")
    @NotNull
    private BigDecimal amount;

    @ApiModelProperty("手续费")
    private BigDecimal fee;

    @ApiModelProperty("备注")
    private String comments;

    @ApiModelProperty("记录状态 1-成功 2-失败 3-进行中")
    @NotNull
    private CommonActionStatus actionStatusEnum;

    @ApiModelProperty("记录id")
    @NotNull
    private Long actionHistoryId;

    @ApiModelProperty("币种")
    private String currency;

}
