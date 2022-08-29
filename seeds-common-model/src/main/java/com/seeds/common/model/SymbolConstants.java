package com.seeds.common.model;

import com.seeds.common.exception.SeedsException;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@UtilityClass
public class SymbolConstants {

    public static final String SYMBOL_TYPE_COMPOUND = "compound";
    public static final String SYMBOL_TYPE_REAL = "real";
    public static final String SYMBOL_TYPE_COMPOUND_REAL = "compound-real";
    public static final String SYMBOL_TYPE_DEBT = "debt";
    public static final String SYMBOL_TYPE_ALL = "all";

    /* compound asset */
    public static final String KBTC = "kBTC";
    public static final String KBCH = "kBCH";
    public static final String KETH = "kETH";
    public static final String KLTC = "kLTC";
    public static final String KEOS = "kEOS";

    public static final String KUSD = "kUSD";
    /* real asset  */
    public static final String KINE = "KINE";
    public static final String USDT = "USDT";
    public static final String USDC = "USDC";
    public static final String DAI = "DAI";
    public static final String PAX = "PAX";
    public static final String ETH = "ETH";
    public static final String WBTC = "WBTC";
    public static final String WETH = "WETH";
    /* debt asset */
    public static final String MCD = "MCD";

    /* fx */
    public static final String USDCHY = "usdcny";

    /**
     * @return
     */
    public static List<String> getSymbolsByType(String type) {
        switch (type) {
            case SYMBOL_TYPE_COMPOUND:
                return getCompoundSymbols();
            case SYMBOL_TYPE_REAL:
                return getRealSymbols();
            case SYMBOL_TYPE_COMPOUND_REAL:
                return getCompoundAndRealSymbols();
            case SYMBOL_TYPE_DEBT:
                return getDebtSymbols();
            case SYMBOL_TYPE_ALL:
                return getAllSymbols();
            default:
                throw new SeedsException("unknown type is given: " + type);
        }
    }

    /**
     * Get compound assets
     *
     * @return List<String> contains KBTC, KBCH, KETH, KLTC, KEOS
     */
    public static List<String> getFxSymbols() {
        return Arrays.asList(USDCHY);
    }

    /**
     * Get compound assets
     *
     * @return List<String> contains KBTC, KBCH, KETH, KLTC, KEOS
     */
    public static List<String> getCompoundSymbols() {
        return Arrays.asList(KBTC, KETH, KBCH, KLTC, KEOS);
    }

    /**
     * Get compound assets with kusd
     *
     * @return List<String> contains KBTC, KBCH, KETH, KLTC, KEOS, KUSD
     */
    public static List<String> getCompoundSymbolsWithKusd() {
        return Arrays.asList(KBTC, KBCH, KETH, KLTC, KEOS, KUSD);
    }

    /**
     * Get real assets
     *
     * @return List<String> contains KUSD, KINE
     */
    public static List<String> getRealSymbols() {
        return Arrays.asList(KINE);
    }

    /**
     * Get compound and real symbols
     *
     * @return
     */
    public static List<String> getCompoundAndRealSymbols() {
        List<String> result = new ArrayList();
        result.addAll(getCompoundSymbols());
        result.addAll(getRealSymbols());
        return result;
    }

    /**
     * Get debt symbols
     *
     * @return
     */
    public static List<String> getDebtSymbols() {
        return Arrays.asList(MCD);
    }

    /**
     * Get all
     *
     * @return
     */
    public static List<String> getAllSymbols() {
        List<String> result = new ArrayList<>();
        result.addAll(getCompoundAndRealSymbols());
        result.addAll(getDebtSymbols());
        return result;
    }

}
