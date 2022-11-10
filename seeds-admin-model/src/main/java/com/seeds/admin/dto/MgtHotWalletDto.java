package com.seeds.admin.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MgtHotWalletDto {

    @ApiModelProperty(value = "地址")
    private String address;
    @ApiModelProperty(value = "类型：1 热钱包")
    private int type;
    @ApiModelProperty(value = "链")
    private String chain;
    @ApiModelProperty(value = "链余额")
    private String chainBalance;
    @ApiModelProperty(value = "链数量")
    private BigDecimal chainBalanceNum;
    @ApiModelProperty(value = "usdt余额")
    private String usdtBalance;
    @ApiModelProperty(value = "usdt数量")
    private BigDecimal usdtBalanceNum;
    @ApiModelProperty(value = "余额")
    Map<String, String> balances;
    @ApiModelProperty(value = "标记")
    private String tag;
    @ApiModelProperty(value = "评论")
    private String comments;
    @ApiModelProperty(value = "状态 1启用 2停用")
    private Integer status;


    public BigDecimal getChainBalanceNum() {
        return isBlank(chainBalance) ? BigDecimal.ZERO : new BigDecimal(chainBalance);
    }

    public BigDecimal getUsdtBalanceNum() {
        return isBlank(usdtBalance) ? BigDecimal.ZERO : new BigDecimal(usdtBalance);
    }
}
