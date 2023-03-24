package com.seeds.game.dto.request;

import com.seeds.common.dto.PageReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author hang.yu
 * @since 2023-03-24
 */
@Data
public class NftOfferPageReq extends PageReq {

    @ApiModelProperty("nft地址")
    @NotBlank(message = "Mint Address cannot be empty")
    private String mintAddress;
}
