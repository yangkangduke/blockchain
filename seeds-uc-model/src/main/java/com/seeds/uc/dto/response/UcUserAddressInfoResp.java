package com.seeds.uc.dto.response;

import com.seeds.uc.enums.CurrencyEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 用户地址
 * </p>
 *
 * @author yk
 * @since 2022-07-29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UcUserAddressInfoResp implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("primary key")
    private Long id;

    private CurrencyEnum currency;

    private String chain;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("备注")
    private String comments;


}
