package com.seeds.uc.dto.redis;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
* @author yk
 * @date 2020/8/26
 */
@Data
@Builder
public class AuthTokenDTO implements Serializable {
    private String accountName;
    private String secret;
    private Long expireAt;
}
