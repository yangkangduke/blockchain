package com.seeds.account.dto.req;

import com.seeds.common.dto.PageReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class AccountHistoryReq extends PageReq {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(hidden = true)
    private Long userId;
    @ApiModelProperty(value = "开始时间")
    Long startTime;
    @ApiModelProperty(value = "结束时间")
    Long endTime;
    @ApiModelProperty(value = "币种")
    String currency;
    @ApiModelProperty(value = "1：充币 2：提币")
    Integer action;
}
