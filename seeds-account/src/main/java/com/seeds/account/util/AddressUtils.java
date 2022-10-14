package com.seeds.account.util;

import com.seeds.common.enums.Chain;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

/**
 *
 * @author yk
 *
 */
public class AddressUtils {

    public static String formalize(Chain chain, String address) {
        if (chain == Chain.TRON) {
            return address;
        } else {
            if (address != null && address.length() >0 && !address.startsWith("0x")) {
                return "0x" + address;
            }
            return address;
        }
    }

    public static boolean validate(Chain chain, String address) {
        if (chain == Chain.TRON) {
            return address != null && address.length() == 34 && address.startsWith("T");
        }
        if (address != null && address.startsWith("0x") && address.length() == 42) {
            try {
                BigInteger value = Numeric.toBigInt(address);
                return value.longValue() != 0;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public static String leftPad(String s, String prefix) {
        if (s != null && s.length() > 0 && !s.startsWith(prefix)) {
            return prefix + s;
        }
        return s;
    }
}
