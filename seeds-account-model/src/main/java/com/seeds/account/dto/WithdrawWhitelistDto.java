package com.seeds.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.seeds.account.enums.CommonStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 提币白名单
 *
 * @author yk
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawWhitelistDto implements Serializable {
    private static final long serialVersionUID = -1L;

    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty(value = "1：ETH 3：TRON")
    private  Integer chain;

    @ApiModelProperty("create time")
    private Long createTime;

    @ApiModelProperty("update time")
    private Long updateTime;

    @ApiModelProperty("币种")
    private String currency;

    @ApiModelProperty("最大额度")
    private BigDecimal maxAmount;

    @ApiModelProperty("单日额度")
    private BigDecimal intradayAmount;

    @ApiModelProperty("自动额度，不用走人工审批的额度")
    private BigDecimal autoAmount;

    @ApiModelProperty("评语")
    private String comments;

    @ApiModelProperty("状态，1:启用 2：停用")
    private Integer status;
}
