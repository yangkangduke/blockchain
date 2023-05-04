package com.seeds.uc.dto.request.security.item;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;


/**
 * @author: yang.deng
 * @since 2023-2-17
 * # twitter，instagram，facebook 入参
 */
@Data
public class ShareLinkReq {

    @ApiModelProperty(value = "twitter地址")
    @Pattern(regexp = "^https?://(?:www\\.)?twitter\\.com/([a-zA-Z0-9_]+)/?$|^$", message = "Invalid Link")
    private String twitter;

    @ApiModelProperty(value = "instagram地址")
    @Pattern(regexp = "^https?://(?:www\\.)?instagram\\.com/([a-zA-Z0-9_.]+)/?$|^$", message = "Invalid Link")
    private String instagram;

    @ApiModelProperty(value = "facebook地址")
    @Pattern(regexp = "^https?://(?:www\\.)?facebook\\.com/([a-zA-Z0-9.]+)/?$|^$", message = "Invalid Link")
    private String facebook;

}
