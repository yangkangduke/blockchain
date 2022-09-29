package com.seeds.account.util;

import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 *
 * @author yk
 *
 */
public class ObjectUtils {

    public static <T> T copy(Object source, T target) {
        BeanUtils.copyProperties(source, target);
        return target;
    }

    public static boolean isAddressEquals(String a, String b) {
        if (a == null || b == null) {
            return false;
        }
        return a.equalsIgnoreCase(b);
    }

    public static boolean containsAddress(List<String> addresses, String address) {
        return addresses.stream().anyMatch(e -> isAddressEquals(e, address));
    }

}
