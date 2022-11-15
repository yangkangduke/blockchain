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
import com.seeds.account.service.IChainContractService;
import com.seeds.account.service.IChainDepositService;
import com.seeds.account.service.ISystemConfigService;
import com.seeds.account.util.AddressUtils;
import com.seeds.account.util.JsonUtils;
import com.seeds.account.util.ObjectUtils;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.enums.Chain;
import com.seeds.common.enums.ErrorCode;
import com.seeds.common.exception.ChainException;
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
 * @author yk
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
    private IChainProviderService chainProviderService;
    @Autowired
    private TRONGridClient tronClient;
    @Autowired
    private IChainDepositService chainDepositService;
    @Autowired
    private ISystemConfigService systemConfigService;
    @Autowired
    private WalletFeignClient walletFeignClient;
    @Autowired
    private TRONChainConverter converter;
    @Autowired
    private IChainContractService chainContractService;


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

    @Override
    public List<AddressBalanceDto> getBalancesOnBatch(Chain chain, List<String> addresses, Long sleepBetween) throws Exception {
        long startTime = System.currentTimeMillis();
        List<AddressBalanceDto> list = Lists.newArrayList();

        // 找出充币币种
        List<String> currencyList = chainDepositService.getAllDepositRules()
                .stream()
                .filter(e -> e.getChain() == chain.getCode())
                .map(DepositRuleDto::getCurrency)
                .collect(Collectors.toList());
        for (String address : addresses) {
            Map<String, BigDecimal> balances = Maps.newHashMap();
            balances.put(chain.getNativeToken(), getChainTokenBalance(chain, address));
            for (String currency : currencyList) {
                BigDecimal balance = getContractBalance(chain, address, currency);
                balances.put(currency, balance);
            }
            list.add(AddressBalanceDto.builder().address(address).balances(balances).build());
        }

        long endTime = System.currentTimeMillis();
        log.info("getBalancesOnBatch chain={} address.size={} took.all={}", chain, addresses.size(), (endTime - startTime));

        return list;
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
        ChainContractDto chainContractDto = chainContractService.get(chain.getCode(), currency);
        log.info("chainContractDto{}", chainContractDto);
        return getContractBalance(chain, address, chainContractDto);
    }

    @Override
    public BigDecimal getContractBalance(Chain chain, String address, ChainContractDto chainContractDto) throws Exception {
        return getContractBalance(address, chainContractDto);
    }

    private BigDecimal getContractBalance(String address, ChainContractDto chainContractDto) throws Exception {
        String contractAddress = chainContractDto.getAddress();

        //This is core.contract.Contract, not from the proto
        Contract contract = tronClient.cli().getContract(contractAddress);
        log.info("---contract {}---", contract);
        Trc20Contract token = new Trc20Contract(contract, address, tronClient.cli());
        log.info("---Trc20Contract {}---", token);
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
        ChainContractDto contractConfigDto = chainContractService.get(chain.getCode(), currency);
        Function function = new Function(decimalMethodName, new ArrayList<>(), Arrays.asList(new TypeReference<Uint256>() {
        }));

        String contractAddress = contractConfigDto.getAddress();
        log.debug("contractAddress={} currency={} decimalMethodName={} decimals={}",
                contractAddress, contractConfigDto.getCurrency(), decimalMethodName, contractConfigDto.getDecimals());

        Response.TransactionExtention txnExt = tronClient.cli().constantCall(contractAddress, contractAddress, function);
        String result = Numeric.toHexString(txnExt.getConstantResult(0).toByteArray());
        List<org.tron.trident.abi.datatypes.Type> types = FunctionReturnDecoder.decode(result, function.getOutputParameters());

        if (types != null && types.size() > 0) {
            BigInteger value = ((Uint256) types.get(0)).getValue();
            int decimals = contractConfigDto.getDecimals();
            BigDecimal amount = unscaleAmountByDecimal(value, decimals);
            return amount;
        }
        throw new ChainException("failed to call the chain");
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
        try {
            return getTransactions0(tronClient.cli(), chain, defiChain, blockNumber, addresses);
        } catch (IllegalException e) {
            if (DEFAULT_ILLEGAL_EXCEPTION_MSG.equals(e.getMessage())) {
                log.warn("getTransactions exception: {}", e.getMessage(), e);
                return null;
            } else {
                throw e;
            }
        }
    }

    private List<NativeChainTransactionDto> getTransactions0(ApiWrapper cli, Chain chain, Chain defiChain, Long blockNumber, List<String> addresses) throws Exception {
        List<NativeChainTransactionDto> ethereumTransactions = Lists.newArrayList();

        String defiContractAddress = getSystemWalletAddressService().getOne(chain, WalletAddressType.DEFI_DEPOSIT_WITHDRAW_CONTRACT);

        Response.BlockExtention block = tronClient.cli().getBlockByLimitNext(blockNumber, blockNumber + 1).getBlock(0);
        List<Response.TransactionExtention> transactionResults = block.getTransactionsList();
        transactionResults.forEach(transactionObject -> {
            org.tron.trident.proto.Chain.Transaction.Contract contract = transactionObject.getTransaction().getRawData().getContract(0);
            if (contract.getType().equals(org.tron.trident.proto.Chain.Transaction.Contract.ContractType.TransferContract)
                    && transactionObject.getTransaction().getRet(0).getRet() == org.tron.trident.proto.Chain.Transaction.Result.code.SUCESS
                    && transactionObject.getTransaction().getRet(0).getContractRet().equals(org.tron.trident.proto.Chain.Transaction.Result.contractResult.SUCCESS)) {
                // TRX 转账 且 成功的
                String toAddress = null;
                String fromAddress = null;
                org.tron.trident.proto.Contract.TransferContract transferContract = null;
                try {
                    transferContract = contract.getParameter().unpack(org.tron.trident.proto.Contract.TransferContract.class);
                    toAddress = Base58Check.bytesToBase58(transferContract.getToAddress().toByteArray());
                    fromAddress = Base58Check.bytesToBase58(transferContract.getOwnerAddress().toByteArray());
                } catch (InvalidProtocolBufferException e) {
                    log.error("decode toAddress error: {}", e.getMessage(), e);
                    // 如果报错就不处理
                    return;
                }

                if (containsAddress(addresses, toAddress)) {
                    // 是用户的地址充币
                    String currency = currentChain.getNativeToken();
                    int decimals = currentChain.getDecimals();

                    long blockTime = block.getBlockHeader().getRawData().getTimestamp() / 1000;
                    String blockHash = ApiWrapper.toHex(block.getBlockid());

                    // sun
                    BigInteger value = BigInteger.valueOf(transferContract.getAmount());
                    // amount
                    BigDecimal amount = unscaleAmountByDecimal(value, decimals);
                    // 不解析手续费
                    BigDecimal txFee = BigDecimal.ZERO;

                    NativeChainTransactionDto nativeChainTransactionDto = NativeChainTransactionDto.builder()
                            .chain(chain)
                            .blockNumber(blockNumber)
                            .blockHash(blockHash)
                            .txHash(ApiWrapper.toHex(transactionObject.getTxid()))
                            .txTime(blockTime)
                            .fromAddress(fromAddress)
                            .toAddress(toAddress)
                            .currency(currency)
                            .amount(amount)
                            .txFee(txFee)
                            .build();
                    ethereumTransactions.add(nativeChainTransactionDto);
                }

            } else if (contract.getType().equals(org.tron.trident.proto.Chain.Transaction.Contract.ContractType.TriggerSmartContract)
                    && transactionObject.getTransaction().getRet(0).getRet() == org.tron.trident.proto.Chain.Transaction.Result.code.SUCESS
                    && transactionObject.getTransaction().getRet(0).getContractRet().equals(org.tron.trident.proto.Chain.Transaction.Result.contractResult.SUCCESS)) {

                // 触发智能合约， TRC-20转账？
                String contractAddress;
                String fromAddress;
                org.tron.trident.proto.Contract.TriggerSmartContract triggerSmartContract;
                try {
                    triggerSmartContract = contract.getParameter().unpack(org.tron.trident.proto.Contract.TriggerSmartContract.class);
                    contractAddress = Base58Check.bytesToBase58(triggerSmartContract.getContractAddress().toByteArray());
                    fromAddress = Base58Check.bytesToBase58(triggerSmartContract.getOwnerAddress().toByteArray());
                } catch (InvalidProtocolBufferException e) {
                    log.error("decode toAddress error: {}", e.getMessage(), e);
                    // 如果报错就不处理
                    return;
                }

                chainContractService.getAll().forEach(contractConfigDto -> {
                    if (contractConfigDto.getChain() == chain.getCode() && ObjectUtils.isAddressEquals(contractConfigDto.getAddress(), contractAddress)) {
                        decodeContractTransaction(chain, defiChain, defiContractAddress,
                                contractConfigDto, addresses, ethereumTransactions, block, transactionObject, triggerSmartContract);
                    }
                });
            }
        });

        return ethereumTransactions;
    }

    private void decodeContractTransaction(Chain chain, Chain defiChain, String defiContractAddress, ChainContractDto contractConfigDto, List<String> addresses, List<NativeChainTransactionDto> ethereumTransactions, Response.BlockExtention block, Response.TransactionExtention transactionObject, org.tron.trident.proto.Contract.TriggerSmartContract triggerSmartContract) {
        ContractTransferParamsDto params = decodeInputAsTransfer(ApiWrapper.toHex(triggerSmartContract.getData()));
        if (params == null) {
            return;
        }
        // 判断充币地址是否为交易所分配给用户的充币地址
        if (containsAddress(addresses, params.getToAddress())) {
            NativeChainTransactionDto nativeChainTransactionDto
                    = decodeContractTransaction0(chain, defiChain, defiContractAddress, contractConfigDto, addresses, ethereumTransactions, block, transactionObject, params, triggerSmartContract);
            if (nativeChainTransactionDto != null) {
                nativeChainTransactionDto.setChain(chain);
                ethereumTransactions.add(nativeChainTransactionDto);
            }
        }
        // 如果是defi充币（此处并不判断fromAddress是否是交易所中已经绑定用户Id的用户，留给后面的业务处理）
        else if (defiChain != null && ObjectUtils.isAddressEquals(defiContractAddress, params.getToAddress())) {
            NativeChainTransactionDto nativeChainTransactionDto =
                    decodeContractTransaction0(chain, defiChain, defiContractAddress, contractConfigDto, addresses, ethereumTransactions, block, transactionObject, params, triggerSmartContract);
            if (nativeChainTransactionDto != null) {
                nativeChainTransactionDto.setChain(defiChain);
                ethereumTransactions.add(nativeChainTransactionDto);
            }
        }
    }

    private NativeChainTransactionDto decodeContractTransaction0(Chain chain, Chain defiChain, String defiContractAddress,
                                                                 ChainContractDto contractConfigDto, List<String> addresses,
                                                                 List<NativeChainTransactionDto> ethereumTransactions,
                                                                 Response.BlockExtention block, Response.TransactionExtention transactionObject, ContractTransferParamsDto params,
                                                                 org.tron.trident.proto.Contract.TriggerSmartContract triggerSmartContract) {
        // block number
        long blockNumber = block.getBlockHeader().getRawData().getNumber();
        // block hash
        String blockHash = ApiWrapper.toHex(block.getBlockid());
        // transaction hash
        String txHash = ApiWrapper.toHex(transactionObject.getTxid());

        log.info("decodeContractTransaction contractConfigDto={} params={} input={} value={} blockNumber={} blockHash={} txHash={} from={}",
                contractConfigDto, params, transactionObject.getTransaction().getRawData().getData().toStringUtf8(), "dummy", blockNumber, blockHash, txHash, "dummy");

        String currency = contractConfigDto.getCurrency();
        int decimals = contractConfigDto.getDecimals();
        long blockTime = block.getBlockHeader().getRawData().getTimestamp() / 1000;

        // from address
        String fromAddress = Base58Check.bytesToBase58(triggerSmartContract.getOwnerAddress().toByteArray());
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

    private ContractTransferParamsDto decodeInputAsTransfer(String input) {
        if (input != null && (input.length() == 136 || input.length() == 200)) {
            try {
                String methodId = input.substring(0, 8);
                // transfer(address to, uint256 value)
                if (Objects.equals("a9059cbb", methodId)) {
                    String to = input.substring(8, 72);
                    String value = input.substring(72, 136);
                    Address toAddress = (Address) DEFAULT_DECODER.getDecoderMethod().invoke(null, to, 0, Address.class);
                    Uint256 amount = (Uint256) DEFAULT_DECODER.getDecoderMethod().invoke(null, value, 0, Uint256.class);

                    ContractTransferParamsDto paramsDto = new ContractTransferParamsDto();
                    paramsDto.setMethodId(methodId);
                    paramsDto.setToAddress(toAddress.getValue());
                    paramsDto.setValue(amount.getValue());
                    return paramsDto;
                }
                // transferFrom(address from, address to, uint256 value)
                else if (Objects.equals("23b872dd", methodId)) {
                    String from = input.substring(8, 72);
                    String to = input.substring(72, 136);
                    String value = input.substring(136, 200);
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

    private boolean containsAddress(List<String> addresses, String address) {
        return addresses.stream().anyMatch(e -> e.equalsIgnoreCase(address));
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

    private long getSendTransactionSleep() {
        return Long.parseLong(systemConfigService.getValue(AccountSystemConfig.CHAIN_TRANSACTION_SEND_SLEEP, "3000"));
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

        RawTransactionDto rawTransactionDto = new RawTransactionDto();
        rawTransactionDto.setGasLimit(BigInteger.valueOf(gasLimit));
        rawTransactionDto.setNonce(BigInteger.valueOf(nonce));
        rawTransactionDto.setGasPrice(BigInteger.valueOf(gasPrice));

        // 2021-05-21 milo
        // 由于Tron没有GasPrice的概念，因此交易的GasLimit使用参数中的 gasPrice * gasLimit来处理
        Assert.isTrue(gasLimit == 1, "gasLimit must be 1");
        //Assert.isTrue(gasPrice <= 10_000_000, "gasPrice less than 10_000_000");

        if (Objects.equals(currentChain.getNativeToken(), currency)) {
            int decimals = currentChain.getDecimals();
            BigInteger value = scaleAmountByDecimal(amount, decimals);
            rawTransactionDto.setTo(toAddress);
            rawTransactionDto.setData("");
            rawTransactionDto.setValue(value);

            // 构建发送TRX的交易（TransactionExtention）
            Response.TransactionExtention transactionExtention = tronClient.cli().transfer(fromAddress, toAddress, value.longValue());

            // 对交易进行编码
            byte[] rawData = transactionExtention.toByteArray();
            rawTransactionDto.setTronTransactionExtensionBase64(Base64.getEncoder().encodeToString(rawData));

            // 签名
            RawTransactionSignRequest request = new RawTransactionSignRequest();
            request.setRawTransaction(rawTransactionDto);
            request.setFromAddress(fromAddress);
            GenericDto<String> response = sign(request);

            // 解码签名交易
            byte[] sourceData = Base64.getDecoder().decode(response.getData());
            org.tron.trident.proto.Chain.Transaction txn = org.tron.trident.proto.Chain.Transaction.parseFrom(sourceData);

            // 广播交易并得到Hash
            String txnHash = tronClient.cli().broadcastTransaction(txn);
            log.info("txHash={}", txnHash);
            rawTransactionDto.setTxnHash(txnHash);
        } else {
            ChainContractDto contractConfigDto = chainContractService.get(chain.getCode(), currency);
            Assert.isTrue(contractConfigDto != null, "Currency contract config not found");

            String contractAddress = contractConfigDto.getAddress();
            int decimals = contractConfigDto.getDecimals();
            BigInteger value = scaleAmountByDecimal(amount, decimals);

            // 构建TRC20的交易
            Function function = new Function("transfer", Arrays.asList(new Address(toAddress), new Uint256(value)), Collections.emptyList());
            TransactionBuilder transactionBuilder = tronClient.cli().triggerCall(fromAddress, contractAddress, function);
            transactionBuilder.setFeeLimit(gasLimit * gasPrice);
            org.tron.trident.proto.Chain.Transaction transaction = transactionBuilder.build();
            byte[] rawData = transaction.toByteArray();
            rawTransactionDto.setTronTransactionBase64(Base64.getEncoder().encodeToString(rawData));

            // 发给合约地址
            rawTransactionDto.setTo(contractAddress);
            rawTransactionDto.setData("");
            // 合约转账时value设置成0
            rawTransactionDto.setValue(BigInteger.ZERO);

            RawTransactionSignRequest request = new RawTransactionSignRequest();
            request.setRawTransaction(rawTransactionDto);
            request.setFromAddress(fromAddress);
            GenericDto<String> response = sign(request);

            // 1. decodeBase64String
            byte[] sourceData = Base64.getDecoder().decode(response.getData());
            // 2. parseFrom
            org.tron.trident.proto.Chain.Transaction txn = org.tron.trident.proto.Chain.Transaction.parseFrom(sourceData);
            String txnHash = tronClient.cli().broadcastTransaction(txn);
            log.info("txHash={}", txnHash);
            rawTransactionDto.setTxnHash(txnHash);
        }

        return rawTransactionDto;
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
