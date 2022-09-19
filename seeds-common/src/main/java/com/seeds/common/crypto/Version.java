package com.seeds.common.crypto;

/**
 * @author rickierao
 * @email antilaw@yahoo.com
 * @date 2021/1/21
 */

import lombok.Getter;

import java.util.Objects;

/**
 * The authentication contract version between the client and server
 */
@Getter
public enum Version {
    /**
     * Version 1
     * <p> - Has mixed implementations for including content data in signature (deprecated)
     */
    @Deprecated
    V1("1", true),

    /**
     * Version 2
     * <p> - will not include content data in signature
     */
    V2("2", false),

    /**
     * Version 3
     * <p> - will include content data in signature
     */
    V3("3", true),

    ;

    private final String value;
    private final boolean dataInSignature;

    Version(String value, boolean dataInSignature) {
        this.value = value;
        this.dataInSignature = dataInSignature;
    }

    public static Version from(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Version value cannot be null");
        }
        for (Version version : Version.values()) {
            if (Objects.equals(value, version.value)) {
                return version;
            }
        }
        throw new IllegalArgumentException(value + " does not have valid mapping in Version");
    }
}
