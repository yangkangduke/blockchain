package com.seeds.uc.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/26
 */
@Data
@Builder
public class AuthTokenDTO implements Serializable {
    private String accountName;
    private String secret;
    private Long expireAt;
}