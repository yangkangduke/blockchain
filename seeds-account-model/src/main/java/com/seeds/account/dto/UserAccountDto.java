package com.seeds.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

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
public class UserAccountDto implements Serializable {
    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value = "用户id")
    long userId;
    @ApiModelProperty(value = "币种")
    String currency;
    @ApiModelProperty(value = "余额")
    BigDecimal available;
    @ApiModelProperty(value = "冻结")
    BigDecimal freeze;
    @ApiModelProperty(value = "locked")
    BigDecimal locked;
    @ApiModelProperty(value = "价格")
    BigDecimal price;
    @ApiModelProperty(value = "值")
    BigDecimal totalValue;

    @JsonIgnore
    @ApiModelProperty(value = "版本")
    long version;
}
