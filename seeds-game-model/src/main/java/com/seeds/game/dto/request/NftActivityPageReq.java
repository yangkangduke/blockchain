package com.seeds.game.dto.request;

import com.seeds.common.dto.PageReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author hang.yu
 * @since 2023-03-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NftActivityPageReq extends PageReq {

    @ApiModelProperty("Nft id")
    @NotNull(message = "Nft id cannot be empty")
    private Long nftId;

    private String mintAddress;

}
