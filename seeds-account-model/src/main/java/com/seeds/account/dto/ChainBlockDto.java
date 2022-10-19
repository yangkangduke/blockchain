package com.seeds.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.seeds.account.enums.CommonStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
/**
 *
 * @author yk
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChainBlockDto implements Serializable {
    private static final long serialVersionUID = -1L;

    private long createTime;

    private long updateTime;

    private long blockNumber;

    private String blockHash;

    private long blockTime;

    private String parentHash;

    private CommonStatus status;
}
