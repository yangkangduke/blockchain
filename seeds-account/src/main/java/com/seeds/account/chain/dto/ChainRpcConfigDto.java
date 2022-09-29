package com.seeds.account.chain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChainRpcConfigDto {
    long chainId;
    String rpc;
    String auth;
    /**
     * rpc节点类型，1-查询节点(只读)，2-发送tx节点(写)
     */
    int type;
}
