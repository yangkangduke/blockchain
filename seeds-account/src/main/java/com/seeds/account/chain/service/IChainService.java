package com.seeds.account.chain.service;

import com.seeds.account.chain.dto.ChainTransaction;
import com.seeds.account.chain.dto.ChainTransactionReceipt;
import com.seeds.account.chain.dto.NativeChainBlockDto;
import com.seeds.account.chain.dto.NativeChainTransactionDto;
import com.seeds.account.dto.*;
import com.seeds.common.enums.Chain;
import com.seeds.wallet.dto.RawTransactionDto;
import com.seeds.wallet.dto.SignedMessageDto;
import org.web3j.abi.datatypes.Type;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 由于接口调用通过代理类反射调用， 所以接口方法不要使用基础数据类型。 否则代理调用失败，找不到对应的方法。
 *
 * @author yk
 */
public interface IChainService {

    Chain getCurrentChain(Chain chain);

    /**
     * 获取指定地址中的ETH以及ERC20合约资产余额
     *
     * @param addresses
     * @return
     * @throws Exception
     */
    List<Map<String, BigDecimal>> getBalances(Chain chain, List<String> addresses) throws Exception;

    /**
     * 使用batch方式获取地址余额
     *
     * @param addresses
     * @param sleepBetween
     * @return
     * @throws Exception
     */
    List<AddressBalanceDto> getBalancesOnBatch(Chain chain, List<String> addresses, Long sleepBetween) throws Exception;

    /**
     * 获取指定地址中的ETH以及ERC20合约资产余额
     *
     * @param address
     * @return
     * @throws Exception
     */
    Map<String, BigDecimal> getBalances(Chain chain, String address) throws Exception;

    /**
     * 创建指定数量的新地址
     *
     * @param numbers
     * @return
     * @throws Exception
     */
    List<String> createAddresses(Chain chain, Integer numbers) throws Exception;

    /**
     * 获取最新的块高度
     *
     * @return
     * @throws Exception
     */
    long getLatestBlockNumber(Chain chain) throws Exception;

    /**
     * Returns a transaction matching the given transaction hash.
     *
     * @param txHash
     * @return
     * @throws Exception
     */
    ChainTransaction getTransactionByTxHash(Chain chain, String txHash) throws Exception;

    /**
     * Returns the receipt of a transaction by transaction hash.
     * The receipt is not available for pending transactions and returns null.
     * status:  TRUE, if the transaction was successful, FALSE, if the EVM reverted the transaction.
     *
     * @param txHash
     * @return
     * @throws Exception
     */
    ChainTransactionReceipt getTransactionReceiptByTxHash(Chain chain, String txHash) throws Exception;

    /**
     * 获取ETH余额
     *
     * @param address
     * @return
     * @throws Exception
     */
    BigDecimal getChainTokenBalance(Chain chain, String address) throws Exception;

    /**
     * 获取合约资产余额
     *
     * @param address
     * @param currency
     * @return
     * @throws Exception
     */
    BigDecimal getContractBalance(Chain chain, String address, String currency) throws Exception;

    /**
     * @param address
     * @param chainContractDto
     * @return
     * @throws Exception
     */
    BigDecimal getContractBalance(Chain chain, String address, ChainContractDto chainContractDto) throws Exception;

    BigDecimal getContractDecimalValue(Chain chain, String currency, String decimalMethodName) throws Exception;

    /**
     * 获取块数据
     *
     * @param blockNumber
     * @return
     * @throws Exception
     */
    NativeChainBlockDto getChainNativeBlock(Chain chain, Long blockNumber) throws Exception;

    /**
     * 获取指定块中指定地址的交易数据
     *
     * @param chain
     * @param defiChain
     * @param blockNumber
     * @param addresses
     * @return
     * @throws Exception
     */
    List<NativeChainTransactionDto> getTransactions(Chain chain, Chain defiChain, Long blockNumber, List<String> addresses) throws Exception;

    /**
     * 发送转账交易
     *
     * @param currency
     * @param fromAddress
     * @param toAddress
     * @param amount
     * @param gasPrice
     * @param gasLimit
     * @return
     * @throws Exception
     */
    String sendTransaction(Chain chain, String currency, String fromAddress, String toAddress, BigDecimal amount, Long gasPrice, Long gasLimit) throws Exception;

    /**
     * 发送转账交易
     *
     * @param currency
     * @param fromAddress
     * @param toAddress
     * @param amount
     * @param gasPrice
     * @param gasLimit
     * @return
     * @throws Exception
     */
    RawTransactionDto internalSendTransaction(Chain chain, String currency, String fromAddress, String toAddress, BigDecimal amount, Long gasPrice, Long gasLimit) throws Exception;

    /**
     * @param currency
     * @param fromAddress
     * @param toAddress
     * @param amount
     * @param gasPrice
     * @param gasLimit
     * @param nonce
     * @param sleepFor
     * @return
     * @throws Exception
     */
    RawTransactionDto internalSendTransaction(Chain chain, String currency, String fromAddress, String toAddress, BigDecimal amount, Long gasPrice, Long gasLimit, Long nonce, Long sleepFor) throws Exception;

    /**
     * 在 链上 询价
     *
     * @param chain        链信息
     * @param fromCurrency 以该币种兑换
     * @param toCurrency   兑换成该币种
     * @param amountIn     要兑换的数量
     * @param needRoute    是否需要 eth route
     *                     e.g. kusd -> kine, router by kusd -> eth -> kine
     * @return 当前链上可兑换出来的数量
     * @throws Exception
     */
    BigDecimal chainQuoteAmount(Chain chain, String fromCurrency, String toCurrency, BigDecimal amountIn, Boolean needRoute) throws Exception;

    /**
     * approve allowance 允许chain router 花费指定金额
     *
     * @param chain    链信息
     * @param address  发起地址
     * @param currency 币种
     * @param amount   approve 数量
     * @return raw transaction with txn hash if successfully send transaction, otherwise return null
     * @throws Exception
     */
    RawTransactionDto chainApprove(Chain chain, String address, String currency, BigDecimal amount) throws Exception;

    /**
     * 查询 chain router allowance
     *
     * @param chain    链信息
     * @param currency 币种
     * @param address
     * @return currently allowance, otherwise throws exception
     * @throws Exception
     */
    BigDecimal queryChainAllowance(Chain chain, String currency, String address) throws Exception;

    /**
     * 在 chain swap 上兑换
     *
     * @param chain           链信息
     * @param fromCurrency    以该币种兑换
     * @param toCurrency      兑换成该币种
     * @param amountIn        要兑换的数量
     * @param swapFromAddress 兑换发起地址
     * @param swapToAddress   兑换接收地址
     * @param needApprove     是否需要approve 额度
     * @param needRoute       是否需要 eth route
     * @return raw transaction with txn hash if successfully send transaction, otherwise return null
     * @throws Exception
     */
    RawTransactionDto chainSwap(Chain chain, String fromCurrency, String toCurrency, BigDecimal amountIn, BigDecimal minAmountOut, Long deadlineInSecond, String swapFromAddress, String swapToAddress, Boolean needApprove, Boolean needRoute) throws Exception;

    /**
     * 调用 Kaptain 合约上报 MCD,KINE 价格以及 vault kUSD balance 变更
     * <p>
     * Reporter Address: the reporter role who signs the price data
     * Poster Address: the address who actually send out the transaction on chain
     *
     * @param chain           chain info
     * @param isRetry         重试上报，true - 直接上报 不做check
     * @param mcdPrice        mcd price
     * @param deltaKusd       vault kUSD balance 变更
     * @param nonce           expected report nonce
     * @param timestamp       时间戳（秒数，精确到分钟级）
     * @param reporterAddress reporter 地址
     * @param posterAddress   poster 地址
     * @param lastTimestamp   上次 mcd 上报时间（秒数，精确到分钟级）
     * @return raw transaction with txn hash if successfully send transaction, otherwise return null
     * @throws Exception
     */
    RawTransactionDto reportKaptain(Chain chain, boolean isRetry, BigDecimal mcdPrice, BigDecimal deltaKusd, BigInteger nonce, Long timestamp, String reporterAddress, String posterAddress, long lastTimestamp) throws Exception;

    /**
     * 调用 kine 合约，转一定数量的kine从分红地址到分红合约地址
     *
     * @param amount          待分发kine奖励数量
     * @param dividendAddress 分红地址
     * @param toAddress       toAddress
     * @param currency
     * @return raw transaction with txn hash if successfully send transaction, otherwise return null
     * @throws Exception
     */
    RawTransactionDto addDividend(Chain chain, BigDecimal amount, String dividendAddress, String toAddress, String currency) throws Exception;

    /**
     * 调用 minter 合约，通知合约新增的奖励并开始新的奖励分发周期
     *
     * @param kineAmount      新增奖励
     * @param dividendAddress 分红地址
     * @param minterAddress   kusdMinter 合约地址
     * @return raw transaction with txn hash if successfully send transaction, otherwise return null
     * @throws Exception
     */
    @Deprecated
    RawTransactionDto notifyDividend(Chain chain, BigDecimal kineAmount, String dividendAddress, String minterAddress) throws Exception;

    /**
     * 获取 oracle 中 currency 的报价和时间戳
     * <p>
     * MCD价格(from view contract)和timestamp(from OpenOraclePriceData)
     * 其他币的价格:
     * 1. UNISWAP_ONLY   使用Helper合约做计算，模拟view中的 fetchAnchorPrice 的过程
     * 2. REPORTER_ONLY  读view contract的 price mapping
     * 3. REPORTER       读view contract的 price mapping
     *
     * @param currency
     * @return currencies价格以及oracle中的时间戳
     */
    //TODO 通通修改
    ChainOraclePricesDto getOraclePrices(Chain chain, Set<String> currency) throws IOException;

    /**
     * 获取oracle中的kToken configuration
     *
     * @param chain
     * @param currency
     * @return
     * @throws IOException
     */
    ChainKTokenConfigDto getKTokenConfigBySymbol(Chain chain, String currency) throws IOException;

    /**
     * Get the estimated anchor price on chain
     *
     * @param currency
     * @param oracleAddress
     * @return
     */
    ChainAnchorPriceView getAnchorPriceView(Chain chain, String currency, String oracleAddress) throws IOException;

    /**
     * 尝试替换链上 txn, 注意 rawTxn 中的 nonce要和原先txn的nonce一样
     *
     * @param rawTxn      新的 txn
     * @param fromAddress 发送人
     * @return 含有新的 txn hash 的 rawTransaction
     * @throws Exception 替换失败，可能是 gas fee 给的不够高，或者txn已经被打包了
     */
    RawTransactionDto replaceTransaction(Chain chain, RawTransactionDto rawTxn, String fromAddress) throws Exception;

    /**
     * 调用 ownable contract, 转移owner
     *
     * @param address                发起地址，原owner
     * @param newOwnerAddress        新owner地址
     * @param ownableContractAddress 合约地址
     * @return raw transaction with txn hash if successfully send transaction, otherwise return null
     * @throws Exception
     */
    RawTransactionDto transferOwnership(Chain chain, String address, String newOwnerAddress, String ownableContractAddress) throws Exception;

    BigDecimal getKineTotalSupply(Chain chain) throws IOException;

    BigDecimal getKineProtocolTotalStakingValueInUSDC(Chain chain) throws IOException;

    /**
     * get each staking asset value
     *
     * @param addressAssetMap address, assetName
     * @return
     * @throws IOException
     */
    Map<String, BigDecimal> getKineProtocolAssetStakingValueInUSDC(Chain chain, Map<String, ChainContractDto> addressAssetMap) throws IOException;

    BigDecimal getTotalSupplyByAddressAndDecimals(Chain chain, String address, Integer decimals) throws IOException;

    BigDecimal getBalanceByAddress(Chain chain, String tokenAddress, String accountAddress, Integer tokenDecimal) throws IOException;

    /**
     * @return
     */
    ChainGasPriceDto getGasPriceDto(Chain chain);

    /**
     * 返回 fast gas price
     *
     * @return
     */
    Long getGasPrice(Chain chain);

    /**
     * @return
     */
    Long getGasLimit(Chain chain);

    /**
     * @param currency
     * @return
     */
    Long getGasLimit(Chain chain, String currency);

    List<ChainCurrencyGasLimitDto> getAllGasLimit(Chain chain);

    /**
     * @return
     */
    Integer getConfirmBlocks(Chain chain);

    /**
     * https://web3js.readthedocs.io/en/v1.2.0/web3-eth.html#getgasprice
     *
     * @return Returns the current gas price oracle. The gas price is determined by the last few blocks median gas price.
     */
    ChainGasPriceDto getCurrentGasPriceOracle(Chain chain) throws Exception;

    void getAndMetricCurrentGasPriceOracle(Chain chain) throws Exception;

    /**
     * get next pending nonce from chain
     * NOTE: MUST CALL THE WRITE CLIENT INSTEAD OF READ CLIENT
     *
     * @param chain
     * @param address
     * @return
     */
    BigInteger getPendingNonce(Chain chain, String address);

    BigInteger getSafeConfirmedNonce(Chain chain, String address) throws IOException;

    /**
     * 撤销一个交易
     *
     * @param txHash
     * @return
     * @throws Exception
     */
    Boolean cancelTransaction(Chain chain, String txHash) throws Exception;

    /**
     * 补一个空的交易
     *
     * @param address
     * @param nonce
     * @return
     */
    String compensateNonceGap(Chain chain, String address, Long nonce) throws Exception;


    String encodeMessageByParams(Chain chain, List<Type> paramsToEncode);

    /**
     * 签名
     *
     * @param message
     * @param address
     * @return
     */
    SignedMessageDto signMessage(Chain chain, String message, String address);

    /**
     * 获取领取状态, 返回此id被领取空投所在的blockNumber
     *
     * @param contractAddress
     * @param id
     * @return
     */
    Long getContractClaimHistory(Chain chain, String contractAddress, Long id) throws Exception;


    BigDecimal getTotalStakes(Chain chain, String address, int decimals) throws Exception;

    /**
     * 计算 transaction fee
     *
     * @param chain
     * @param gasUsed
     * @param gasPrice
     * @return
     */
    BigDecimal calcTxFee(Chain chain, Long gasUsed, Long gasPrice);

    /**
     * @param chain
     * @param id
     * @param address
     * @param currency
     * @param amount
     * @param deadline
     * @return
     */
    SignedMessageDto encodeAndSignDefiWithdraw(Chain chain, long id, String address, String currency, BigDecimal amount, long deadline);
}
