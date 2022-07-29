package com.seeds.uc.dto.redis;

import com.seeds.uc.enums.ClientAuthTypeEnum;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class TwoFactorAuth implements Serializable {
    private Long userId;
    private Long expireAt;
    // the account name to 2FA check
    private String authAccountName;
    private ClientAuthTypeEnum authType;
}
