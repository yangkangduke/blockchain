package com.seeds.wallet.service.impl;

import com.google.common.collect.Maps;
import com.seeds.wallet.dto.RawTransactionDto;
import com.seeds.wallet.dto.SignedMessageDto;
import com.seeds.wallet.providers.WalletChainProvider;
import com.seeds.wallet.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Configuration
public class WalletServiceImpl implements WalletService {

    /**
     * 支持的链处理类
     */
    Map<Integer, WalletChainProvider> providers = Maps.newConcurrentMap();

    @Override
    public void registerProvider(int chain, WalletChainProvider provider) {
        log.info("registerProvider chain={} provider={}", chain, provider);
        providers.put(chain, provider);
    }

    @Override
    @Transactional
    public String createNewWallet(int chain) throws Exception {
        WalletChainProvider provider = providers.get(chain);
        Assert.isTrue(provider != null, "unknown chain " + chain);

        return provider.createNewWallet(chain);
    }

    @Override
    @Transactional
    public String signRawTransaction(int chain, long chainId, RawTransactionDto rawTransaction, String address) throws Exception {
        WalletChainProvider provider = providers.get(chain);
        Assert.isTrue(provider != null, "unknown chain " + chain);
        Assert.isTrue(chainId > 0, "unknown chainId " + chainId);

        return provider.signRawTransaction(chain, chainId, rawTransaction, address);
    }

    @Override
    public SignedMessageDto signMessage(int chain, String messageToSign, String address) throws Exception {
        WalletChainProvider provider = providers.get(chain);
        Assert.isTrue(provider != null, "unknown chain " + chain);

        return provider.signMessage(chain, messageToSign, address);
    }

    @Override
    public List<SignedMessageDto> signMessages(int chain, List<String> messagesToSign, String address) throws Exception {
        WalletChainProvider provider = providers.get(chain);
        Assert.isTrue(provider != null, "unknown chain " + chain);

        return provider.signMessages(chain, messagesToSign, address);
    }
}