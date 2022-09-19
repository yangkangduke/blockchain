package com.seeds.wallet.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author rickierao
 * @email antilaw@yahoo.com
 * @date 2021/1/10
 */
@Data
@Builder
public class SignMessageRequest {
    private int chain;
    private long chainId;

    private List<String> messagesToSign;
    private String address;
}
