package com.seeds.account.chain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.seeds.common.enums.Chain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
public class NativeChainBlockDto implements Serializable {

    private static final long serialVersionUID = -1L;

    Chain chain;
    long blockNumber;
    String blockHash;
    String parentHash;
    long blockTime;
}
