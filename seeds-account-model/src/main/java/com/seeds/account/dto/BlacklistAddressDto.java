package com.seeds.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 充提币黑名单
 *
 * @author yk
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlacklistAddressDto implements Serializable {
    private static final long serialVersionUID = -1L;
    private Long id;

    @ApiModelProperty(value = "1；冲币 2：提币")
    private Integer type;

    @NotNull
    private Long userId;

    @NotNull
    @ApiModelProperty("address")
    private String address;

    @NotNull
    @ApiModelProperty("reason")
    private String reason;

    @ApiModelProperty(value = "1:启用 2：停用")
    private Integer status;
    @ApiModelProperty(value = "1：ETH 3：TRON")
    private Integer chain;
}
