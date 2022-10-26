package com.seeds.admin.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Data
@Builder
public class MgtAccountAssetDto {
    long userId;
    Integer accountType;
    String kusdBalance;
    String kineBalance;
    String usdtBalance;
    String usdcBalance;

    BigDecimal kusdBalanceNum;
    BigDecimal kineBalanceNum;
    BigDecimal usdtBalanceNum;
    BigDecimal usdcBalanceNum;

    Map<String, String> currencyBalances;


    public BigDecimal getKusdBalanceNum() {
        return isNotBlank(kusdBalance) ? new BigDecimal(kusdBalance) : BigDecimal.ZERO;
    }

    public BigDecimal getKineBalanceNum() {
        return isNotBlank(kineBalance) ? new BigDecimal(kineBalance) : BigDecimal.ZERO;
    }

    public BigDecimal getUsdtBalanceNum() {
        return isNotBlank(usdtBalance) ? new BigDecimal(usdtBalance) : BigDecimal.ZERO;
    }

    public BigDecimal getUsdcBalanceNum() {
        return isNotBlank(usdcBalance) ? new BigDecimal(usdcBalance) : BigDecimal.ZERO;
    }
}
