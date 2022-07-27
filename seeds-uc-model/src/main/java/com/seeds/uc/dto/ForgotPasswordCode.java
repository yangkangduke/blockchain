package com.seeds.uc.dto;

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
public class ForgotPasswordCode implements Serializable {
    private String key;
    private String account;
    private String email;
    private Long expireAt;
}
