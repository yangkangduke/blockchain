package com.seeds.account.monitor;

import com.google.common.collect.Maps;
import com.seeds.account.service.IAddressCollectService;
import com.seeds.account.util.MetricsGaugeUtils;
import com.seeds.common.enums.Chain;
import io.micrometer.core.instrument.Tags;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 监控待归集的地址余额
 * @author hewei
 */
@Service
@Slf4j
public class PendingCollectBalancesMonitorService {
    @Autowired
    private IAddressCollectService addressCollectService;

    /**
     * 由于查询链上余额较慢， 地址较多。
     * 这里15分钟执行一次
     */
    @Scheduled(fixedDelay = 15 * 60 * 1000, initialDelay = 10 * 1000)
    public void monitor() throws Exception {
        log.info("PendingCollectBalancesMonitorService start ...");

        Map<Chain, Map<String, BigDecimal>> collectBalances = addressCollectService.getPendingCollectBalances();

        Map<String, BigDecimal> allChainTotal = Maps.newHashMap();

        collectBalances.forEach(((key, total) -> {
            total.forEach((currency, balance) -> {
                BigDecimal balanceAllChainTotal = allChainTotal.computeIfAbsent(currency, k -> BigDecimal.ZERO);
                allChainTotal.put(currency, balanceAllChainTotal.add(balance));
            });
            log.debug("fetchAndCacheBalance chain={} total={}", key, total);
            total.forEach((currency, balanceTotal) -> MetricsGaugeUtils.gauge("user-deposit-address-balances",
                    Tags.of("currency", currency, "chain", key.getName()), balanceTotal.doubleValue()));
        }));

        allChainTotal.forEach((currency, balanceTotal) -> MetricsGaugeUtils.gauge("user-deposit-address-balances",
                Tags.of("currency", currency, "chain", "ALL"), balanceTotal.doubleValue()));

        log.info("PendingCollectBalancesMonitorService end ...");
    }
}
