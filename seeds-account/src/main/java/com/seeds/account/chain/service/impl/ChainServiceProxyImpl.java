package com.seeds.account.chain.service.impl;

import com.google.common.collect.Maps;
import com.seeds.account.chain.dto.ChainTransaction;
import com.seeds.account.chain.dto.ChainTransactionReceipt;
import com.seeds.account.chain.dto.NativeChainBlockDto;
import com.seeds.account.chain.dto.NativeChainTransactionDto;
import com.seeds.account.chain.service.IChainProviderService;
import com.seeds.account.chain.service.IChainService;
import com.seeds.account.dto.*;
import com.seeds.account.util.Utils;
import com.seeds.common.enums.Chain;
import com.seeds.common.enums.ErrorCode;
import com.seeds.wallet.dto.RawTransactionDto;
import com.seeds.wallet.dto.SignedMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.Type;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 这只是个代理类， 代理通过AOP切面实现。 这里的实现方法为空方法。
 * 这里提供了ChainProviderService的注册方法。
 * 所有ChainService的实现类，必须注册到这里。
 *
 * @author ray
 */
@Service
@Slf4j
@Primary
public class ChainServiceProxyImpl implements IChainService, IChainProviderService {


    /**
     * 支持的链处理类
     */
    Map<Chain, IChainService> providers = Maps.newConcurrentMap();

    @Override
    public void registerProvider(Chain chain, IChainService provider) {
        log.info("registerProvider chain={} provider={}", chain, provider);
        Utils.check(chain != null, ErrorCode.ACCOUNT_INVALID_CHAIN);
        providers.put(chain, provider);
    }

    @Override
    public IChainService getServiceProvider(Chain chain) {
        IChainService provider = providers.get(chain);
        Utils.check(provider != null, ErrorCode.ACCOUNT_INVALID_CHAIN);
        return provider;
    }

    @Override
    public Chain getCurrentChain(Chain chain) {
        return getServiceProvider(chain).getCurrentChain(chain);
    }

    @Override
    public Map<String, BigDecimal> getBalances(Chain chain, String address) throws Exception {
        return getServiceProvider(chain).getBalances(chain, address);
    }

    @Override
    public List<Map<String, BigDecimal>> getBalances(Chain chain, List<String> addresses) throws Exception {
        return getServiceProvider(chain).getBalances(chain, addresses);
    }

    @Override
    public List<AddressBalanceDto> getBalancesOnBatch(Chain chain, List<String> addresses, Long sleepBetween) throws Exception {
        return getServiceProvider(chain).getBalancesOnBatch(chain, addresses, sleepBetween);
    }

    @Override
    public List<String> createAddresses(Chain chain, Integer numbers) throws Exception {
        return getServiceProvider(chain).createAddresses(chain, numbers);
    }

    @Override
    public long getLatestBlockNumber(Chain chain) throws Exception {
        return getServiceProvider(chain).getLatestBlockNumber(chain);
    }

    @Override
    public ChainTransaction getTransactionByTxHash(Chain chain, String txHash) throws Exception {
        return getServiceProvider(chain).getTransactionByTxHash(chain, txHash);
    }

    @Override
    public ChainTransactionReceipt getTransactionReceiptByTxHash(Chain chain, String txHash) throws Exception {
        return getServiceProvider(chain).getTransactionReceiptByTxHash(chain, txHash);
    }

    @Override
    public BigDecimal getChainTokenBalance(Chain chain, String address) throws Exception {
        return getServiceProvider(chain).getChainTokenBalance(chain, address);
    }

    @Override
    public BigDecimal getContractBalance(Chain chain, String address, String currency) throws Exception {
        return getServiceProvider(chain).getContractBalance(chain, address, currency);
    }

    @Override
    public BigDecimal getContractBalance(Chain chain, String address, ChainContractDto chainContractDto) throws Exception {
        return getServiceProvider(chain).getContractBalance(chain, address, chainContractDto);
    }

    @Override
    public BigDecimal getContractDecimalValue(Chain chain, String currency, String decimalMethodName) throws Exception {
        return getServiceProvider(chain).getContractDecimalValue(chain, currency, decimalMethodName);
    }

    @Override
    public NativeChainBlockDto getChainNativeBlock(Chain chain, Long blockNumber) throws Exception {
        NativeChainBlockDto nativeChainBlockDto = getServiceProvider(chain).getChainNativeBlock(chain, blockNumber);
        // 2021-04-29 milo
        if (nativeChainBlockDto.getChain() == null) {
            nativeChainBlockDto.setChain(chain);
        }
        return nativeChainBlockDto;
    }

    @Override
    public List<NativeChainTransactionDto> getTransactions(Chain chain, Chain defiChain, Long blockNumber, List<String> addresses) throws Exception {
        IChainService provider = getServiceProvider(chain);

        long l1 = System.currentTimeMillis();
        List<NativeChainTransactionDto> list = provider.getTransactions(chain, defiChain, blockNumber, addresses);
        long l2 = System.currentTimeMillis();
        if (list.size() > 0) {
            log.info("getTransactions chain={} defiChain={} blockNumber={} took={} deposit-transactions={} ", chain, defiChain, blockNumber, (l2 - l1), list);
        }

        return list;
    }

    @Override
    public String sendTransaction(Chain chain, String currency, String fromAddress, String toAddress, BigDecimal amount, Long gasPrice, Long gasLimit) throws Exception {
        return getServiceProvider(chain).sendTransaction(chain, currency, fromAddress, toAddress, amount, gasPrice, gasLimit);
    }

    @Override
    public RawTransactionDto internalSendTransaction(Chain chain, String currency, String fromAddress, String toAddress, BigDecimal amount, Long gasPrice, Long gasLimit) throws Exception {
        return getServiceProvider(chain).internalSendTransaction(chain, currency, fromAddress, toAddress, amount, gasPrice, gasLimit);
    }

    @Override
    public RawTransactionDto internalSendTransaction(Chain chain, String currency, String fromAddress, String toAddress, BigDecimal amount, Long gasPrice, Long gasLimit, Long nonce, Long sleepFor) throws Exception {
        return getServiceProvider(chain).internalSendTransaction(chain, currency, fromAddress, toAddress, amount, gasPrice, gasLimit, nonce, sleepFor);
    }

    @Override
    public BigDecimal chainQuoteAmount(Chain chain, String fromCurrency, String toCurrency, BigDecimal amountIn, Boolean needRoute) throws Exception {
        return getServiceProvider(chain).chainQuoteAmount(chain, fromCurrency, toCurrency, amountIn, needRoute);
    }

    @Override
    public RawTransactionDto chainApprove(Chain chain, String address, String currency, BigDecimal amount) throws Exception {
        return getServiceProvider(chain).chainApprove(chain, address, currency, amount);
    }

    @Override
    public BigDecimal queryChainAllowance(Chain chain, String currency, String address) throws Exception {
        return getServiceProvider(chain).queryChainAllowance(chain, currency, address);
    }

    @Override
    public RawTransactionDto chainSwap(Chain chain, String fromCurrency, String toCurrency, BigDecimal amountIn, BigDecimal minAmountOut, Long deadlineInSecond, String swapFromAddress, String swapToAddress, Boolean needApprove, Boolean needRoute) throws Exception {
        return getServiceProvider(chain).chainSwap(chain, fromCurrency, toCurrency, amountIn, minAmountOut, deadlineInSecond, swapFromAddress, swapToAddress, needApprove, needRoute);
    }

    @Override
    public RawTransactionDto reportKaptain(Chain chain, boolean isRetry, BigDecimal mcdPrice, BigDecimal deltaKusd, BigInteger nonce, Long timestamp, String reporterAddress, String posterAddress, long lastTimestamp) throws Exception {
        return getServiceProvider(chain).reportKaptain(chain, isRetry, mcdPrice, deltaKusd, nonce, timestamp, reporterAddress, posterAddress, lastTimestamp);
    }

    @Override
    public RawTransactionDto addDividend(Chain chain, BigDecimal amount, String dividendAddress, String toAddress, String currency) throws Exception {
        return getServiceProvider(chain).addDividend(chain, amount, dividendAddress, toAddress, currency);
    }

    @Override
    public RawTransactionDto notifyDividend(Chain chain, BigDecimal kineAmount, String dividendAddress, String minterAddress) throws Exception {
        return getServiceProvider(chain).notifyDividend(chain, kineAmount, dividendAddress, minterAddress);
    }

    @Override
    public ChainOraclePricesDto getOraclePrices(Chain chain, Set<String> currency) throws IOException {
        return getServiceProvider(chain).getOraclePrices(chain, currency);
    }

    @Override
    public ChainKTokenConfigDto getKTokenConfigBySymbol(Chain chain, String currency) throws IOException {
        return getServiceProvider(chain).getKTokenConfigBySymbol(chain, currency);
    }

    @Override
    public ChainAnchorPriceView getAnchorPriceView(Chain chain, String currency, String oracleAddress) throws IOException {
        return getServiceProvider(chain).getAnchorPriceView(chain, currency, oracleAddress);
    }

    @Override
    public RawTransactionDto replaceTransaction(Chain chain, RawTransactionDto rawTxn, String fromAddress) throws Exception {
        return getServiceProvider(chain).replaceTransaction(chain, rawTxn, fromAddress);
    }

    @Override
    public RawTransactionDto transferOwnership(Chain chain, String address, String newOwnerAddress, String ownableContractAddress) throws Exception {
        return getServiceProvider(chain).transferOwnership(chain, address, newOwnerAddress, ownableContractAddress);
    }

    @Override
    public BigDecimal getKineTotalSupply(Chain chain) throws IOException {
        return getServiceProvider(chain).getKineTotalSupply(chain);
    }

    @Override
    public BigDecimal getKineProtocolTotalStakingValueInUSDC(Chain chain) throws IOException {
        return getServiceProvider(chain).getKineProtocolTotalStakingValueInUSDC(chain);
    }

    @Override
    public Map<String, BigDecimal> getKineProtocolAssetStakingValueInUSDC(Chain chain, Map<String, ChainContractDto> addressAssetMap) throws IOException {
        return getServiceProvider(chain).getKineProtocolAssetStakingValueInUSDC(chain, addressAssetMap);
    }

    @Override
    public BigDecimal getTotalSupplyByAddressAndDecimals(Chain chain, String address, Integer decimals) throws IOException {
        return getServiceProvider(chain).getTotalSupplyByAddressAndDecimals(chain, address, decimals);
    }

    @Override
    public BigDecimal getBalanceByAddress(Chain chain, String tokenAddress, String accountAddress, Integer tokenDecimal) throws IOException {
        return getServiceProvider(chain).getBalanceByAddress(chain, tokenAddress, accountAddress, tokenDecimal);
    }

    @Override
    public Long getGasPrice(Chain chain) {
        return getServiceProvider(chain).getGasPrice(chain);
    }

    @Override
    public ChainGasPriceDto getGasPriceDto(Chain chain) {
        return getServiceProvider(chain).getGasPriceDto(chain);
    }

    @Override
    public Long getGasLimit(Chain chain) {
        return getServiceProvider(chain).getGasLimit(chain);
    }

    @Override
    public Long getGasLimit(Chain chain, String currency) {
        return getServiceProvider(chain).getGasLimit(chain, currency);
    }

    @Override
    public List<ChainCurrencyGasLimitDto> getAllGasLimit(Chain chain) {
        return getServiceProvider(chain).getAllGasLimit(chain);
    }

    @Override
    public Integer getConfirmBlocks(Chain chain) {
        return getServiceProvider(chain).getConfirmBlocks(chain);
    }

    @Override
    public ChainGasPriceDto getCurrentGasPriceOracle(Chain chain) throws Exception {
        return getServiceProvider(chain).getCurrentGasPriceOracle(chain);
    }

    @Override
    public void getAndMetricCurrentGasPriceOracle(Chain chain) throws Exception {
        getServiceProvider(chain).getAndMetricCurrentGasPriceOracle(chain);
    }

    @Override
    public BigInteger getPendingNonce(Chain chain, String address) {
        IChainService provider = getServiceProvider(chain);
        BigInteger nonce = provider.getPendingNonce(chain, address);
        log.info("getPendingNonce chain={} address={} nonce={}", chain, address, nonce);
        return nonce;
    }

    @Override
    public BigInteger getSafeConfirmedNonce(Chain chain, String address) throws IOException {
        return getServiceProvider(chain).getSafeConfirmedNonce(chain, address);
    }

    @Override
    public Boolean cancelTransaction(Chain chain, String txHash) throws Exception {
        return getServiceProvider(chain).cancelTransaction(chain, txHash);
    }

    @Override
    public String compensateNonceGap(Chain chain, String address, Long nonce) throws Exception {
        return getServiceProvider(chain).compensateNonceGap(chain, address, nonce);
    }

    @Override
    public String encodeMessageByParams(Chain chain, List<Type> paramsToEncode) {
        return getServiceProvider(chain).encodeMessageByParams(chain, paramsToEncode);
    }

    @Override
    public SignedMessageDto signMessage(Chain chain, String message, String address) {
        return getServiceProvider(chain).signMessage(chain, message, address);
    }

    @Override
    public Long getContractClaimHistory(Chain chain, String contractAddress, Long id) throws Exception {
        return getServiceProvider(chain).getContractClaimHistory(chain, contractAddress, id);
    }

    @Override
    public BigDecimal getTotalStakes(Chain chain, String address, int decimals) throws Exception {
        return getServiceProvider(chain).getTotalStakes(chain, address, decimals);
    }

    @Override
    public BigDecimal calcTxFee(Chain chain, Long gasUsed, Long gasPrice) {
        return getServiceProvider(chain).calcTxFee(chain, gasUsed, gasPrice);
    }

    @Override
    public SignedMessageDto encodeAndSignDefiWithdraw(Chain chain, long id, String address, String currency, BigDecimal amount, long deadline) {
        log.info("encodeAndSignDefiWithdraw chain={} id={} address={} currency={} amount={} deadline={}", chain, id, address, currency, amount, deadline);
        IChainService provider = getServiceProvider(chain);
        return provider.encodeAndSignDefiWithdraw(chain, id, address, currency, amount, deadline);
    }
}
