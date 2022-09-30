package com.seeds.account.chain.service.impl.bsc;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.seeds.account.AccountConstants;
import com.seeds.account.chain.dto.ChainTransaction;
import com.seeds.account.chain.dto.ChainTransactionReceipt;
import com.seeds.account.chain.dto.NativeChainBlockDto;
import com.seeds.account.chain.dto.NativeChainTransactionDto;
import com.seeds.account.chain.service.IChainProviderService;
import com.seeds.account.chain.service.IChainService;
import com.seeds.account.chain.service.impl.ChainBasicService;
import com.seeds.account.dto.*;
import com.seeds.account.enums.AccountSystemConfig;
import com.seeds.account.enums.WalletAddressType;
import com.seeds.account.mapper.ChainBlockMapper;
import com.seeds.account.model.ChainBlock;
import com.seeds.account.service.IActionControlService;
import com.seeds.account.service.IChainDepositService;
import com.seeds.account.service.ISystemWalletAddressService;
import com.seeds.account.util.AddressUtils;
import com.seeds.account.util.JsonUtils;
import com.seeds.account.util.ObjectUtils;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.enums.Chain;
import com.seeds.common.model.SymbolConstants;
import com.seeds.common.redis.account.RedisKeys;
import com.seeds.common.utils.BasicUtils;
import com.seeds.wallet.dto.RawTransactionDto;
import com.seeds.wallet.dto.RawTransactionSignRequest;
import com.seeds.wallet.dto.SignMessageRequest;
import com.seeds.wallet.dto.SignedMessageDto;
import com.seeds.wallet.feign.WalletFeignClient;
import io.micrometer.core.instrument.Tags;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.web3j.abi.*;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint64;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.*;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 提供对Binance Smart Chain的节点访问
 *
 * @author milo
 * @author xu
 * @author cheng
 * @author Ray
 */
@Component
@Slf4j
@Data
public class BSCChainServiceProvider extends ChainBasicService implements IChainService {

    /**
     * 定义当前Chain
     */
    final Chain currentChain = Chain.BSC;
    public static final Integer DEFAULT_SCALE = 10;

    @Autowired
    IChainProviderService chainProviderService;
    @Autowired
    ChainBlockMapper chainBlockMapper;
    @Autowired
    WalletFeignClient walletFeignClient;
//    @Autowired
//    ChainFunctionConfig chainFunctionConfig;
//    @Autowired
//    ChainContractService chainContractService;
    @Autowired
    ISystemWalletAddressService systemWalletAddressService;
    @Autowired
    IChainDepositService chainDepositService;
    @Autowired
    RedissonClient client;
//    @Autowired
//    NotificationService notificationService;
    @Autowired
    BSCChainConverter chainConverter;
    @Autowired
    BSCWeb3Client web3;
//    @Autowired
//    PriceService priceService;
    @Autowired
    IActionControlService actionControlService;

    private static final DefaultFunctionEncoder DEFAULT_ENCODER = new DefaultFunctionEncoder();

    @PostConstruct
    public void init() {
        chainProviderService.registerProvider(currentChain, this);
    }

    @Override
    public Chain getCurrentChain(Chain chain) {
        return this.currentChain;
    }

    @Override
    public List<Map<String, BigDecimal>> getBalances(Chain chain, List<String> addresses) throws Exception {
        return null;
    }

    @Override
    public List<AddressBalanceDto> getBalancesOnBatch(Chain chain, List<String> addresses, Long sleepBetween) throws Exception {
        return null;
    }

    @Override
    public Map<String, BigDecimal> getBalances(Chain chain, String address) throws Exception {
        return null;
    }

    /**
     * 获取最新的块高度
     *
     * @return
     * @throws Exception
     */
    @Override
    public long getLatestBlockNumber(Chain chain) throws Exception {
        return web3.readCli().ethBlockNumber().send().getBlockNumber().longValue();
    }

    /**
     * 获取交易
     *
     * @param txHash
     * @return
     * @throws Exception
     */
    @Override
    public ChainTransaction getTransactionByTxHash(Chain chain, String txHash) throws Exception {
        EthTransaction transaction = web3.readCli().ethGetTransactionByHash(txHash).send();
        return chainConverter.toChainTransaction(transaction.getTransaction().orElse(null));
    }

    /**
     * 获取交易
     *
     * @param txHash
     * @return
     * @throws Exception
     */
    @Override
    public ChainTransactionReceipt getTransactionReceiptByTxHash(Chain chain, String txHash) throws Exception {
        EthGetTransactionReceipt receipt = web3.readCli().ethGetTransactionReceipt(txHash).send();
        return chainConverter.toChainTransactionReceipt(receipt.getTransactionReceipt().orElse(null));
    }

    /**
     * 不区分大小写
     *
     * @param addresses
     * @param address
     * @return
     */
    private boolean containsAddress(List<String> addresses, String address) {
        return addresses.stream().anyMatch(e -> e.equalsIgnoreCase(address));
    }

    @Override
    public BigDecimal getChainTokenBalance(Chain chain, String address) throws Exception {
        EthGetBalance ethGetBalance = web3.readCli().ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
        BigInteger wei = ethGetBalance.getBalance();
        return Convert.fromWei(new BigDecimal(wei), Convert.Unit.ETHER);
    }

    @Override
    public BigDecimal getContractBalance(Chain chain, String address, String currency) throws Exception {
        return null;
    }

    @Override
    public BigDecimal getContractBalance(Chain chain, String address, ChainContractDto chainContractDto) throws Exception {
        return null;
    }

    @Override
    public BigDecimal getContractDecimalValue(Chain chain, String currency, String decimalMethodName) throws Exception {
        return null;
    }


    @Override
    public NativeChainBlockDto getChainNativeBlock(Chain chain, Long blockNumber) throws Exception {
        EthBlock ethBlock = web3.readCli().ethGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.valueOf(blockNumber)), true).send();
        if (ethBlock == null || ethBlock.getBlock() == null) {
            return null;
        }
        EthBlock.Block block = ethBlock.getBlock();
        NativeChainBlockDto blockDto = new NativeChainBlockDto();
        blockDto.setBlockTime(block.getTimestamp().longValue());
        blockDto.setBlockNumber(block.getNumber().longValue());
        blockDto.setBlockHash(block.getHash());
        blockDto.setParentHash(block.getParentHash());
        return blockDto;
    }

    @Override
    public List<NativeChainTransactionDto> getTransactions(Chain chain, Chain defiChain, Long blockNumber, List<String> addresses) throws Exception {
        return null;
    }


    @Override
    public String sendTransaction(Chain chain, String currency, String fromAddress, String toAddress, BigDecimal amount, Long gasPrice, Long gasLimit) throws Exception {
        return internalSendTransaction(chain, currency, fromAddress, toAddress, amount, gasPrice, gasLimit).getTxnHash();
    }

    @Override
    public RawTransactionDto internalSendTransaction(Chain chain, String currency, String fromAddress, String toAddress, BigDecimal amount, Long gasPrice, Long gasLimit) throws Exception {
        return null;
    }

    @Override
    public RawTransactionDto internalSendTransaction(Chain chain, String currency, String fromAddress, String toAddress, BigDecimal amount, Long gasPrice, Long gasLimit, Long nonce, Long sleepFor) throws Exception {
        return null;
    }

    @Override
    public BigDecimal chainQuoteAmount(Chain chain, String fromCurrency, String toCurrency, BigDecimal amountIn, Boolean needRoute) throws Exception {
        return null;
    }

    @Override
    public RawTransactionDto chainApprove(Chain chain, String address, String currency, BigDecimal amount) throws Exception {
        return null;
    }

    @Override
    public BigDecimal queryChainAllowance(Chain chain, String currency, String address) throws Exception {
        return null;
    }

    @Override
    public RawTransactionDto chainSwap(Chain chain, String fromCurrency, String toCurrency, BigDecimal amountIn, BigDecimal minAmountOut, Long deadlineInSecond, String swapFromAddress, String swapToAddress, Boolean needApprove, Boolean needRoute) throws Exception {
        return null;
    }

    @Override
    public RawTransactionDto reportKaptain(Chain chain, boolean isRetry, BigDecimal mcdPrice, BigDecimal deltaKusd, BigInteger nonce, Long timestamp, String reporterAddress, String posterAddress, long lastTimestamp) throws Exception {
        return null;
    }

    @Override
    public RawTransactionDto addDividend(Chain chain, BigDecimal amount, String dividendAddress, String toAddress, String currency) throws Exception {
        return null;
    }

    @Override
    public RawTransactionDto notifyDividend(Chain chain, BigDecimal kineAmount, String dividendAddress, String minterAddress) throws Exception {
        return null;
    }

    @Override
    public ChainOraclePricesDto getOraclePrices(Chain chain, Set<String> currency) throws IOException {
        return null;
    }

    @Override
    public ChainKTokenConfigDto getKTokenConfigBySymbol(Chain chain, String currency) throws IOException {
        return null;
    }

    @Override
    public ChainAnchorPriceView getAnchorPriceView(Chain chain, String currency, String oracleAddress) throws IOException {
        return null;
    }

    @Override
    public RawTransactionDto replaceTransaction(Chain chain, RawTransactionDto rawTxn, String fromAddress) throws Exception {
        return null;
    }

    @Override
    public RawTransactionDto transferOwnership(Chain chain, String address, String newOwnerAddress, String ownableContractAddress) throws Exception {
        return null;
    }

    @Override
    public BigDecimal getKineTotalSupply(Chain chain) throws IOException {
        return null;
    }

    @Override
    public BigDecimal getKineProtocolTotalStakingValueInUSDC(Chain chain) throws IOException {
        return null;
    }

    @Override
    public Map<String, BigDecimal> getKineProtocolAssetStakingValueInUSDC(Chain chain, Map<String, ChainContractDto> addressAssetMap) throws IOException {
        return null;
    }

    @Override
    public BigDecimal getTotalSupplyByAddressAndDecimals(Chain chain, String address, Integer decimals) throws IOException {
        return null;
    }

    @Override
    public BigDecimal getBalanceByAddress(Chain chain, String tokenAddress, String accountAddress, Integer tokenDecimal) throws IOException {
        return null;
    }

    @Override
    public ChainGasPriceDto getCurrentGasPriceOracle(Chain chain) throws Exception {
        return null;
    }

    @Override
    public void getAndMetricCurrentGasPriceOracle(Chain chain) throws Exception {

    }

    @Override
    public BigInteger getPendingNonce(Chain chain, String address) {
        return null;
    }

    @Override
    public BigInteger getSafeConfirmedNonce(Chain chain, String address) throws IOException {
        return null;
    }

    @Override
    public Boolean cancelTransaction(Chain chain, String txHash) throws Exception {
        return null;
    }

    @Override
    public String compensateNonceGap(Chain chain, String address, Long nonce) throws Exception {
        return null;
    }

    @Override
    public String encodeMessageByParams(Chain chain, List<Type> paramsToEncode) {
        return null;
    }

    @Override
    public SignedMessageDto signMessage(Chain chain, String message, String address) {
        return null;
    }

    @Override
    public Long getContractClaimHistory(Chain chain, String contractAddress, Long id) throws Exception {
        return null;
    }

    @Override
    public BigDecimal getTotalStakes(Chain chain, String address, int decimals) throws Exception {
        return null;
    }

    @Override
    public BigDecimal calcTxFee(Chain chain, Long gasUsed, Long gasPrice) {
        return null;
    }

    @Override
    public SignedMessageDto encodeAndSignDefiWithdraw(Chain chain, long id, String address, String currency, BigDecimal amount, long deadline) {
        return null;
    }


}