package com.seeds.account.dto.req;

import com.seeds.common.dto.PageReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
@NoArgsConstructor
@AllArgsConstructor
public class AccountPendingTransactionsReq extends PageReq {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(hidden = true)
    private Long userId;
    @ApiModelProperty(value = "币种")
    private String currency;
    @ApiModelProperty(value = "1：充币 2：提币")
    private Integer action;
    @ApiModelProperty(hidden = true)
    private List<Integer> statusList;
    @ApiModelProperty(value = "开始时间")
    private Long startTime;
    @ApiModelProperty(value = "结束时间")
    private Long endTime;
    @ApiModelProperty(hidden = true)
    private boolean onlyManualCheck;
}
