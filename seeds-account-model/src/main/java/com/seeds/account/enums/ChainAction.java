package com.seeds.account.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.google.common.collect.Maps;
import com.seeds.common.enums.EnumWithCode;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;

/**
 *
 * 链操作
 *
 * @author milo
 *
 */
@Getter
public enum ChainAction {
    /**
     * 充币
     */
    DEPOSIT(1),
    /**
     * 提币
     */
    WITHDRAW(2);

    @EnumValue
    private Integer code;

    ChainAction(int code) {
        this.code = code;
    }

    public static final Map<Integer, ChainAction> codeMap = Maps.newHashMap();
    static {
        Arrays.stream(values()).forEach(e -> codeMap.put(e.getCode(), e));
    }
    public static ChainAction fromCode(int code) {
        return codeMap.get(code);
    }
}
