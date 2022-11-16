package com.seeds.account.dto.req;

import com.seeds.common.enums.Chain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * <p>
 * 黑地址请求req
 * </p>
 *
 * @author dengyang
 * @since 2022-11-11
 */
@Data
@ApiModel(value = "BlackListPageReq", description = "黑地址请求req")
public class BlackListAddressReq {

    private static final long serialVersionUID = 1L;

    /**
     * @see Chain
     */
    @ApiModelProperty(value = "链 1：eth 3: tron")
    private Integer chain;

    @ApiModelProperty(value = "1；冲币 2：提币")
    private Integer type;
}
