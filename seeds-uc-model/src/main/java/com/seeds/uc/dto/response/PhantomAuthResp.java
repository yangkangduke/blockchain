package com.seeds.uc.dto.response;

import lombok.Builder;
import lombok.Data;

/**
 * @author yk
 * @date 2022/8/7
 */
@Data
@Builder
public class PhantomAuthResp {

    private String publicAddress;
    private String nonce;

}
