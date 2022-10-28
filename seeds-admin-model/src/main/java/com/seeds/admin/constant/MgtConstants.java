package com.seeds.admin.constant;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.seeds.account.enums.WalletAddressType;
import com.seeds.common.enums.Chain;

import java.util.Map;
import java.util.Set;

import static com.seeds.account.enums.ChainDividendAction.*;
import static com.seeds.account.enums.WalletAddressType.*;

public class MgtConstants {

    public static final String KINE = "KINE";

    public static final String USDT = "USDT";

    public static final String USDC = "USDC";

    public static final String BNB = "BNB";

    public static final String NATIVE_TOKEN = "NATIVE_TOKEN";

    public static final Map<WalletAddressType, Integer> ADDRESS_TYPE = ImmutableMap.<WalletAddressType, Integer>builder()
            .put(MINTER, TO_MINTER.getCode())
            .put(KINE_RANCH, TO_KINE_RANCH.getCode())
            .put(FARMING, TO_FARMING.getCode())
            .build();

    public static Set<String> getNativeTokens() {
        Set<String> tokens = Sets.newHashSet();
        for (Chain chain : Chain.values()) {
            tokens.add(chain.getNativeToken());
        }
        return tokens;
    }

}
