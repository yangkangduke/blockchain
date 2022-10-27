package com.seeds.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 链类型
 *
 * @author yk
 */
@Getter
public enum Chain implements EnumWithCode  {

    /**
     * 以太坊 Ethereum
     */
    ETH(1, "ETH", "ETH", 18, "ERC20", null),

    /**
     * 币安智能链 Binance Smart Chain
     */
    BSC(2, "BSC", "BNB", 18, "BEP20", null),

    /**
     * Tron
     */
    TRON(3, "TRON", "TRX", 6, "TRC20", null),

    /**
     * OKExChain
     */
    OKC(4, "OKC", "OKT", 18, "OKEx-ERC20", null),

    /**
     * Heco
     */
    HECO(5, "HECO", "HT", 18, "HRC20", null),

    /**
     * Matic
     */
    MATIC(6, "MATIC", "MATIC", 18, "ERC20", null),


    /**
     * 支持DEFI充提的ETH链（虚拟链）
     */
    DEFI_ETH(101, "DEFI-ETH", ETH.getNativeToken(), 18, ETH.getTokenProtocol(), ETH),

    /**
     * 支持DEFI充提的BSC链（虚拟链）
     */
    DEFI_BSC(102, "DEFI-BSC", BSC.getNativeToken(), 18, BSC.getTokenProtocol(), BSC),

    /**
     * 支持DEFI充提的OKC链（虚拟链）
     */
    DEFI_OKC(104, "DEFI-OKC", OKC.getNativeToken(), 18, OKC.getTokenProtocol(), OKC),

    /**
     * 支持DEFI充提的HECO链（虚拟链）
     */
    DEFI_HECO(105, "DEFI-HECO", HECO.getNativeToken(), 18, HECO.getTokenProtocol(), HECO),

    /**
     * 支持DEFI充提的Matic链（虚拟链）
     */
    DEFI_MATIC(106, "DEFI-MATIC", MATIC.getNativeToken(), 18, MATIC.getTokenProtocol(), MATIC),
    ;

    /**
     * 支持的原生区块链
     */
//    public static List<Chain> SUPPORT_LIST = Lists.newArrayList(ETH, BSC, TRON, MATIC);
    public static List<Chain> SUPPORT_LIST = Lists.newArrayList(ETH, TRON);

    /**
     * 支持gas price 区块链
     */
    public static List<Chain> SUPPORT_GAS_PRICE_LIST = Lists.newArrayList(ETH, BSC, MATIC);

    /**
     * 支持创建和分配充币地址的链
     */
    public static List<Chain> SUPPORT_CREATE_ADDRESS_LIST = Lists.newArrayList(ETH, BSC, TRON, MATIC);

    /**
     * 支持kine bridge的链(计算kine流通量使用)
     */
    public static List<Chain> SUPPORT_KINE_BRIDGE_LIST = Lists.newArrayList(ETH, BSC, MATIC);

    /**
     * 支持质押的链
     */
    public static List<Chain> SUPPORT_STAKING_LIST = Lists.newArrayList(ETH, BSC, MATIC);

    /**
     * 支持分红的链
     */
    public static List<Chain> SUPPORT_DIVIDEND_LIST = Lists.newArrayList(ETH, BSC, MATIC);

    /**
     * 支持兑换的链
     */
    public static List<Chain> SUPPORT_EXCHANGE_LIST = Lists.newArrayList(ETH, BSC, MATIC);

    /**
     * 支持DEFI充提的链
     */
    public static List<Chain> SUPPORT_DEFI_LIST = Lists.newArrayList(DEFI_ETH, DEFI_BSC, DEFI_MATIC);

    /**
     * 存在Nonce的链
     */
    public static List<Chain> SUPPORT_NONCE_LIST = Lists.newArrayList(ETH, BSC, MATIC);

    /**
     * map chain
     *
     * @param chain
     * @return
     */
    public static Chain mapChain(Chain chain) {
        return (chain == Chain.BSC || chain == Chain.OKC || chain == Chain.HECO || chain == Chain.MATIC) ? Chain.ETH : chain;
    }

    /**
     * chain code
     */
    @EnumValue
    private Integer code;

    /**
     * chain name
     */
    private String name;

    /**
     * 原生代币
     */
    private String nativeToken;

    /**
     * 原生代币的精度
     */
    private int decimals;

    /**
     * 代币协议
     */
    private String tokenProtocol;

    /**
     *
     */
    private Chain relayOn;


    Chain(int code, String name, String nativeToken, int decimals, String tokenProtocol, Chain relayOn) {
        this.code = code;
        this.name = name;
        this.nativeToken = nativeToken;
        this.decimals = decimals;
        this.tokenProtocol = tokenProtocol;
        this.relayOn = relayOn;
    }

    public static final Map<Integer, Chain> codeMap = Maps.newHashMap();

    static {
        Arrays.stream(values()).forEach(e -> codeMap.put(e.getCode(), e));
    }

    public static Chain fromCode(int code) {
        return codeMap.get(code);
    }

    public static Chain fromName(String name) {
        return Arrays.stream(values()).filter(e -> Objects.equals(name, e.getName())).findFirst().orElse(null);
    }

    public static Chain fromToken(String token) {
        return Arrays.stream(values()).filter(e -> Objects.equals(token, e.getNativeToken())).findFirst().orElse(null);
    }

    /**
     * 返回chain支持的DEFI-Chain
     *
     * @param chain
     * @return
     */
    public static Chain supportedDefiChain(Chain chain) {
        return SUPPORT_DEFI_LIST.stream().filter(e -> e.getRelayOn() == chain).findFirst().orElse(null);
    }
}
