package com.seeds.account.chain.service.impl.eth;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.seeds.account.AccountConstants;
import com.seeds.account.anno.ExecutionLock;
import com.seeds.account.chain.dto.ChainTransaction;
import com.seeds.account.chain.dto.ChainTransactionReceipt;
import com.seeds.account.chain.dto.NativeChainBlockDto;
import com.seeds.account.chain.dto.NativeChainTransactionDto;
import com.seeds.account.chain.feigh.ETHScanApiClient;
import com.seeds.account.chain.feigh.FeignClientFactory;
import com.seeds.account.chain.service.IChainProviderService;
import com.seeds.account.chain.service.IChainService;
import com.seeds.account.chain.service.impl.ChainBasicService;
import com.seeds.account.dto.*;
import com.seeds.account.enums.AccountSystemConfig;
import com.seeds.account.enums.WalletAddressType;
import com.seeds.account.mapper.ChainBlockMapper;
import com.seeds.account.model.ChainBlock;
import com.seeds.account.service.IActionControlService;
import com.seeds.account.service.IChainContractService;
import com.seeds.account.service.IChainDepositService;
import com.seeds.account.service.ISystemWalletAddressService;
import com.seeds.account.util.AddressUtils;
import com.seeds.account.util.JsonUtils;
import com.seeds.account.util.ObjectUtils;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.enums.Chain;
import com.seeds.common.enums.ErrorCode;
import com.seeds.common.exception.ChainException;
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
import org.springframework.beans.factory.annotation.Value;
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
 * 提供对Ethereum的节点访问
 *
 * @author yk
 * @author xu
 * @author cheng
 * @author Ray
 */
@Component
@Slf4j
@Data
public class ETHChainServiceProvider extends ChainBasicService implements IChainService {

    @Value("${etherscan.api.key:WF61F7WQBJDHTXYUUF8IANBS2H1IZ7ME1V}")
    private String scanKey;
    /**
     * 定义当前Chain
     */
    final Chain currentChain = Chain.ETH;

    @Autowired
    IChainProviderService chainProviderService;

    public static final Integer DEFAULT_SCALE = 10;

    @Autowired
    ChainBlockMapper chainBlockMapper;
    @Autowired
    WalletFeignClient walletFeignClient;

//    @Autowired
//    IChainFunctionConfig chainFunctionConfig;
    @Autowired
    IChainContractService chainContractService;

    @Autowired
    ISystemWalletAddressService systemWalletAddressService;
    @Autowired
    IChainDepositService chainDepositService;
    @Autowired
    RedissonClient client;
    @Autowired
    FeignClientFactory feignClientFactory;
//    @Autowired
//    NotificationService notificationService;
    @Autowired
    ETHChainConverter chainConverter;
    @Autowired
    ETHWeb3Client web3;
//    @Autowired
//    PriceService priceService;

    @Autowired
    IActionControlService actionControlService;

    private final TransferFunctionDecoder DEFAULT_DECODER = new TransferFunctionDecoder();
    private static final DefaultFunctionEncoder DEFAULT_ENCODER = new DefaultFunctionEncoder();

    private ETHScanApiClient scanApiClient;

    @PostConstruct
    public void init() {
        scanApiClient = feignClientFactory.etherscanApiClient();
        chainProviderService.registerProvider(currentChain, this);
    }

    @Override
    public Chain getCurrentChain(Chain chain) {
        return this.currentChain;
    }

    @Override
    public Map<String, BigDecimal> getBalances(Chain chain, String address) throws Exception {
        List<String> currencyList = chainContractService.getAll().stream()
                .filter(e -> e.getChain() == chain.getCode())
                .map(ChainContractDto::getCurrency)
                .collect(Collectors.toList());

        Map<String, BigDecimal> addressBalances = Maps.newHashMap();
        BigDecimal nativeTokenBalance = getChainTokenBalance(chain, address);
        addressBalances.put(currentChain.getNativeToken(), nativeTokenBalance);
        for (String contract : currencyList) {
            BigDecimal contractBalance = getContractBalance(chain, address, contract);
            addressBalances.put(contract, contractBalance);
        }
        return addressBalances;
    }

    @Override
    public List<Map<String, BigDecimal>> getBalances(Chain chain, List<String> addresses) throws Exception {
        List<String> currencyList = chainContractService.getAll().stream()
                .filter(e -> e.getChain() == chain.getCode())
                .map(ChainContractDto::getCurrency)
                .collect(Collectors.toList());

        List<Map<String, BigDecimal>> balances = Lists.newArrayList();
        for (String address : addresses) {
            Map<String, BigDecimal> addressBalances = Maps.newHashMap();
            BigDecimal nativeTokenBalance = getChainTokenBalance(chain, address);
            addressBalances.put(currentChain.getNativeToken(), nativeTokenBalance);
            for (String contract : currencyList) {
                BigDecimal contractBalance = getContractBalance(chain, address, contract);
                addressBalances.put(contract, contractBalance);
            }
            balances.add(addressBalances);
        }
        return balances;
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
        ChainContractDto chainContractDto = chainContractService.get(chain.getCode(), currency);
        return getContractBalance(chain, address, chainContractDto);
    }

    @Override
    public BigDecimal getContractBalance(Chain chain, String address, ChainContractDto chainContractDto) throws Exception {
        DefaultBlockParameter blockParameter = DefaultBlockParameterName.LATEST;
        return getContractBalance(address, chainContractDto, blockParameter);
    }

    @Override
    public BigDecimal getContractDecimalValue(Chain chain, String currency, String decimalMethodName) throws Exception {
        ChainContractDto contractConfigDto = chainContractService.get(chain.getCode(), currency);
        DefaultBlockParameter blockParameter = DefaultBlockParameterName.LATEST;
        Function function = new Function(decimalMethodName, new ArrayList<>(), Arrays.asList(new TypeReference<Uint256>() {
        }));
        String data = FunctionEncoder.encode(function);
        String contractAddress = contractConfigDto.getAddress();
        log.debug("contractAddress={} currency={} decimalMethodName={} decimals={}",
                contractAddress, contractConfigDto.getCurrency(), decimalMethodName, contractConfigDto.getDecimals());

        Transaction transaction = Transaction.createEthCallTransaction(null, contractAddress, data);
        String result = web3.readCli().ethCall(transaction, blockParameter).send().getValue();
        List<Type> types = FunctionReturnDecoder.decode(result, function.getOutputParameters());
        if (types != null && types.size() > 0) {
            BigInteger value = ((Uint256) types.get(0)).getValue();
            int decimals = contractConfigDto.getDecimals();
            BigDecimal amount = unscaleAmountByDecimal(value, decimals);
            return amount;
        }
        throw new ChainException("failed to call the chain");
    }

    private BigDecimal getContractBalance(String address, ChainContractDto chainContractDto, DefaultBlockParameter blockParameter) throws Exception {
        Function function = new Function("balanceOf", Arrays.asList(new Address(address)), Arrays.asList(new TypeReference<Uint256>() {
        }));
        String data = FunctionEncoder.encode(function);
        String contractAddress = chainContractDto.getAddress();
        log.debug("contractAddress={} currency={} decimals={}", contractAddress, chainContractDto.getCurrency(), chainContractDto.getDecimals());

        Transaction transaction = Transaction.createEthCallTransaction(address, contractAddress, data);
        String result = web3.readCli().ethCall(transaction, blockParameter).send().getValue();
        List<Type> types = FunctionReturnDecoder.decode(result, function.getOutputParameters());
        if (types != null && types.size() > 0) {
            BigInteger value = ((Uint256) types.get(0)).getValue();
            int decimals = chainContractDto.getDecimals();
            BigDecimal amount = unscaleAmountByDecimal(value, decimals);
            return amount;
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal getContractTotal(String address, ContractConfigDto contractConfigDto, DefaultBlockParameter blockParameter) throws Exception {
        Function function = new Function("balanceOf", Arrays.asList(new Address(address)), Arrays.asList(new TypeReference<Uint256>() {
        }));
        String data = FunctionEncoder.encode(function);
        String contractAddress = contractConfigDto.getAddress();
        log.debug("contractAddress={} currency={} decimals={}", contractAddress, contractConfigDto.getCurrency(), contractConfigDto.getDecimals());

        Transaction transaction = Transaction.createEthCallTransaction(address, contractAddress, data);
        String result = web3.readCli().ethCall(transaction, blockParameter).send().getValue();
        List<Type> types = FunctionReturnDecoder.decode(result, function.getOutputParameters());
        if (types != null && types.size() > 0) {
            BigInteger value = ((Uint256) types.get(0)).getValue();
            int decimals = contractConfigDto.getDecimals();
            BigDecimal amount = unscaleAmountByDecimal(value, decimals);
            return amount;
        }
        return BigDecimal.ZERO;
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
        return getTransactions0(web3.readCli(), chain, defiChain, blockNumber, addresses);
    }

    private List<NativeChainTransactionDto> getTransactions0(Web3j web3j, Chain chain, Chain defiChain, Long blockNumber, List<String> addresses) throws Exception {
        List<NativeChainTransactionDto> ethereumTransactions = Lists.newArrayList();

        String defiContractAddress = systemWalletAddressService.getOne(chain, WalletAddressType.DEFI_DEPOSIT_WITHDRAW_CONTRACT);

        EthBlock ethBlock = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.valueOf(blockNumber)), true).send();
        List<EthBlock.TransactionResult> transactionResults = ethBlock.getBlock().getTransactions();
        transactionResults.forEach(transactionResult -> {
            EthBlock.TransactionObject transactionObject = (EthBlock.TransactionObject) transactionResult.get();

            // to address
            String toAddress = transactionObject.getTo();
            if (containsAddress(addresses, toAddress)) {
                // transaction hash 需要去查询交易的状态，如果交易不存在或者交易状态不是OK就不处理
                String txHash = transactionObject.getHash();
                TransactionReceipt receipt = null;
                try {
                    receipt = web3j.ethGetTransactionReceipt(txHash).send().getTransactionReceipt().orElse(null);
                } catch (Exception ex) {
                    log.error("getTransactions txHash={}", txHash, ex);
                    // 如果报错就不处理
                    return;
                }
                if (receipt == null || !receipt.isStatusOK()) {
                    log.error("getTransactions txHash={} receipt={}", txHash, receipt);
                    return;
                }
                String currency = currentChain.getNativeToken();
                int decimals = currentChain.getDecimals();

                long blockTime = ethBlock.getBlock().getTimestamp().longValue();
                String blockHash = ethBlock.getBlock().getHash();

                // from address
                String fromAddress = transactionObject.getFrom();
                // wei
                BigInteger value = transactionObject.getValue();
                // amount
                BigDecimal amount = unscaleAmountByDecimal(value, decimals);
                // 不解析手续费
                BigDecimal txFee = BigDecimal.ZERO;

                NativeChainTransactionDto nativeChainTransactionDto = NativeChainTransactionDto.builder()
                        .chain(chain)
                        .blockNumber(blockNumber)
                        .blockHash(blockHash)
                        .txHash(txHash)
                        .txTime(blockTime)
                        .fromAddress(fromAddress)
                        .toAddress(toAddress)
                        .currency(currency)
                        .amount(amount)
                        .txFee(txFee)
                        .build();
                ethereumTransactions.add(nativeChainTransactionDto);
            } else {
                chainContractService.getAll().forEach(contractConfigDto -> {
                    if (contractConfigDto.getChain() == chain.getCode() && ObjectUtils.isAddressEquals(contractConfigDto.getAddress(), toAddress)) {
                        decodeContractTransaction(chain, defiChain, defiContractAddress,
                                contractConfigDto, addresses, ethereumTransactions, ethBlock, transactionObject);
                    }
                });
            }
        });

        return ethereumTransactions;
    }

    private void decodeContractTransaction(Chain chain, Chain defiChain, String defiContractAddress,
                                           ChainContractDto contractConfigDto, List<String> addresses,
                                           List<NativeChainTransactionDto> ethereumTransactions,
                                           EthBlock ethBlock, EthBlock.TransactionObject transactionObject) {
        ContractTransferParamsDto params = decodeInputAsTransfer(transactionObject.getInput());
        if (params == null) {
            return;
        }
        // 判断充币地址是否为交易所分配给用户的充币地址
        if (containsAddress(addresses, params.getToAddress())) {
            NativeChainTransactionDto nativeChainTransactionDto
                    = decodeContractTransaction0(chain, defiChain, defiContractAddress, contractConfigDto, addresses, ethereumTransactions, ethBlock, transactionObject, params);
            if (nativeChainTransactionDto != null) {
                nativeChainTransactionDto.setChain(chain);
                ethereumTransactions.add(nativeChainTransactionDto);
            }
        }
        // 如果是defi充币（此处并不判断fromAddress是否是交易所中已经绑定用户Id的用户，留给后面的业务处理）
        else if (defiChain != null && ObjectUtils.isAddressEquals(defiContractAddress, params.getToAddress())) {
            NativeChainTransactionDto nativeChainTransactionDto =
                    decodeContractTransaction0(chain, defiChain, defiContractAddress, contractConfigDto, addresses, ethereumTransactions, ethBlock, transactionObject, params);
            if (nativeChainTransactionDto != null) {
                nativeChainTransactionDto.setChain(defiChain);
                ethereumTransactions.add(nativeChainTransactionDto);
            }
        }
    }

    private NativeChainTransactionDto decodeContractTransaction0(Chain chain, Chain defiChain, String defiContractAddress,
                                                                 ChainContractDto contractConfigDto, List<String> addresses,
                                                                 List<NativeChainTransactionDto> ethereumTransactions,
                                                                 EthBlock ethBlock, EthBlock.TransactionObject transactionObject, ContractTransferParamsDto params) {
        // block number
        long blockNumber = ethBlock.getBlock().getNumber().longValue();
        // block hash
        String blockHash = ethBlock.getBlock().getHash();
        // transaction hash
        String txHash = transactionObject.getHash();

        log.info("decodeContractTransaction contractConfigDto={} params={} input={} value={} blockNumber={} blockHash={} txHash={} from={}",
                contractConfigDto, params, transactionObject.getInput(), transactionObject.getValue(), blockNumber, blockHash, txHash, transactionObject.getFrom());

        TransactionReceipt receipt = null;
        try {
            receipt = web3.readCli().ethGetTransactionReceipt(txHash).send().getTransactionReceipt().orElse(null);
        } catch (Exception ex) {
            log.error("getTransactions txHash={}", txHash, ex);
            // 如果报错就不处理
            return null;
        }
        if (receipt == null || !receipt.isStatusOK()) {
            log.error("getTransactions txHash={} receipt={} contractConfigDto={}", txHash, receipt, contractConfigDto);
            return null;
        }

        String currency = contractConfigDto.getCurrency();

        int decimals = contractConfigDto.getDecimals();

        long blockTime = ethBlock.getBlock().getTimestamp().longValue();

        // from address
        String fromAddress = transactionObject.getFrom();
        // to address
        String toAddress = params.getToAddress();
        // the token amount to transfer
        BigDecimal amount = unscaleAmountByDecimal(params.getValue(), decimals);
        // 不解析手续费
        BigDecimal txFee = BigDecimal.ZERO;
        NativeChainTransactionDto nativeChainTransactionDto = NativeChainTransactionDto.builder()
                .blockNumber(blockNumber).blockHash(blockHash).txHash(txHash).txTime(blockTime).fromAddress(fromAddress).toAddress(toAddress).currency(currency).amount(amount).txFee(txFee).build();
        return nativeChainTransactionDto;
    }

    /**
     * Function: transfer(address to, uint256 value)
     * <p>
     * MethodID: 0xa9059cbb
     * [0]:  0000000000000000000000008beeaa193a5d3b677e86109a7c8f8b9a928946bb
     * [1]:  0000000000000000000000000000001dc7f967d827de7507938aa36000000000
     * <p>
     * <p>
     * Function: transferFrom(address from, address to, uint256 value)
     * <p>
     * MethodID: 0x23b872dd
     * [0]:  00000000000000000000000039367ce3a0ad2bde87bdaf932aaa76f6bdc1c25e
     * [1]:  0000000000000000000000008beeaa193a5d3b677e86109a7c8f8b9a928946bb
     * [2]:  0000000000000000000000000000000000000000000000008ac7230489e80000
     *
     * @param input
     * @return
     */

    private ContractTransferParamsDto decodeInputAsTransfer(String input) {
        if (input != null && (input.length() == 138 || input.length() == 202)) {
            try {
                String methodId = input.substring(0, 10);
                // transfer(address to, uint256 value)
                if (Objects.equals("0xa9059cbb", methodId)) {
                    String to = input.substring(10, 74);
                    String value = input.substring(74, 138);
                    Address toAddress = (Address) DEFAULT_DECODER.getDecoderMethod().invoke(null, to, 0, Address.class);
                    Uint256 amount = (Uint256) DEFAULT_DECODER.getDecoderMethod().invoke(null, value, 0, Uint256.class);

                    ContractTransferParamsDto paramsDto = new ContractTransferParamsDto();
                    paramsDto.setMethodId(methodId);
                    paramsDto.setToAddress(toAddress.getValue());
                    paramsDto.setValue(amount.getValue());
                    return paramsDto;
                }
                // transferFrom(address from, address to, uint256 value)
                else if (Objects.equals("0x23b872dd", methodId)) {
                    String from = input.substring(10, 74);
                    String to = input.substring(74, 138);
                    String value = input.substring(138, 202);
                    Address fromAddress = (Address) DEFAULT_DECODER.getDecoderMethod().invoke(null, from, 0, Address.class);
                    Address toAddress = (Address) DEFAULT_DECODER.getDecoderMethod().invoke(null, to, 0, Address.class);
                    Uint256 amount = (Uint256) DEFAULT_DECODER.getDecoderMethod().invoke(null, value, 0, Uint256.class);

                    ContractTransferParamsDto paramsDto = new ContractTransferParamsDto();
                    paramsDto.setMethodId(methodId);
                    paramsDto.setFromAddress(fromAddress.getValue());
                    paramsDto.setToAddress(toAddress.getValue());
                    paramsDto.setValue(amount.getValue());
                    return paramsDto;
                }
            } catch (Exception e) {
                log.error("failed to decodeInputAsTransfer input={}", input);
            }
        }
        return null;
    }

    @Override
    public String sendTransaction(Chain chain, String currency, String fromAddress, String toAddress, BigDecimal amount, Long gasPrice, Long gasLimit) throws Exception {
        return internalSendTransaction(chain, currency, fromAddress, toAddress, amount, gasPrice, gasLimit).getTxnHash();
    }

    @Override
    public RawTransactionDto internalSendTransaction(Chain chain, String currency, String fromAddress, String toAddress, BigDecimal amount, Long gasPrice, Long gasLimit) throws Exception {
        // 发送每笔交易后休眠
        long sleepFor = getSendTransactionSleep();
        long nonce = getPendingNonce(chain, fromAddress).longValue();
        RawTransactionDto raw = internalSendTransaction0(chain, currency, fromAddress, toAddress, amount, gasPrice, gasLimit, nonce, sleepFor);
        return raw;
    }

    @Override
    public RawTransactionDto internalSendTransaction(Chain chain, String currency, String fromAddress, String toAddress, BigDecimal amount, Long gasPrice, Long gasLimit, Long nonce, Long sleepFor) throws Exception {
        RawTransactionDto raw = internalSendTransaction0(chain, currency, fromAddress, toAddress, amount, gasPrice, gasLimit, nonce, sleepFor);
        return raw;
    }

    private RawTransactionDto internalSendTransaction0(Chain chain, String currency, String fromAddress, String toAddress, BigDecimal amount, long gasPrice, long gasLimit, long nonce, long sleepFor) throws Exception {
        log.info("sendTransaction currency={} fromAddress={} toAddress={} amount={} gasPrice={} gasLimit={}",
                currency, fromAddress, toAddress, amount, gasPrice, gasLimit);
        Assert.isTrue(currency != null && currency.length() > 0, "invalid currency " + currency);
        Assert.isTrue(amount != null && amount.signum() > 0, "invalid amount " + amount);
        Assert.isTrue(AddressUtils.validate(chain, fromAddress), "invalid from address " + fromAddress);
        Assert.isTrue(AddressUtils.validate(chain, toAddress), "invalid to address " + toAddress);

        BasicUtils.sleepFor(sleepFor);

        //BigInteger latest_nonce = web3j.ethGetTransactionCount(fromAddress, DefaultBlockParameterName.LATEST).send().getTransactionCount();
        BigInteger pendingNonce = BigInteger.valueOf(nonce); //web3.cli().ethGetTransactionCount(fromAddress, DefaultBlockParameterName.PENDING).send().getTransactionCount();

        RawTransactionDto rawTransactionDto = new RawTransactionDto();
        rawTransactionDto.setNonce(pendingNonce);
        rawTransactionDto.setGasPrice(BigInteger.valueOf(gasPrice));
        rawTransactionDto.setGasLimit(BigInteger.valueOf(gasLimit));

        if (Objects.equals(currentChain.getNativeToken(), currency)) {
            int decimals = currentChain.getDecimals();
            BigInteger value = scaleAmountByDecimal(amount, decimals);
            rawTransactionDto.setTo(toAddress);
            rawTransactionDto.setData("");
            rawTransactionDto.setValue(value);
        } else {
            ChainContractDto contractConfigDto = chainContractService.get(chain.getCode(), currency);
            Assert.isTrue(contractConfigDto != null, "Currency contract config not found");

            String contractAddress = contractConfigDto.getAddress();
            int decimals = contractConfigDto.getDecimals();
            BigInteger value = scaleAmountByDecimal(amount, decimals);

            Function function = new Function("transfer", Arrays.asList(new Address(toAddress), new Uint256(value)), Collections.emptyList());
            String data = FunctionEncoder.encode(function);
            // 发给合约地址
            rawTransactionDto.setTo(contractAddress);
            rawTransactionDto.setData(data);
            // 合约转账时value设置成0
            rawTransactionDto.setValue(BigInteger.ZERO);
        }

        RawTransactionSignRequest request = new RawTransactionSignRequest();
        request.setRawTransaction(rawTransactionDto);
        request.setFromAddress(fromAddress);
        GenericDto<String> response = sign(request);
        EthSendTransaction ethSendTransaction = web3.writeCli().ethSendRawTransaction(response.getData()).send();
        if (ethSendTransaction.hasError()) {
            log.error("error when sendTransaction, transaction err= {}", JsonUtils.writeValue(ethSendTransaction));
            throw new ChainException(String.format("error when sendTransaction, err= %s", JsonUtils.writeValue(ethSendTransaction)));
        }
        String txHash = ethSendTransaction.getTransactionHash();
        log.info("txHash={}", txHash);
        rawTransactionDto.setTxnHash(txHash);

        return rawTransactionDto;
    }

    @Override
    public BigDecimal chainQuoteAmount(Chain chain, String fromCurrency, String toCurrency, BigDecimal amountIn, Boolean needRoute) throws Exception {
        // todo: read reserves from redis
        ChainContractDto from = chainContractService.get(chain.getCode(), fromCurrency);
        ChainContractDto to = chainContractService.get(chain.getCode(), toCurrency);
        Assert.isTrue(fromCurrency != null && from != null, "invalid fromCurrency " + fromCurrency);
        Assert.isTrue(toCurrency != null && to != null, "invalid toCurrency " + toCurrency);

        BigInteger scaledAmountIn = scaleAmountByDecimal(amountIn, from.getDecimals());
        String routerAddress = systemWalletAddressService.getOne(chain, WalletAddressType.SWAP_ROUTER);
        log.info("chainQuoteAmount - chain={} routerAddress={} fromCurrency={} toCurrency={} amountIn={} decimal={} scaledAmountIn={}",
                chain, routerAddress, fromCurrency, toCurrency, amountIn, from.getDecimals(), scaledAmountIn);
        // if route required
        List<Type> inputParams;
        if (needRoute) {
            inputParams = Arrays.asList(new Uint256(scaledAmountIn),
                    new DynamicArray(Address.class, new Address(from.getAddress()), getChainSwapEthAddress(chain), new Address(to.getAddress()))
            );
        } else {
            inputParams = Arrays.asList(new Uint256(scaledAmountIn),
                    new DynamicArray(Address.class, new Address(from.getAddress()), new Address(to.getAddress()))
            );
        }

        Function function = new Function("getAmountsOut",
                inputParams,
                Arrays.asList(new TypeReference<DynamicArray<Uint256>>() {
                })
        );
        String data = FunctionEncoder.encode(function);

        Transaction transaction = Transaction.createEthCallTransaction(null, routerAddress, data);
        EthCall ethCall = web3.readCli().ethCall(transaction, DefaultBlockParameterName.LATEST).send();
        if (ethCall.hasError()) {
            log.error("chainQuoteAmount error, chain={} err= {}", chain, JsonUtils.writeValue(ethCall));
            throw new ChainException(String.format("chainQuoteAmount error, chain=%s err= %s", chain, JsonUtils.writeValue(ethCall)));
        }
        String result = ethCall.getValue();
        List<Type> types = FunctionReturnDecoder.decode(result, function.getOutputParameters());
        if (types != null && types.size() > 0) {
            List<Uint256> res = ((DynamicArray<Uint256>) types.get(0)).getValue();
            BigInteger value;
            if (needRoute) {
                Assert.isTrue(res != null && res.size() > 2, "chainQuoteAmount ethCall got result size less than 3");
                value = res.get(2).getValue();
            } else {
                Assert.isTrue(res != null && res.size() > 1, "chainQuoteAmount ethCall got result size less than 2");
                value = res.get(1).getValue();
            }
            int decimals = to.getDecimals();
            BigDecimal amountOut = unscaleAmountByDecimal(value, decimals);
            log.info("chainQuoteAmount - chain={} amountOut={} decimals={} scaledAmountOut={}", chain, value, decimals, amountOut);
            return amountOut;
        }
        return BigDecimal.ZERO;
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
        Assert.isTrue(fromCurrency != null && !fromCurrency.equals(toCurrency), "uniswap swap fromCurrency == toCurrency");
        ChainContractDto from = chainContractService.get(chain.getCode(), fromCurrency);
        ChainContractDto to = chainContractService.get(chain.getCode(), toCurrency);
        Assert.isTrue(fromCurrency != null && from != null, "invalid fromCurrency " + fromCurrency);
        Assert.isTrue(toCurrency != null && to != null, "invalid toCurrency " + toCurrency);

        BigInteger scaledAmountIn = scaleAmountByDecimal(amountIn, from.getDecimals());
        BigInteger scaledMinAmountOut = scaleAmountByDecimal(minAmountOut, to.getDecimals());

        // 查看当前余额是否足够
        BigDecimal holdAmount = getContractBalance(swapFromAddress, from, DefaultBlockParameterName.LATEST);
        BigInteger scaledHoldAmount = scaleAmountByDecimal(holdAmount, from.getDecimals());
        Assert.isTrue(scaledHoldAmount.compareTo(scaledAmountIn) >= 0, "not enough balance");

        if (needApprove) {
            chainApprove(chain, swapFromAddress, fromCurrency, amountIn);
        }

        String routerAddress = systemWalletAddressService.getOne(chain, WalletAddressType.SWAP_ROUTER);
        Function function;
        if (currentChain.getNativeToken().equalsIgnoreCase(fromCurrency)) {
            // 如果 from=eth，调用 swapExactETHForTokens
            function = new Function("swapExactETHForTokens",
                    Arrays.asList(
                            new Uint256(scaledMinAmountOut),
                            new DynamicArray(Address.class, new Address(from.getAddress()), new Address(to.getAddress())),
                            new Address(swapToAddress),
                            new Uint256(deadlineInSecond)),
                    Arrays.asList(new TypeReference<DynamicArray<Uint256>>() {
                    })
            );
        } else if (toCurrency.equals(currentChain.getNativeToken())) {
            // 如果 to=eth, 调用 swapExactTokensForETH
            function = new Function("swapExactTokensForETH",
                    Arrays.asList(
                            new Uint256(scaledAmountIn),
                            new Uint256(scaledMinAmountOut),
                            new DynamicArray(Address.class, new Address(from.getAddress()), new Address(to.getAddress())),
                            new Address(swapToAddress),
                            new Uint256(deadlineInSecond)),
                    Arrays.asList(new TypeReference<DynamicArray<Uint256>>() {
                    })
            );
        } else {
            // 其他, 调用 swapExactTokensForTokens
            // if route required
            List<Type> inputParams;
            if (needRoute) {
                inputParams = Arrays.asList(
                        new Uint256(scaledAmountIn),
                        new Uint256(scaledMinAmountOut),
                        new DynamicArray(Address.class, new Address(from.getAddress()), getChainSwapEthAddress(chain), new Address(to.getAddress())),
                        new Address(swapToAddress),
                        new Uint256(deadlineInSecond)
                );
            } else {
                inputParams = Arrays.asList(
                        new Uint256(scaledAmountIn),
                        new Uint256(scaledMinAmountOut),
                        new DynamicArray(Address.class, new Address(from.getAddress()), new Address(to.getAddress())),
                        new Address(swapToAddress),
                        new Uint256(deadlineInSecond)
                );
            }
            function = new Function("swapExactTokensForTokens", inputParams, Arrays.asList(new TypeReference<DynamicArray<Uint256>>() {
            }));
        }

        BigInteger pendingNonce = getPendingNonce(chain, swapFromAddress);
        long gasPrice = getGasPrice(chain);
        long gasLimit = getGasLimit(chain);
        String data = FunctionEncoder.encode(function);
        RawTransactionSignRequest rawTransactionSignRequest = new RawTransactionSignRequest();
        RawTransactionDto rawTransactionDto = new RawTransactionDto();
        rawTransactionDto.setNonce(pendingNonce);
        rawTransactionDto.setGasPrice(BigInteger.valueOf(gasPrice));
        rawTransactionDto.setGasLimit(BigInteger.valueOf(gasLimit));
        rawTransactionDto.setTo(routerAddress);
        rawTransactionDto.setData(data);
        rawTransactionSignRequest.setRawTransaction(rawTransactionDto);
        rawTransactionSignRequest.setFromAddress(swapFromAddress);
        if (currentChain.getNativeToken().equalsIgnoreCase(fromCurrency)) {
            rawTransactionDto.setValue(scaledAmountIn);
        } else {
            rawTransactionDto.setValue(BigInteger.ZERO);
        }
        String signTransaction = sign(rawTransactionSignRequest).getData();
        EthSendTransaction ethSendTransaction = web3.writeCli().ethSendRawTransaction(signTransaction).send();
        if (ethSendTransaction.hasError()) {
            log.error("error when send uniswap transaction, chain={} err= {}", chain, JsonUtils.writeValue(ethSendTransaction));
            throw new ChainException(String.format("error when send uniswap transaction, chain=%s err= %s", chain, JsonUtils.writeValue(ethSendTransaction)));
        }
        String txHash = ethSendTransaction.getTransactionHash();
        log.info("chainSwap chain={} txHash={}", chain, txHash);
        rawTransactionDto.setTxnHash(txHash);

        return rawTransactionDto;
    }

    // Reporter Address: the reporter role who signs the price data
    // Poster Address: the address who actually send out the transaction on chain
    @Override
    public RawTransactionDto reportKaptain(Chain chain, boolean isRetry, BigDecimal mcdPrice, BigDecimal deltaKusd, BigInteger nonce, Long timestamp, String reporterAddress, String posterAddress, long lastTimestamp) throws Exception {
        return null;
    }

    @Override
    public RawTransactionDto addDividend(Chain chain, BigDecimal amount, String dividendAddress, String toAddress, String currency) throws Exception {
        ChainContractDto contractConfig = chainContractService.get(chain.getCode(), currency);
        BigInteger scaledAmount = scaleAmountByDecimal(amount, contractConfig.getDecimals());
        // 检查 dividendAddress kine 余额是否足够
        BigDecimal holdAmount = getContractBalance(dividendAddress, contractConfig, DefaultBlockParameterName.LATEST);
        BigInteger scaledHoldAmount = scaleAmountByDecimal(holdAmount, contractConfig.getDecimals());
        Assert.isTrue(scaledHoldAmount.compareTo(scaledAmount) >= 0, "addDividend not enough in dividendAddress");
        // 调用 kine 合约执行划转奖励到 minter 合约，并返回txn hash
        return internalSendTransaction(chain, contractConfig.getCurrency(), dividendAddress, toAddress, amount, getGasPrice(chain), getGasLimit(chain));
    }

    @Override
    public RawTransactionDto notifyDividend(Chain chain, BigDecimal kineAmount, String dividendAddress, String minterAddress) throws Exception {
        ChainContractDto kineConfig = chainContractService.get(chain.getCode(), SymbolConstants.KINE);
        BigInteger scaledAmount = scaleAmountByDecimal(kineAmount, kineConfig.getDecimals());

        Function function = new Function("notifyRewardAmount", Arrays.asList(new Uint256(scaledAmount)), Collections.emptyList());
        String data = FunctionEncoder.encode(function);

        BigInteger pendingNonce = getPendingNonce(chain, dividendAddress);

        RawTransactionSignRequest rawTransactionSignRequest = new RawTransactionSignRequest();
        RawTransactionDto rawTransactionDto = new RawTransactionDto();
        rawTransactionDto.setNonce(pendingNonce);
        rawTransactionDto.setGasPrice(BigInteger.valueOf(getGasPrice(chain)));
        rawTransactionDto.setGasLimit(BigInteger.valueOf(getGasLimit(chain)));
        rawTransactionDto.setTo(minterAddress);
        rawTransactionDto.setData(data);
        rawTransactionDto.setValue(BigInteger.ZERO);

        rawTransactionSignRequest.setRawTransaction(rawTransactionDto);
        rawTransactionSignRequest.setFromAddress(dividendAddress);
        String signTransaction = sign(rawTransactionSignRequest).getData();
        EthSendTransaction ethSendTransaction = web3.writeCli().ethSendRawTransaction(signTransaction).send();
        if (ethSendTransaction.hasError()) {
            log.error("error when send notifyDividend transaction, err= {}", JsonUtils.writeValue(ethSendTransaction));
            throw new ChainException(String.format("error when send notifyDividend transaction, err= %s", JsonUtils.writeValue(ethSendTransaction)));
        }
        String txHash = ethSendTransaction.getTransactionHash();
        log.info("notifyDividend got txHash: {}", txHash);
        rawTransactionDto.setTxnHash(txHash);

        return rawTransactionDto;
    }

    /**
     * 1. UNISWAP_ONLY   4 使用Helper合约做计算，模拟view中的 fetchAnchorPrice 的过程
     * 2. REPORTER_ONLY  3 读view contract的 price mapping
     * 3. REPORTER       2 读view contract的 price mapping
     *
     * @param currency
     * @return
     */
    @Override
    public ChainOraclePricesDto getOraclePrices(Chain chain, Set<String> currency) throws IOException {
        return null;
    }

    @Override
    public ChainKTokenConfigDto getKTokenConfigBySymbol(Chain chain, String currency) throws IOException {
        return null;
    }

    @Override
    public ChainAnchorPriceView getAnchorPriceView(Chain chain, String currency, String oracleHelperAddress) throws IOException {
        return null;
    }

    @Override
    public RawTransactionDto replaceTransaction(Chain chain, RawTransactionDto rawTxn, String fromAddress) throws Exception {
        RawTransactionSignRequest rawTransactionSignRequest = new RawTransactionSignRequest();
        rawTransactionSignRequest.setRawTransaction(rawTxn);
        rawTransactionSignRequest.setFromAddress(fromAddress);
        String signTransaction = sign(rawTransactionSignRequest).getData();
        EthSendTransaction ethSendTransaction = web3.writeCli().ethSendRawTransaction(signTransaction).send();
        if (ethSendTransaction.hasError()) {
            log.error("error when replaceTransaction, err= {}", JsonUtils.writeValue(ethSendTransaction));
            throw new ChainException(String.format("error when replaceTransaction, err= %s", JsonUtils.writeValue(ethSendTransaction)));
        }
        String txHash = ethSendTransaction.getTransactionHash();
        log.info("replaceTransaction got txHash: {}", txHash);
        rawTxn.setTxnHash(txHash);

        return rawTxn;
    }

    @Override
    public RawTransactionDto transferOwnership(Chain chain, String address, String newOwnerAddress, String ownableContractAddress) throws Exception {
        return null;
    }

    /**
     * ETH current_circulating_supply = total_supply - treasury_hold - dispensers - ETHBridgeBalance
     *
     * @return
     * @throws IOException
     */
    @Override
    public BigDecimal getKineTotalSupply(Chain chain) throws IOException {
        String kineAddress = chainContractService.get(chain.getCode(), AccountConstants.KINE).getAddress();
        String kineTreasuryAddress = systemWalletAddressService.getOne(chain, WalletAddressType.KINE_TREASURY);
        // get Kine total supply
        BigDecimal kineTotalSupply = getTotalSupplyByAddressAndDecimals(chain, kineAddress, 18);

        // ETH current_circulating_supply = total_supply - treasury_hold - dispensers - crossChainBridgeLocked
        BigDecimal kineCurrentSupply = kineTotalSupply.subtract(getBalanceByAddress(chain, kineAddress, kineTreasuryAddress, 18));

        String seedDispenserAddress = systemWalletAddressService.getOne(chain, WalletAddressType.KINE_DISPENSER_SEED);
        String privateDispenserAddress = systemWalletAddressService.getOne(chain, WalletAddressType.KINE_DISPENSER_PRIVATE);
        // subtract dispensers balances
        kineCurrentSupply = kineCurrentSupply.subtract(getBalanceByAddress(chain, kineAddress, seedDispenserAddress, 18));
        kineCurrentSupply = kineCurrentSupply.subtract(getBalanceByAddress(chain, kineAddress, privateDispenserAddress, 18));

        String bridgeAddress = systemWalletAddressService.getOne(chain, WalletAddressType.KINE_BRIDGE);
        // subtract bridge balance
        kineCurrentSupply = kineCurrentSupply.subtract(getBalanceByAddress(chain, kineAddress, bridgeAddress, 18));

        return kineCurrentSupply;
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


    private String encodeOraclePriceMessage(Chain chain, Long timestamp, String symbol, BigInteger price) {
        final String kind = "prices";
        return encodeMessageByParams(chain,
                Arrays.asList(
                        new Utf8String(kind),
                        new Uint64(timestamp),
                        new Utf8String(symbol),
                        new Uint64(price)));
    }

    @Override
    public String encodeMessageByParams(Chain chain, List<Type> paramsToEncode) {
        return DEFAULT_ENCODER.encodeParameters(paramsToEncode);
    }

    private Address getChainSwapEthAddress(Chain chain) {
        ChainContractDto chainContractDto = chainContractService.get(chain.getCode(), "WETH");
        return new Address(chainContractDto.getAddress());
    }


    private boolean checkVolatilityAndTimeElapsed(long previousTs,
                                                  long lastTimestamp,
                                                  long currentTs,
                                                  BigDecimal previousPrice,
                                                  BigDecimal currentPrice,
                                                  BigDecimal reporterVolatility,
                                                  Long reporterPeriodInSeconds) {
        boolean mcdVolatilityExceed = previousPrice.compareTo(BigDecimal.ZERO) == 0
                || currentPrice.subtract(previousPrice)
                .divide(previousPrice, DEFAULT_SCALE, RoundingMode.HALF_EVEN)
                .abs().compareTo(reporterVolatility) >= 0;

        // Price change > 1%, or the time elapsed more than 30 mins, do post
        return mcdVolatilityExceed || currentTs - previousTs >= reporterPeriodInSeconds || currentTs - lastTimestamp >= reporterPeriodInSeconds;
    }


    @Override
    public ChainGasPriceDto getCurrentGasPriceOracle(Chain chain) {
        return null;
    }

    @Override
    @Async("executorPool")
    public void getAndMetricCurrentGasPriceOracle(Chain chain) {
        boolean enabled = actionControlService.isEnabled("chain", String.format("fetch_gas_price_%s", chain.getName().toLowerCase()));
        if (!enabled) {
            return;
        }
        ChainGasPriceDto chainGasPriceDto = getCurrentGasPriceOracle(chain);
        long gasPriceExpireTime = Long.parseLong(systemConfigService.getValue(AccountSystemConfig.CHAIN_GAS_PRICE_EXPIRE, "60"));
        log.info("currentGasPrice={}", chainGasPriceDto);
        RBucket<String> rBucket = client.getBucket(RedisKeys.getChainGasPriceKey(chain.getCode()), StringCodec.INSTANCE);
        rBucket.set(JsonUtils.writeValue(chainGasPriceDto), gasPriceExpireTime, TimeUnit.SECONDS);
    }

    @Override
    @ExecutionLock(key = "account:chain:exec:lock:{address}")
    public BigInteger getPendingNonce(Chain chain, String address) {
        try {
            return web3.writeCli().ethGetTransactionCount(address, DefaultBlockParameterName.PENDING).send().getTransactionCount();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @ExecutionLock(key = "account:chain:exec:lock:{address}")
    public BigInteger getSafeConfirmedNonce(Chain chain, String address) throws IOException {
        ChainBlock latestChainBlock = chainBlockMapper.getLatestBlock(chain);
        if (latestChainBlock == null) {
            return BigInteger.ZERO;
        }
        return web3.readCli().ethGetTransactionCount(
                address,
                DefaultBlockParameter.valueOf(
                        new BigInteger(
                                String.valueOf(latestChainBlock.getBlockNumber() - (long) (getConfirmBlocks(chain)))
                        )
                )
        ).send().getTransactionCount().subtract(BigInteger.ONE);
    }

    @Override
    public Boolean cancelTransaction(Chain chain, String txHash) throws Exception {
        log.info("cancelTransaction txHash={}", txHash);
        EthTransaction transaction = web3.readCli().ethGetTransactionByHash(txHash).send();
        if (transaction != null && transaction.getTransaction().isPresent()) {
            org.web3j.protocol.core.methods.response.Transaction tx = transaction.getResult();
            RawTransactionDto cancelRawTransactionDto = RawTransactionDto.builder()
                    .value(BigInteger.valueOf(0L))
                    .to(tx.getFrom())
                    .nonce(tx.getNonce())
                    .gasPrice(BigInteger.valueOf(getGasPrice(chain)))
                    .gasLimit(BigInteger.valueOf(getGasLimit(chain)))
                    .data("0x")
                    .build();

            RawTransactionSignRequest signRequest = new RawTransactionSignRequest();
            signRequest.setRawTransaction(cancelRawTransactionDto);
            signRequest.setFromAddress(tx.getFrom());
            GenericDto<String> signResponse = sign(signRequest);
            String signTransaction = signResponse.getData();

            EthSendTransaction ethSendTransaction = web3.writeCli().ethSendRawTransaction(signTransaction).send();
            return !ethSendTransaction.hasError();
        }
        return false;
    }

    @Override
    public String compensateNonceGap(Chain chain, String address, Long nonce) throws Exception {
        log.info("compensateNonceGap address={} nonce={}", address, nonce);
        RawTransactionDto cancelRawTransactionDto = RawTransactionDto.builder()
                .value(BigInteger.valueOf(1L))
                .to(address)
                .nonce(BigInteger.valueOf(nonce))
                .gasPrice(BigInteger.valueOf(getGasPrice(chain)))
                .gasLimit(BigInteger.valueOf(getGasLimit(chain)))
                .data("0x")
                .build();

        RawTransactionSignRequest signRequest = new RawTransactionSignRequest();
        signRequest.setRawTransaction(cancelRawTransactionDto);
        signRequest.setFromAddress(address);
        GenericDto<String> signResponse = sign(signRequest);
        String signTransaction = signResponse.getData();

        EthSendTransaction ethSendTransaction = web3.writeCli().ethSendRawTransaction(signTransaction).send();
        log.info("compensateNonceGap response={}", ethSendTransaction != null ? ethSendTransaction.getRawResponse() : null);
        return ethSendTransaction != null && ethSendTransaction.getError() != null ? ethSendTransaction.getTransactionHash() : null;
    }

    private long getSendTransactionSleep() {
        return Long.parseLong(systemConfigService.getValue(AccountSystemConfig.CHAIN_TRANSACTION_SEND_SLEEP, "3000"));
    }

    @Override
    public List<AddressBalanceDto> getBalancesOnBatch(Chain chain, List<String> addresses, Long sleepBetween) throws Exception {
        return null;
    }



    @Override
    public SignedMessageDto encodeAndSignDefiWithdraw(Chain chain, long id, String address, String currency, BigDecimal amount, long deadline) {
        ChainContractDto contract = chainContractService.get(chain.getCode(), currency);
        int decimals = contract.getDecimals();
        BigInteger value = amount.abs().multiply(BigDecimal.TEN.pow(decimals)).toBigInteger();
        // (uint256 id, address payable to, bool isETH, address currency, uint256 amount, uint256 deadline)
        List<Type> types = Arrays.asList(new Uint256(id), new Address(address),
                new Bool(Objects.equals(chain.getNativeToken(), contract.getCurrency())),
                new Address(contract.getAddress()), new Uint256(value), new Uint256(deadline));

        String encodedMessage = encodeMessageByParams(chain, types);
        String defiWithdrawSigner = systemWalletAddressService.getOne(chain, WalletAddressType.DEFI_DEPOSIT_WITHDRAW_SIGNER);

        return signMessage(chain, encodedMessage, defiWithdrawSigner);
    }

    @Override
    public SignedMessageDto signMessage(Chain chain, String message, String address) {
        List<String> messagesToSign = Lists.newArrayList(message);
        SignMessageRequest request = SignMessageRequest.builder().messagesToSign(messagesToSign).address(address).build();
        GenericDto<List<SignedMessageDto>> response = signMessages(request);
        return response.getData().get(0);
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
        return Convert.fromWei(new BigDecimal(gasUsed * gasPrice), Convert.Unit.ETHER);
    }

    /**
     * @param request
     * @return
     */
    private GenericDto<String> sign(RawTransactionSignRequest request) {
        // 2021-04-28 milo 统一添加 chain & chainId
        request.setChain(currentChain.getCode());
        request.setChainId(web3.getChainRpcConfig().getChainId());
        log.info("signMessages chain={} request={}", currentChain, request);

        GenericDto<String> response = walletFeignClient.sign(request);
        log.info("signMessages chain={} response={}", currentChain, response);
        return response;
    }

    /**
     * @param request
     * @return
     */
    private GenericDto<List<SignedMessageDto>> signMessages(SignMessageRequest request) {
        // 2021-04-28 milo 统一添加 chain & chainId
        request.setChain(currentChain.getCode());
        request.setChainId(web3.getChainRpcConfig().getChainId());
        log.info("signMessages chain={} request={}", currentChain, request);

        GenericDto<List<SignedMessageDto>> response = walletFeignClient.signMessages(request);
        log.info("signMessages chain={} response={}", currentChain, response);
        return response;
    }
}
