package com.seeds.account.dto.req;

import com.seeds.common.dto.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>
 * 链上交易分页请求入参
 * </p>
 *
 * @author hang.yu
 * @since 2022-10-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ChainTxnPageReq", description = "链上交易分页请求入参")
public class ChainTxnPageReq extends PageReq {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "类型 1：提币")
    private Integer type;
    @ApiModelProperty(value = "状态 1：待处理 6: 已确认")
    private Integer status;
    @ApiModelProperty(value = "链 1：eth 3: tron")
    private Integer chain;

}
