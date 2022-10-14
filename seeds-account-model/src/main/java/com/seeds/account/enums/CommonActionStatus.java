package com.seeds.account.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.collect.Maps;
import com.seeds.common.enums.EnumWithCode;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;

/**
 *
 * 通用状态
 *
 * @author yk
 *
 */
@Getter
public enum CommonActionStatus {
    /**
     * 成功
     */
    SUCCESS(1),
    /**
     * 失败
     */
    FAILED(2),
    /**
     * 进行中
     */
    PROCESSING(3);

    @JsonValue
    @EnumValue
    private Integer code;

    CommonActionStatus(int code) {
        this.code = code;
    }


    public static final Map<Integer, CommonActionStatus> codeMap = Maps.newHashMap();
    static {
        Arrays.stream(values()).forEach(e -> codeMap.put(e.getCode(), e));
    }
    public static CommonActionStatus fromCode(int code) {
        return codeMap.get(code);
    }
}
