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
    @Pattern(regexp = "/^((?:http:\\/\\/)?|(?:https:\\/\\/)?)?(?:www\\.)?twitter\\.com\\/(\\w+)$/", message = "Invalid Link")
    private String twitter;

    @ApiModelProperty(value = "instagram地址")
    @Pattern(regexp = "/^((?:http:\\/\\/)?|(?:https:\\/\\/)?)?(?:www\\.)?instagram\\.com\\/(\\w+)$/", message = "Invalid Link")
    private String instagram;

    @ApiModelProperty(value = "facebook地址")
    @Pattern(regexp = "/^((?:http:\\/\\/)?|(?:https:\\/\\/)?)?(?:www\\.)?facebook\\.com\\/(\\w+)$/", message = "Invalid Link")
    private String facebook;

}
