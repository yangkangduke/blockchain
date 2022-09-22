package com.seeds.wallet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author rickierao
 * @email antilaw@yahoo.com
 * @date 2021/1/8
 */
@Data
@Builder
@AllArgsConstructor
public class SignedMessageDto {
    private String hash;
    private String message;
    private String signature;
    private String signatory;
}