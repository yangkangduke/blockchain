package com.seeds.account.chain.service.impl;

import com.google.common.collect.Lists;
import com.seeds.account.chain.service.IChainService;
import com.seeds.account.dto.*;
import com.seeds.account.enums.AccountSystemConfig;
import com.seeds.account.ex.AccountException;
import com.seeds.account.service.ISystemConfigService;
import com.seeds.account.service.ISystemWalletAddressService;
import com.seeds.account.util.AddressUtils;
import com.seeds.account.util.JsonUtils;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.enums.Chain;
import com.seeds.common.enums.ErrorCode;
import com.seeds.common.redis.account.RedisKeys;
import com.seeds.wallet.feign.WalletFeignClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.web3j.abi.datatypes.Address;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yk
 */
@Service
@Slf4j
@Data
public abstract class ChainBasicService implements IChainService {

    @Autowired
    protected ISystemConfigService systemConfigService;
    @Autowired
    RedissonClient client;
//    @Autowired
//    NotificationService notificationService;
    @Autowired
    WalletFeignClient walletFeignClient;
//    @Autowired
//    ChainContractService chainContractService;
    @Autowired
    private ISystemWalletAddressService systemWalletAddressService;

    private GenericDto<String> createAccount(Chain chain) {
        // 2021-04-28 milo 统一添加 chain
        GenericDto<String> response = walletFeignClient.createAccount(this.getCurrentChain(chain).getCode());
        log.info("createAccount chain={} response={}", this.getCurrentChain(chain), response);
        return response;
    }

    @Override
    public List<String> createAddresses(Chain chain, Integer numbers) throws Exception {
        List<String> addresses = Lists.newArrayList();
        try {
            for (int i = 0; i < numbers; i++) {
                GenericDto<String> response = createAccount(chain);
                String address = response.getData();
                address = AddressUtils.formalize(chain, address);
                if (address != null && address.length() > 0 && AddressUtils.validate(chain, address)) {
                    addresses.add(address);
                }
            }
        } catch (Exception e) {
            log.error("error in create new account", e);
        }
        return addresses;
    }

    @Override
    public Long getGasLimit(Chain chain) {
        // 查询币种设置的GasLimit
        String limits = systemConfigService.getValue(AccountSystemConfig.CHAIN_GAS_LIMIT);
        if (limits != null && limits.length() > 0) {
            List<ChainGasLimitDto> dtos = JsonUtils.readValue(limits, new com.fasterxml.jackson.core.type.TypeReference<List<ChainGasLimitDto>>() {
            });
            ChainGasLimitDto dto = dtos.stream().filter(e -> e.getChain() == chain.getCode()).findFirst().orElse(null);
            if (dto != null) {
                return dto.getGasLimit();
            }
        }
        throw new AccountException(ErrorCode.ACCOUNT_BUSINESS_ERROR, "failed to get gas limit");
    }

    @Override
    public Long getGasLimit(Chain chain, String currency) {
        // 查询币种设置的GasLimit
        String limits = systemConfigService.getValue(AccountSystemConfig.CHAIN_ALL_GAS_LIMIT);
        if (limits != null && limits.length() > 0) {
            List<ChainCurrencyGasLimitDto> dtos = JsonUtils.readValue(limits, new com.fasterxml.jackson.core.type.TypeReference<List<ChainCurrencyGasLimitDto>>() {
            });
            ChainCurrencyGasLimitDto dto = dtos.stream().filter(e -> e.getChain() == chain.getCode() && Objects.equals(e.getCurrency(), currency)).findFirst().orElse(null);
            if (dto != null) {
                return dto.getGasLimit();
            }
        }
        // 读取默认
        return getGasLimit(chain);
    }

    @Override
    public List<ChainCurrencyGasLimitDto> getAllGasLimit(Chain chain) {
        String allLimitJson = systemConfigService.getValue(AccountSystemConfig.CHAIN_ALL_GAS_LIMIT);
        if (allLimitJson != null && allLimitJson.length() > 0) {
            List<ChainCurrencyGasLimitDto> dtos = JsonUtils.readValue(allLimitJson, new com.fasterxml.jackson.core.type.TypeReference<List<ChainCurrencyGasLimitDto>>() {
            });
            return dtos.stream().filter(e -> e.getChain() == chain.getCode()).collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }

    @Override
    public Long getGasPrice(Chain chain) {
        return getGasPriceDto(chain).getFastGasPrice();
    }

    @Override
    public ChainGasPriceDto getGasPriceDto(Chain chain) {
        String gasPrices = systemConfigService.getValue(AccountSystemConfig.CHAIN_GAS_PRICE);
        long fastGasPrice;
        long safeGasPrice;
        long proposedGasPrice;
        BigDecimal gasPriceVolatility;
        if (gasPrices == null || gasPrices.length() == 0) {
            throw new AccountException(ErrorCode.ACCOUNT_BUSINESS_ERROR, "failed to get gas price");
        }
        List<ChainGasPriceConfigDto> dtos = JsonUtils.readValue(gasPrices, new com.fasterxml.jackson.core.type.TypeReference<List<ChainGasPriceConfigDto>>() {
        });
        ChainGasPriceConfigDto dto = dtos.stream().filter(e -> e.getChain() == chain.getCode()).findFirst().orElse(null);
        if (dto == null) {
            throw new AccountException(ErrorCode.ACCOUNT_BUSINESS_ERROR, "failed to get gas price");
        }
        fastGasPrice = dto.getDefaultGasPrice();
        safeGasPrice = dto.getDefaultGasPrice();
        proposedGasPrice = dto.getDefaultGasPrice();
        gasPriceVolatility = dto.getGasPriceVolatility().add(BigDecimal.ONE);

        // from redis
        RBucket<String> rBucket = client.getBucket(RedisKeys.getChainGasPriceKey(chain.getCode()), StringCodec.INSTANCE);
        if (rBucket.isExists() && !StringUtils.isEmpty(rBucket.get())) {
            ChainGasPriceDto chainGasPriceDto = JsonUtils.readValue(rBucket.get(), new com.fasterxml.jackson.core.type.TypeReference<ChainGasPriceDto>() {
            });
            fastGasPrice = new BigDecimal(chainGasPriceDto.getFastGasPrice()).multiply(gasPriceVolatility).longValue();
            safeGasPrice = new BigDecimal(chainGasPriceDto.getSafeGasPrice()).multiply(gasPriceVolatility).longValue();
            proposedGasPrice = new BigDecimal(chainGasPriceDto.getProposeGasPrice()).multiply(gasPriceVolatility).longValue();
            log.info("getGasPrice, redis");
        } else {
            // notify to ops
//            notificationService.sendNotificationAsync(NotificationDto.builder()
//                    .notificationType(OpsAction.OPS_SUPPORT.getNotificationType())
//                    .values(ImmutableMap.of(
//                            "ts", System.currentTimeMillis(),
//                            "module", "gas-price",
//                            "content", String.format("failed to get gas price from chain, using default=%s", fastGasPrice)))
//                    .build());
            log.info("getGasPrice, default");
        }
        // 5000 g-wei is the max
        Assert.isTrue(fastGasPrice > 0 && fastGasPrice <= 5000_000000000L, "invalid gas price, fastGasPrice=" + fastGasPrice);
        Assert.isTrue(safeGasPrice > 0 && safeGasPrice <= 5000_000000000L, "invalid gas price, safeGasPrice=" + safeGasPrice);
        Assert.isTrue(proposedGasPrice > 0 && proposedGasPrice <= 5000_000000000L, "invalid gas price, proposedGasPrice=" + proposedGasPrice);

        ChainGasPriceDto chainGasPriceDto = ChainGasPriceDto.builder()
                .chain(chain.getCode())
                .fastGasPrice(fastGasPrice)
                .safeGasPrice(safeGasPrice)
                .proposeGasPrice(proposedGasPrice)
                .build();
        log.info("getGasPrice, chainGasPriceDto={}", chainGasPriceDto);
        return chainGasPriceDto;
    }

    @Override
    public Integer getConfirmBlocks(Chain chain) {
        // 查询币种设置的GasLimit
        String blocks = systemConfigService.getValue(AccountSystemConfig.CHAIN_CONFIRM_BLOCKS);
        if (blocks != null && blocks.length() > 0) {
            List<ChainConfirmBlockDto> dtos = JsonUtils.readValue(blocks, new com.fasterxml.jackson.core.type.TypeReference<List<ChainConfirmBlockDto>>() {
            });
            ChainConfirmBlockDto dto = dtos.stream().filter(e -> e.getChain() == chain.getCode()).findFirst().orElse(null);
            if (dto != null) {
                return dto.getConfirmBlocks();
            }
        }
        throw new AccountException(ErrorCode.ACCOUNT_BUSINESS_ERROR, "failed to get confirm blocks");
    }

    protected BigDecimal unscaleAmountByDecimal(BigInteger amount, Integer decimals) {
        return new BigDecimal(amount).divide(BigDecimal.TEN.pow(decimals));
    }

    protected BigInteger scaleAmountByDecimal(BigDecimal amount, Integer decimals) {
        return amount.abs().multiply(BigDecimal.TEN.pow(decimals)).toBigInteger();
    }

    protected Set<Address> getIgnoreMarkets(Chain chain) {
        String ignoreMarketStr = systemConfigService.getValue(AccountSystemConfig.CHAIN_IGNORE_MARKETS);
        Set<Address> ignoreMarkets = new HashSet<>();
        if (!StringUtils.isEmpty(ignoreMarketStr)) {
            List<ChainIgnoreMarketsDto> dtos = JsonUtils.readValue(ignoreMarketStr, new com.fasterxml.jackson.core.type.TypeReference<List<ChainIgnoreMarketsDto>>() {
            });
            ChainIgnoreMarketsDto dto = dtos.stream().filter(e -> e.getChain() == chain.getCode()).findFirst().orElse(null);
            if (dto != null) {
                ignoreMarkets = dto.getIgnoreMarkets().stream().distinct().map(Address::new).collect(Collectors.toSet());
            }
        }
        return ignoreMarkets;
    }

}
