package com.seeds.admin.dto;

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

    private String address;

    private int type;

    private String chain;

//    private String kusdBalance;
//
//    private BigDecimal kusdBalanceNum;

//    private String kineBalance;
//
//    private BigDecimal kineBalanceNum;

    private String chainBalance;

    private BigDecimal chainBalanceNum;

    private String usdtBalance;

    private BigDecimal usdtBalanceNum;

//    private String usdcBalance;
//
//    private BigDecimal usdcBalanceNum;

    Map<String, String> balances;

//    public BigDecimal getKusdBalanceNum() {
//        return isBlank(kusdBalance) ? BigDecimal.ZERO : new BigDecimal(kusdBalance);
//    }
//
//    public BigDecimal getKineBalanceNum() {
//        return isBlank(kineBalance) ? BigDecimal.ZERO : new BigDecimal(kineBalance);
//    }

    public BigDecimal getChainBalanceNum() {
        return isBlank(chainBalance) ? BigDecimal.ZERO : new BigDecimal(chainBalance);
    }

    public BigDecimal getUsdtBalanceNum() {
        return isBlank(usdtBalance) ? BigDecimal.ZERO : new BigDecimal(usdtBalance);
    }
//
//    public BigDecimal getUsdcBalanceNum() {
//        return isBlank(usdcBalance) ? BigDecimal.ZERO : new BigDecimal(usdcBalance);
//    }
}
