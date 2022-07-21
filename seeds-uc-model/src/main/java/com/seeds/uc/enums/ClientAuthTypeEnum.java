package com.seeds.uc.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.seeds.common.exception.SeedsException;
import com.seeds.uc.exceptions.GenericException;
import lombok.Getter;

import java.util.EnumSet;
import java.util.Set;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/1
 */
@Getter
public enum ClientAuthTypeEnum {
    UNKNOWN((short) 0, "unknown"),
    // 手机验证
    PHONE((short) 1, "phone"),
    // 邮箱验证
    EMAIL((short) 2, "email"),
    // 谷歌验证
    GA((short) 3, "ga"),
    ;

    public static Set<ClientAuthTypeEnum> NEED_SEND_CODE = EnumSet.of(PHONE, EMAIL);
    @EnumValue
    @JsonValue
    private final Short code;
    private final String desc;

    ClientAuthTypeEnum(Short code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ClientAuthTypeEnum from(Short code) {
        switch (code) {
            case 1:
                return PHONE;
            case 2:
                return EMAIL;
            case 3:
                return GA;
            default:
                throw new SeedsException("ClientAuthTypeEnum - no such enum for code: " + code);
        }
    }

    public static String getAccountNameByAuthType(String phone, String email, ClientAuthTypeEnum authTypeEnum) {
        if (PHONE.equals(authTypeEnum)) {
            return phone;
        } else if (EMAIL.equals(authTypeEnum)) {
            return email;
        } else {
            throw new GenericException("the authTypeEnum is " + authTypeEnum + " is invalid");
        }
    }
}