package com.seeds.account.calc;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.seeds.account.AccountConstants;
import com.seeds.account.dto.UserAccountDto;
import com.seeds.account.dto.UserAccountSummaryDto;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * @author milo
 */
@Slf4j
public class AccountCalculator extends RootCalculator {

    /**
     * 钱包账户
     *
     * @param accounts
     * @param priceMap
     * @return
     */
    public static UserAccountSummaryDto calculateAccountSummary(List<UserAccountDto> accounts, Map<String, BigDecimal> priceMap) {
        UserAccountSummaryDto accountSummaryDto = new UserAccountSummaryDto();
        accountSummaryDto.setAccounts(accounts);
        updateWalletAccountSummary(accountSummaryDto, priceMap);
        return accountSummaryDto;
    }

    /**
     * 更新钱包账户
     *
     * @param walletAccountSummary
     * @param priceMap
     */
    public static void updateWalletAccountSummary(UserAccountSummaryDto walletAccountSummary, Map<String, BigDecimal> priceMap) {
        walletAccountSummary.getAccounts().forEach(e -> {
            BigDecimal price = priceMap.get(e.getCurrency());
            if (price == null && e.getAvailable().signum() != 0) {
                log.error("missing {} price", e.getCurrency());
            }
            e.setPrice(price);
            // available + locked
            e.setTotalValue(e.getAvailable().add(e.getLocked()).multiply(price));
        });
        BigDecimal totalValue = walletAccountSummary.getAccounts().stream()
                .map(UserAccountDto::getTotalValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        walletAccountSummary.setTotalValue(totalValue);
    }

    public static UserAccountDto createWalletAccountOnFly(String currency, BigDecimal price) {
        return UserAccountDto.builder()
                .currency(currency)
                .available(BigDecimal.ZERO)
                .freeze(BigDecimal.ZERO)
                .locked(BigDecimal.ZERO)
                .price(price)
                .totalValue(BigDecimal.ZERO)
                .version(AccountConstants.DEFAULT_VERSION)
                .build();
    }
}
