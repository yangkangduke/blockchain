package com.seeds.uc.dto.redis;

import com.seeds.uc.enums.AuthCodeUseTypeEnum;
import com.seeds.uc.enums.ClientAuthTypeEnum;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/2
 */
@Data
@Builder
public class AuthCodeDTO implements Serializable {
    private String key;
    // login name, 可能是手机号可能是邮箱
    private String name;
    private String countryCode;
    private Long expireAt;
    private String code;
    private ClientAuthTypeEnum authType;
    private AuthCodeUseTypeEnum useType;
}
