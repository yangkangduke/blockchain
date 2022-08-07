package com.seeds.uc.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author yk
 * @date 2022/8/7
 */
@Data
@Builder
public class MetamaskAuthResp {

    private String publicAddress;
    private String nonce;

}
