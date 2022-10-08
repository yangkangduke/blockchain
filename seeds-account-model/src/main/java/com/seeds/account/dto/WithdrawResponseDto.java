package com.seeds.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author milo
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawResponseDto {

    /**
     * Defi提币合约地址
     */
    String defiWithdrawContract;

    /**
     * 如果是Defi提币，返回的签名信息
     */
    String signedMessage;

    /**
     * 如果是Defi提币，返回的签名信息的签名
     */
    String signedSignature;

    /**
     * 截止日期(秒)
     */
    long deadline;
}
