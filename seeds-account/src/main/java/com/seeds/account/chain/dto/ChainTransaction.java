package com.seeds.account.chain.dto;

import com.seeds.common.enums.Chain;
import lombok.*;

import java.io.Serializable;

/**
 * @author ray
 */

@Data
@Builder
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class ChainTransaction implements Serializable {
    private Chain chain;
    private String txHash;
}
