package com.seeds.account.dto.req;

import com.seeds.account.enums.CommonStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 充提黑名单新增/编辑
 * </p>
 *
 * @author dengyang
 * @since 2022-11-11
 */
@Data
@ApiModel(value = "BlackListSaveOrUpdateReq", description = "黑名单列表新增/编辑")
public class BlackListAddressSaveOrUpdateReq {

    private static final long serialVersionUID = 1L;
    private Long id;

    @ApiModelProperty(value = "1：ETH 3：TRON")
    private Integer chain;

    @ApiModelProperty(value = "1；冲币 2：提币")
    private Integer type;

    @ApiModelProperty("address")
    private String address;

    @ApiModelProperty("reason")
    private String reason;

    @ApiModelProperty(value = "1:启用 2：停用")
    private Integer status;

}
