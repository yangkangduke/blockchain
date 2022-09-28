package com.seeds.account.enums;

import com.google.common.collect.Maps;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;

/**
 *
 * 钱包地址类型
 *
 */
@Getter
public enum WalletAddressType {
    /**
     * 热钱包
     */
    HOT(1, true),
    /**
     * 冷钱包
     */
    COLD(2, false),
    EXCHANGE(3, true),
    DIVIDEND(4, true),
    MCD_POSTER(5, true),
    MCD_REPORTER(6, true),

    KAPTAIN(7, false),
    ORACLE(8, false),
    ORACLE_HELPER(9, false),
    MINTER(10, false),
    SWAP_ROUTER(11, false),
    KINE_PROTOCOL_HELPER(12, false),
    KINE_UNITROLLER(13, false),

    KINE_TREASURY(14, false),
    KUSD_VAULT(15, false),
    AIRDROP_REWARD_SIGNER(16, true),
    AIRDROP_REWARD_CONTRACT(17, true),

    /**
     * 种子轮分发合约
     */
    KINE_DISPENSER_SEED(18, false),

    /**
     * 私募轮分发合约
     */
    KINE_DISPENSER_PRIVATE(19, false),

    KINE_RANCH(20, false),
    FARMING(21, false),
    /**
     * DEFI 充提签名
     */
    DEFI_DEPOSIT_WITHDRAW_SIGNER(22, true),

    /**
     * DEFI 充提合约
     */
    DEFI_DEPOSIT_WITHDRAW_CONTRACT(23, true),

    /**
     * kine bridge 合约
     */
    KINE_BRIDGE(24, false),

    /**
     * 其它, 有key
     */
    OTHER_WITH_KEY(99, true),

    /**
     * 其它, 无key
     */
    OTHER_WITHOUT_KEY(100, false),
    ;

    int code;
    boolean requirePrivateKey;

    WalletAddressType(int code, boolean requirePrivateKey) {
        this.code = code;
        this.requirePrivateKey = requirePrivateKey;
    }

    public static final Map<Integer, WalletAddressType> codeMap = Maps.newHashMap();
    static {
        Arrays.stream(values()).forEach(e -> codeMap.put(e.getCode(), e));
    }
    public static WalletAddressType fromCode(int code) {
        return codeMap.get(code);
    }
}
