package com.seeds.account.chain.service.impl.tron;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.protobuf.InvalidProtocolBufferException;
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
import com.seeds.account.ex.AccountException;
import com.seeds.account.service.IChainDepositService;
import com.seeds.account.service.ISystemConfigService;
import com.seeds.account.util.AddressUtils;
import com.seeds.account.util.JsonUtils;
import com.seeds.account.util.ObjectUtils;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.enums.Chain;
import com.seeds.common.enums.ErrorCode;
import com.seeds.wallet.dto.RawTransactionDto;
import com.seeds.wallet.dto.RawTransactionSignRequest;
import com.seeds.wallet.dto.SignedMessageDto;
import com.seeds.wallet.feign.WalletFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.tron.trident.abi.DefaultFunctionEncoder;
import org.tron.trident.abi.FunctionReturnDecoder;
import org.tron.trident.abi.TypeDecoder;
import org.tron.trident.abi.TypeReference;
import org.tron.trident.abi.datatypes.Address;
import org.tron.trident.abi.datatypes.Function;
import org.tron.trident.abi.datatypes.generated.Uint256;
import org.tron.trident.core.ApiWrapper;
import org.tron.trident.core.contract.Contract;
import org.tron.trident.core.contract.Trc20Contract;
import org.tron.trident.core.exceptions.IllegalException;
import org.tron.trident.core.transaction.TransactionBuilder;
import org.tron.trident.proto.Response;
import org.tron.trident.utils.Base58Check;
import org.tron.trident.utils.Numeric;
import org.web3j.abi.datatypes.Type;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

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
public class TRONChainServiceProvider extends ChainBasicService implements IChainService {

    /**
     * 定义当前Chain
     */
    final Chain currentChain = Chain.TRON;

    String DEFAULT_ILLEGAL_EXCEPTION_MSG = "Query failed. Please check the parameters.";

    @Autowired
    IChainProviderService chainProviderService;

    @Autowired
    TRONGridClient tronClient;

    @Autowired
    IChainDepositService chainDepositService;

    @Autowired
    ISystemConfigService systemConfigService;

    @Autowired
    WalletFeignClient walletFeignClient;

    @Autowired
    TRONChainConverter converter;

    private final TransferFunctionDecoder DEFAULT_DECODER = new TransferFunctionDecoder();
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
    public Map<String, BigDecimal> getBalances(Chain chain, String address) throws Exception {
        return null;
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
    public long getLatestBlockNumber(Chain chain) throws Exception {
        return tronClient.cli().getNowBlock().getBlockHeader().getRawData().getNumber();
    }

    @Override
    public ChainTransaction getTransactionByTxHash(Chain chain, String txHash) throws Exception {
        try {
            org.tron.trident.proto.Chain.Transaction transaction = tronClient.cli().getTransactionById(txHash);
            return converter.toChainTransaction(txHash, transaction);
        } catch (IllegalException e) {
            if (DEFAULT_ILLEGAL_EXCEPTION_MSG.equals(e.getMessage())) {
                log.warn("getTransactionByTxHash exception: {}", e.getMessage(), e);
                return null;
            } else {
                throw e;
            }
        }
    }

    @Override
    public ChainTransactionReceipt getTransactionReceiptByTxHash(Chain chain, String txHash) throws Exception {
        try {
            org.tron.trident.proto.Chain.Transaction transaction = tronClient.cli().getTransactionById(txHash);
            Response.TransactionInfo transactionInfo = tronClient.cli().getTransactionInfoById(txHash);
            Response.BlockExtention blockExtention = tronClient.cli().getBlockByLimitNext(transactionInfo.getBlockNumber(), transactionInfo.getBlockNumber() + 1).getBlock(0);
            return converter.toChainTransactionReceipt(transaction, transactionInfo, blockExtention);
        } catch (IllegalException e) {
            if (DEFAULT_ILLEGAL_EXCEPTION_MSG.equals(e.getMessage())) {
                log.warn("getTransactionReceiptByTxHash exception: {}", e.getMessage(), e);
                return null;
            } else {
                throw e;
            }
        }
    }

    @Override
    public BigDecimal getChainTokenBalance(Chain chain, String address) throws Exception {
        Long sun = tronClient.cli().getAccountBalance(address);
        return org.tron.trident.utils.Convert.fromSun(String.valueOf(sun), org.tron.trident.utils.Convert.Unit.TRX);
    }

    @Override
    public BigDecimal getContractBalance(Chain chain, String address, String currency) throws Exception {
       return null;
    }

    @Override
    public BigDecimal getContractBalance(Chain chain, String address, ChainContractDto chainContractDto) throws Exception {
        return getContractBalance(address, chainContractDto);
    }

    private BigDecimal getContractBalance(String address, ChainContractDto chainContractDto) throws Exception {
        String contractAddress = chainContractDto.getAddress();

        //This is core.contract.Contract, not from the proto
        Contract contract = tronClient.cli().getContract(contractAddress);
        Trc20Contract token = new Trc20Contract(contract, address, tronClient.cli());
        int decimals = chainContractDto.getDecimals();
        BigInteger value = token.balanceOf(address);
        if (value != null) {
            BigDecimal amount = unscaleAmountByDecimal(value, decimals);
            return amount;
        } else {
            return BigDecimal.ZERO;
        }
    }

    @Override
    public BigDecimal getContractDecimalValue(Chain chain, String currency, String decimalMethodName) throws Exception {
        return null;
    }

    @Override
    public NativeChainBlockDto getChainNativeBlock(Chain chain, Long blockNumber) throws Exception {
        try {
            Response.BlockExtention blockExtention = tronClient.cli().getBlockByLimitNext(blockNumber, blockNumber + 1).getBlock(0);
            return converter.toNativeChainBlockDto(blockExtention);
        } catch (IllegalException e) {
            if (DEFAULT_ILLEGAL_EXCEPTION_MSG.equals(e.getMessage())) {
                log.warn("getChainNativeBlock exception: {}", e.getMessage(), e);
                return null;
            } else {
                throw e;
            }
        }
    }

    @Override
    public List<NativeChainTransactionDto> getTransactions(Chain chain, Chain defiChain, Long blockNumber, List<String> addresses) throws Exception {
        return null;
    }



    private boolean containsAddress(List<String> addresses, String address) {
        return addresses.stream().anyMatch(e -> e.equalsIgnoreCase(address));
    }

    @Override
    public String sendTransaction(Chain chain, String currency, String fromAddress, String toAddress, BigDecimal amount, Long gasPrice, Long gasLimit) throws Exception {
        return internalSendTransaction(chain, currency, fromAddress, toAddress, amount, gasPrice, gasLimit).getTxnHash();
    }

    @Override
    public RawTransactionDto internalSendTransaction(Chain chain, String currency, String fromAddress, String toAddress, BigDecimal amount, Long gasPrice, Long gasLimit) throws Exception {
      return null;
    }

    private long getSendTransactionSleep() {
        return Long.parseLong(systemConfigService.getValue(AccountSystemConfig.CHAIN_TRANSACTION_SEND_SLEEP, "3000"));
    }

    @Override
    public RawTransactionDto internalSendTransaction(Chain chain, String currency, String fromAddress, String toAddress, BigDecimal amount, Long gasPrice, Long gasLimit, Long nonce, Long sleepFor) throws Exception {
        return null;
    }



    private GenericDto<String> sign(RawTransactionSignRequest request) {
        // 2021-04-28 milo 统一添加 chain & chainId
        request.setChain(currentChain.getCode());
        request.setChainId(tronClient.getChainRpcConfig().getChainId());
        log.info("signMessages chain={} request={}", currentChain, request);

        GenericDto<String> response = walletFeignClient.sign(request);
        log.info("signMessages chain={} response={}", currentChain, response);
        return response;
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

        //This is core.contract.Contract, not from the proto
        Contract contract = tronClient.cli().getContract(tokenAddress);
        String callerAddress = tokenAddress;
        // 只查询， 随便写一个合法的地址即可

        Trc20Contract token = new Trc20Contract(contract, callerAddress, tronClient.cli());

        BigInteger value = token.balanceOf(accountAddress);

        if (value != null) {
            BigDecimal amount = unscaleAmountByDecimal(value, tokenDecimal);
            return amount;
        } else {
            return BigDecimal.ZERO;
        }

    }

    @Override
    public ChainGasPriceDto getGasPriceDto(Chain chain) {
        String gasPrices = systemConfigService.getValue(AccountSystemConfig.CHAIN_GAS_PRICE);
        long fastGasPrice;
        long safeGasPrice;
        long proposedGasPrice;
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
    public ChainGasPriceDto getCurrentGasPriceOracle(Chain chain) throws Exception {
        //TODO;   ？ TRON 没有 gas price
        return null;
    }

    @Override
    public void getAndMetricCurrentGasPriceOracle(Chain chain) throws Exception {
        //TODO;  TRON 没有gas price
    }

    @Override
    public BigInteger getPendingNonce(Chain chain, String address) {
        //TODO; TRON 没有nonce
        return BigInteger.ZERO;
    }

    @Override
    public BigInteger getSafeConfirmedNonce(Chain chain, String address) throws IOException {
        //TODO;   TRON 没有nonce
        return BigInteger.ZERO;
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
        // TRON 不需要这个方法， 交易如果燃烧TRX，则直接查询燃烧的trx数量即可。 sun单位
        return new BigDecimal(gasUsed.toString());
    }

    @Override
    public SignedMessageDto encodeAndSignDefiWithdraw(Chain chain, long id, String address, String currency, BigDecimal amount, long deadline) {
        return null;
    }

    @Slf4j
    private static final class TransferFunctionDecoder {
        Method decoderMethod = null;

        TransferFunctionDecoder() {
            try {
                decoderMethod = TypeDecoder.class.getDeclaredMethod("decode", String.class, int.class, Class.class);
                decoderMethod.setAccessible(true);
            } catch (NoSuchMethodException e) {
                log.error("failed to init transfer decoder", e);
            }
        }

        Method getDecoderMethod() {
            return this.decoderMethod;
        }
    }
}
