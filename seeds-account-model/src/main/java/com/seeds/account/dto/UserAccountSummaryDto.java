package com.seeds.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author milo
 *
 */
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountSummaryDto implements Serializable {
    private static final long serialVersionUID = -1L;

    /**
     * 交易账户
     */
    @ApiModelProperty(value = "交易账户")
    List<UserAccountDto> accounts;

    @ApiModelProperty(value = "合计值")
    BigDecimal totalValue;
}
