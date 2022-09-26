package com.seeds.uc.dto.request;

import com.seeds.uc.enums.AccountActionStatusEnum;
import com.seeds.uc.enums.NFTOfferStatusEnum;
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
public class NFTBuyCallbackReq implements Serializable {

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

    @ApiModelProperty("NFT的tokenId")
    private String tokenId;

    @ApiModelProperty("admin中的NFT的id")
    private Long nftId;

    @ApiModelProperty("记录状态 1-进行中 2-成功 3-失败")
    @NotNull
    private AccountActionStatusEnum actionStatusEnum;

    @ApiModelProperty("记录id")
    @NotNull
    private Long actionHistoryId;

    @ApiModelProperty("offer的id")
    private Long offerId;

    @ApiModelProperty("状态 0：竞价中 1：已接受 2：已拒绝 3：已过期")
    private NFTOfferStatusEnum offerStatusEnum;

    @ApiModelProperty("发送地址")
    private String fromAddress;

    @ApiModelProperty("接受地址")
    private String toAddress;

    @ApiModelProperty("链")
    private String chain;

    @ApiModelProperty("哈希值")
    private String txHash;

    @ApiModelProperty("区块号")
    private Long blockNumber;

    @ApiModelProperty("区块的hash值")
    private String blockHash;
}
