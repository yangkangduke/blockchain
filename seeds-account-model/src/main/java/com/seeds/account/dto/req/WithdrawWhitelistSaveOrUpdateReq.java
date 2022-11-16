package com.seeds.account.dto.req;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
/**
 * <p>
 * 提币白名单新增/编辑
 * </p>
 *
 * @author dengyang
 * @since 2022-11-12
 */
@Data
@ApiModel(value = "WithdrawWhitelistSaveOrUpdateReq", description = "提币白名单新增/编辑")
public class WithdrawWhitelistSaveOrUpdateReq {
    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty("user id")
    private Long userId;

    @ApiModelProperty(value = "1：ETH 3：TRON")
    private Integer chain;

    @ApiModelProperty("币种")
    private String currency;

    @ApiModelProperty("max amount")
    private BigDecimal maxAmount;

    @ApiModelProperty("intrady amount")
    private BigDecimal intradayAmount;

    @ApiModelProperty("auto withdraw amount")
    private BigDecimal autoAmount;

    @ApiModelProperty("comments")
    private String comments;

    @ApiModelProperty("状态，1:启用 2：停用")
    private Integer status;
}
