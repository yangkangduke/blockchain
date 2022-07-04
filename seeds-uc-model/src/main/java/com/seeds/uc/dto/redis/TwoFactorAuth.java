package com.seeds.uc.dto.redis;

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
public class TwoFactorAuth implements Serializable {
    private Long userId;
    private Long expireAt;
    // the account name to 2FA check
    private String authAccountName;
    private ClientAuthTypeEnum authType;
}
