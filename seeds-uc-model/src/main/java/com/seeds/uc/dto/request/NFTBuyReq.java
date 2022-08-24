package com.seeds.uc.dto.request;

import com.seeds.uc.enums.AccountActionStatusEnum;
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

    @ApiModelProperty("admin中的NFT的id")
    private Long nftId;


}
