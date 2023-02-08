package com.seeds.uc.dto.redis;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
* @author yk
 * @date 2020/8/27
 */
@Data
@Builder
public class GenPhantomAuth implements Serializable {
    private String nonce;
    private Long expireAt;
}
