package com.seeds.common.enumeration;

/**
 * @author old meng
 */
public interface EnumWithCode {
    static <T extends EnumWithCode> T fromCode(Class<T> enumClass, Short code) {
        if (code == null) {
            throw new IllegalArgumentException("EnumWithCode code should not be null");
        }

        if (enumClass.isAssignableFrom(EnumWithCode.class)) {
            throw new IllegalArgumentException("Illegal EnumWithCode type");
        }

        T[] enums = enumClass.getEnumConstants();
        for (T t : enums) {
            if (t.getCode().equals(code)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Cannot parse integer: " + code + " to " + enumClass.getName());
    }

    Short getCode();
}
