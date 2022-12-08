package com.seeds.account.monitor;

import com.seeds.account.chain.service.IChainService;
import com.seeds.account.dto.AddressBalanceDto;
import com.seeds.account.dto.SystemWalletAddressDto;
import com.seeds.account.service.ISystemWalletAddressService;
import com.seeds.account.util.MetricsGaugeUtils;
import com.seeds.common.enums.Chain;
import io.micrometer.core.instrument.Tags;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 监控热钱包地址余额
 *
 * @author hewei
 */
@Service
@Slf4j
public class HotWalletBalancesMonitorService {
    @Autowired
    private IChainService chainService;
    @Autowired
    private ISystemWalletAddressService systemWalletAddressService;

    @Scheduled(fixedDelay = 60 * 1000, initialDelay = 10 * 1000)
    public void monitor() throws Exception {
        log.info("HotWalletBalancesMonitorService start ...");


        List<String> addressesEth = systemWalletAddressService.getAll()
                .stream()
                .filter(e -> e.getChain() == Chain.ETH.getCode())
                .map(SystemWalletAddressDto::getAddress)
                .collect(Collectors.toList());
        List<String> addressesTron = systemWalletAddressService.getAll()
                .stream()
                .filter(e -> e.getChain() == Chain.TRON.getCode())
                .map(SystemWalletAddressDto::getAddress)
                .collect(Collectors.toList());
        List<AddressBalanceDto> balancesEth = chainService.getBalancesOnBatch(Chain.fromCode(Chain.ETH.getCode()), addressesEth, 0L);

        balancesEth.forEach((address) -> address.getBalances().forEach((currency, balance) ->
                MetricsGaugeUtils.gauge("hot-wallet-balances", Tags.of("currency", currency, "chain", Chain.ETH.getName()), balance.doubleValue())));

        List<AddressBalanceDto> balancesTron = chainService.getBalancesOnBatch(Chain.fromCode(Chain.TRON.getCode()), addressesTron, 0L);

        balancesTron.forEach((address) -> address.getBalances().forEach((currency, balance) ->
                MetricsGaugeUtils.gauge("hot-wallet-balances", Tags.of("currency", currency, "chain", Chain.TRON.getName()), balance.doubleValue())));

        log.info("HotWalletBalancesMonitorService end ...");
    }
}
