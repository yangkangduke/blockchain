package com.seeds.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MgtDepositAddressDto {
    private String address;

//    private String kusdBalance;
//
//    private BigDecimal kusdBalanceNum;

    private String usdtBalance;

    private BigDecimal usdtBalanceNum;

    private String chainBalance;

    private BigDecimal chainBalanceNum;

//    private String kineBalance;
//
//    private BigDecimal kineBalanceNum;
//
//    private String usdcBalance;
//
//    private BigDecimal usdcBalanceNum;
//
//    public BigDecimal getKusdBalanceNum() {
//        return isNotBlank(kusdBalance) ? new BigDecimal(kusdBalance) : BigDecimal.ZERO;
//    }

    public BigDecimal getUsdtBalanceNum() {
        return isNotBlank(usdtBalance) ? new BigDecimal(usdtBalance) : BigDecimal.ZERO;
    }

    public BigDecimal getChainBalanceNum() {
        return isNotBlank(chainBalance) ? new BigDecimal(chainBalance) : BigDecimal.ZERO;
    }

//    public BigDecimal getKineBalanceNum() {
//        return isNotBlank(kineBalance) ? new BigDecimal(kineBalance) : BigDecimal.ZERO;
//    }
//
//    public BigDecimal getUsdcBalanceNum() {
//        return isNotBlank(usdcBalance) ? new BigDecimal(usdcBalance) : BigDecimal.ZERO;
//    }
}
