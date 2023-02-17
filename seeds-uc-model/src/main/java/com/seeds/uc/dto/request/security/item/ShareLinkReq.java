package com.seeds.uc.dto.request.security.item;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;


/**
 * @author: yang.deng
 * @since 2023-2-17
 * # twitter，instagram，facebook 入参
 */
@Data
public class ShareLinkReq {

    @ApiModelProperty(value = "twitter地址")
    @NotBlank
    private String twitter;

    @ApiModelProperty(value = "instagram地址")
    @NotBlank
    private String instagram;

    @ApiModelProperty(value = "facebook地址")
    @NotBlank
    private String facebook;

}
